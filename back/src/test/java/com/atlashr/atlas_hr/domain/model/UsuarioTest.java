package com.atlashr.atlas_hr.domain.model;

import com.atlashr.atlas_hr.domain.exception.UsuarioNotValidException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private static final String VALID_USERNAME = "john123";
    private static final String VALID_EMAIL = "john@example.com";
    private static final String VALID_PASSWORD = "secret123";

    @Test
    void registroExitosoConDatosValidos() {
        Usuario usuario = Usuario.builder()
                .username(VALID_USERNAME)
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .build();

        assertEquals(VALID_USERNAME, usuario.getUsername());
        assertEquals(VALID_EMAIL, usuario.getEmail());
        assertEquals(VALID_PASSWORD, usuario.getPassword());
        assertEquals("USER", usuario.getRol());
        assertTrue(usuario.isActivo());
    }

    @Test
    void usernameVacioLanzaExcepcion() {
        UsuarioNotValidException ex = assertThrows(UsuarioNotValidException.class, () ->
                Usuario.builder()
                        .username("")
                        .email(VALID_EMAIL)
                        .password(VALID_PASSWORD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El nombre de usuario no puede estar vacío"));
    }

    @Test
    void usernameNuloLanzaExcepcion() {
        UsuarioNotValidException ex = assertThrows(UsuarioNotValidException.class, () ->
                Usuario.builder()
                        .username(null)
                        .email(VALID_EMAIL)
                        .password(VALID_PASSWORD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El nombre de usuario no puede estar vacío"));
    }

    @Test
    void usernameMayorA30CharsLanzaExcepcion() {
        String longUsername = "a".repeat(31);

        UsuarioNotValidException ex = assertThrows(UsuarioNotValidException.class, () ->
                Usuario.builder()
                        .username(longUsername)
                        .email(VALID_EMAIL)
                        .password(VALID_PASSWORD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El nombre de usuario es demasiado largo"));
    }

    @Test
    void emailVacioLanzaExcepcion() {
        UsuarioNotValidException ex = assertThrows(UsuarioNotValidException.class, () ->
                Usuario.builder()
                        .username(VALID_USERNAME)
                        .email("")
                        .password(VALID_PASSWORD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El email no puede estar vacío"));
    }

    @Test
    void emailFormatoInvalidoLanzaExcepcion() {
        UsuarioNotValidException ex = assertThrows(UsuarioNotValidException.class, () ->
                Usuario.builder()
                        .username(VALID_USERNAME)
                        .email("noemail")
                        .password(VALID_PASSWORD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El email no tiene un formato válido"));
    }

    @Test
    void passwordVacioLanzaExcepcion() {
        UsuarioNotValidException ex = assertThrows(UsuarioNotValidException.class, () ->
                Usuario.builder()
                        .username(VALID_USERNAME)
                        .email(VALID_EMAIL)
                        .password("")
                        .build()
        );
        assertTrue(ex.getErrors().contains("La contraseña no puede estar vacía"));
    }

    @Test
    void passwordMenosDe6CharsLanzaExcepcion() {
        UsuarioNotValidException ex = assertThrows(UsuarioNotValidException.class, () ->
                Usuario.builder()
                        .username(VALID_USERNAME)
                        .email(VALID_EMAIL)
                        .password("12345")
                        .build()
        );
        assertTrue(ex.getErrors().contains("La contraseña debe tener al menos 6 caracteres"));
    }

    @Test
    void multiplesErroresSeAcumulan() {
        UsuarioNotValidException ex = assertThrows(UsuarioNotValidException.class, () ->
                Usuario.builder()
                        .username("")
                        .email("invalido")
                        .password("123")
                        .build()
        );
        assertEquals(3, ex.getErrors().size());
    }

    @Test
    void rolPersonalizadoSeGuarda() {
        Usuario usuario = Usuario.builder()
                .username(VALID_USERNAME)
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .rol("ADMIN")
                .build();

        assertEquals("ADMIN", usuario.getRol());
    }

    @Test
    void rolPorDefectoEsUser() {
        Usuario usuario = Usuario.builder()
                .username(VALID_USERNAME)
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .build();

        assertEquals("USER", usuario.getRol());
    }
}
