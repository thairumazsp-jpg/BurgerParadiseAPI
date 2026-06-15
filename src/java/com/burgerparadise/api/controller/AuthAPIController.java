package com.burgerparadise.api.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CONSTRUCCIÓN API: Servicio Web RESTful para autenticación y registro.
 * Responde en formato JSON estándar de la industria.
 * @author Thairuma
 */
@WebServlet(name = "AuthAPIController", urlPatterns = {"/api/auth/*"})
public class AuthAPIController extends HttpServlet {

    // Configuración local del pool de conexiones (Puerto alterno 3307)
    private final String url = "jdbc:mysql://localhost:3307/burger_paradise?useSSL=false&serverTimezone=UTC";
    private final String user = "root";
    private final String pass = "";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error Driver: " + e.getMessage());
        }
        return DriverManager.getConnection(url, user, pass);
    }

    /**
     * CRITERIO 2: SERVICIO DE INICIO DE SESIÓN (LOGIN) - Método GET o POST simplificado
     * Intercepta la ruta /api/auth/login
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        // Captura de parámetros desde la URL o el formulario encodificado
        String userParam = request.getParameter("username");
        String passParam = request.getParameter("password");

        // CRITERIO 3: Validación de campos vacíos en el Back-End
        if (userParam == null || passParam == null || userParam.trim().isEmpty() || passParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"status\": \"error\", \"message\": \"Faltan credenciales obligatorias (username/password)\"}");
            return;
        }

        String query = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, userParam.trim());
            stmt.setString(2, passParam.trim()); // En entornos reales se usaría un Hash (BCrypt)
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Autenticación correcta
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print("{\"status\": \"success\", \"message\": \"Autenticacion satisfactoria. Bienvenido a Burger Paradise API.\"}");
                } else {
                    // Credenciales inválidas
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.print("{\"status\": \"error\", \"message\": \"Error en la autenticacion. Usuario o contrasena incorrectos.\"}");
                }
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\": \"error\", \"message\": \"Fallo interno en la base de datos: " + e.getMessage() + "\"}");
        }
    }

    /**
     * CRITERIO 1: SERVICIO DE REGISTRO DE USUARIOS - Método POST
     * Intercepta la ruta /api/auth/register
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String userParam = request.getParameter("username");
        String passParam = request.getParameter("password");

        // Validación estructural de entrada
        if (userParam == null || passParam == null || userParam.trim().isEmpty() || passParam.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"status\": \"error\", \"message\": \"Datos de registro incompletos.\"}");
            return;
        }

        String query = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, userParam.trim());
            stmt.setString(2, passParam.trim());
            
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"status\": \"success\", \"message\": \"Usuario registrado correctamente en el servicio web.\"}");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            out.print("{\"status\": \"error\", \"message\": \"El nombre de usuario ya se encuentra registrado.\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\": \"error\", \"message\": \"Error en el servidor: " + e.getMessage() + "\"}");
        }
    }
}