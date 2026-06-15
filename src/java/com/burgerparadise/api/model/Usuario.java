package com.burgerparadise.api.model;

/**
 * ESTÁNDAR DE CODIFICACIÓN: Clase JavaBean para la entidad Usuario.
 * Encapsula las credenciales de autenticación del servicio web.
 * @author Thairuma
 */
public class Usuario {
    private int idUsuario;
    private String username;
    private String password;

    public Usuario() {}

    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Métodos Getter y Setter obligatorios
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}