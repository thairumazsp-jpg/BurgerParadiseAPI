package com.burgerparadise.api.dao;

import com.burgerparadise.api.model.Usuario;
import java.sql.*;

/**
 * Componente de acceso a datos (DAO) para la gestión de usuarios.
 * Permite desvincular las consultas SQL de la lógica del controlador.
 */
public class UsuarioDAO {
    // Configuración de la cadena de conexión local (Puerto alterno 3307)
    private final String url = "jdbc:mysql://localhost:3307/burger_paradise?useSSL=false&serverTimezone=UTC";
    private final String user = "root";
    private final String pass = "";

    /**
     * Establece y retorna la conexión activa con la base de datos MariaDB/MySQL.
     */
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver no encontrado: " + e.getMessage());
        }
        return DriverManager.getConnection(url, user, pass);
    }

    /**
     * Valida si las credenciales de un usuario coinciden en la base de datos.
     */
    public boolean validarLogin(String username, String password) {
        String query = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username.trim());
            stmt.setString(2, password.trim());
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retorna true si encuentra coincidencia
            }
        } catch (SQLException e) {
            System.err.println("Error en validarLogin: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserta un nuevo registro de usuario en la base de datos.
     */
    public boolean registrarUsuario(Usuario usuario) throws SQLException {
        String query = "INSERT INTO usuarios (username, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, usuario.getUsername().trim());
            stmt.setString(2, usuario.getPassword().trim());
            
            return stmt.executeUpdate() > 0;
        }
    }
}
