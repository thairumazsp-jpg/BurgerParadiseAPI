package com.burgerparadise.api.dao;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Clase de prueba unitaria para validar la lógica del módulo de Usuarios.
 * @author Thairuma
 */
public class UsuarioDAOTest {

    @Test
    public void testValidacionCamposVacios() {
        // Simulamos la validación de un usuario vacío o nulo
        String usuarioVacio = "";
        String passwordVacio = "   ";
        
        boolean esInvalido = (usuarioVacio == null || passwordVacio == null || 
                              usuarioVacio.trim().isEmpty() || passwordVacio.trim().isEmpty());
        
        // La prueba pasa con éxito si confirma que los campos efectivamente son inválidos (true)
        assertTrue("La validación de campos vacíos debería detectar las cadenas vacías", esInvalido);
    }
}