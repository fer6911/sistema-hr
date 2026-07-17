package com.atlashr.atlas_hr.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenAdapterTest {

    @Mock
    private JwtUtil jwtUtil;

    private JwtTokenAdapter jwtTokenAdapter;

    private static final String USERNAME = "admin";
    private static final String ROL = "ADMIN";
    private static final String TOKEN = "jwt-token-generado";

    @BeforeEach
    void setUp() {
        jwtTokenAdapter = new JwtTokenAdapter(jwtUtil);
    }

    @Test
    void delegaGeneracionAJwtUtil() {
        when(jwtUtil.generateToken(USERNAME, ROL)).thenReturn(TOKEN);

        String resultado = jwtTokenAdapter.generateToken(USERNAME, ROL);

        assertEquals(TOKEN, resultado);
        verify(jwtUtil).generateToken(USERNAME, ROL);
    }
}
