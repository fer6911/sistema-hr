package com.atlashr.atlas_hr.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class
JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET = "dGVzdC1zZWNyZXQta2V5LWZvci10ZXN0aW5nLXB1cnBvc2VzLW9ubHk=";
    private static final long EXPIRATION = 86400000L;
    private static final String USERNAME = "admin";
    private static final String ROL = "ADMIN";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET, EXPIRATION);
    }

    @Test
    void generarTokenNoEsNiloNiVacio() {
        String token = jwtUtil.generateToken(USERNAME, ROL);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsernameDevuelveCorrecto() {
        String token = jwtUtil.generateToken(USERNAME, ROL);

        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals(USERNAME, extractedUsername);
    }

    @Test
    void extractRolDevuelveCorrecto() {
        String token = jwtUtil.generateToken(USERNAME, ROL);

        String extractedRol = jwtUtil.extractRol(token);

        assertEquals(ROL, extractedRol);
    }

    @Test
    void tokenRecienGeneradoEsValido() {
        String token = jwtUtil.generateToken(USERNAME, ROL);

        assertTrue(jwtUtil.isTokenValid(token));
    }

    @Test
    void tokenConCharsBasuraEsInvalido() {
        assertFalse(jwtUtil.isTokenValid("token-falso-absolutamente-invalido"));
    }

    @Test
    void tokenExpiradoEsInvalido() {
        JwtUtil jwtUtilConExpiracionCorta = new JwtUtil(SECRET, -1000L);

        String token = jwtUtilConExpiracionCorta.generateToken(USERNAME, ROL);

        assertFalse(jwtUtilConExpiracionCorta.isTokenValid(token));
    }
}
