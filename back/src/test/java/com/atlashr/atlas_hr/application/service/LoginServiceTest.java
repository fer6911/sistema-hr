package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.LoginDto;
import com.atlashr.atlas_hr.application.dto.TokenResponseDto;
import com.atlashr.atlas_hr.application.ports.out.TokenGenerationPort;
import com.atlashr.atlas_hr.application.ports.out.UsuarioRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.CredencialesInvalidasException;
import com.atlashr.atlas_hr.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenGenerationPort tokenGenerationPort;

    private LoginService loginService;

    private static final String USERNAME = "john123";
    private static final String PASSWORD = "secret123";
    private static final String ENCODED_PASSWORD = "$2a$encoded";
    private static final String ROL = "USER";
    private static final String TOKEN = "jwt-token-test";

    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        loginService = new LoginService(usuarioRepositoryPort, passwordEncoder, tokenGenerationPort);
        usuarioValido = Usuario.builder()
                .username(USERNAME)
                .email("john@example.com")
                .password(ENCODED_PASSWORD)
                .rol(ROL)
                .build();
    }

    @Test
    void loginExitoso() {
        when(usuarioRepositoryPort.findByUsername(USERNAME)).thenReturn(Optional.of(usuarioValido));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(tokenGenerationPort.generateToken(USERNAME, ROL)).thenReturn(TOKEN);

        LoginDto dto = new LoginDto(USERNAME, PASSWORD);
        TokenResponseDto resultado = loginService.login(dto);

        assertEquals(TOKEN, resultado.token());
        assertEquals(USERNAME, resultado.username());
        assertEquals(ROL, resultado.rol());
    }

    @Test
    void usuarioNoExisteLanzaExcepcion() {
        when(usuarioRepositoryPort.findByUsername(USERNAME)).thenReturn(Optional.empty());

        LoginDto dto = new LoginDto(USERNAME, PASSWORD);
        CredencialesInvalidasException ex = assertThrows(CredencialesInvalidasException.class, () ->
                loginService.login(dto)
        );
        assertEquals("Credenciales inválidas", ex.getMessage());
        assertTrue(ex.getErrors().contains("Credenciales inválidas"));
    }

    @Test
    void passwordIncorrectaLanzaExcepcion() {
        when(usuarioRepositoryPort.findByUsername(USERNAME)).thenReturn(Optional.of(usuarioValido));
        when(passwordEncoder.matches("wrongpassword", ENCODED_PASSWORD)).thenReturn(false);

        LoginDto dto = new LoginDto(USERNAME, "wrongpassword");
        CredencialesInvalidasException ex = assertThrows(CredencialesInvalidasException.class, () ->
                loginService.login(dto)
        );
        assertEquals("Credenciales inválidas", ex.getMessage());
    }

    @Test
    void seGeneraToken() {
        when(usuarioRepositoryPort.findByUsername(USERNAME)).thenReturn(Optional.of(usuarioValido));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(tokenGenerationPort.generateToken(USERNAME, ROL)).thenReturn(TOKEN);

        LoginDto dto = new LoginDto(USERNAME, PASSWORD);
        loginService.login(dto);

        verify(tokenGenerationPort).generateToken(USERNAME, ROL);
    }

    @Test
    void passwordCorrectaNoLanzaExcepcion() {
        when(usuarioRepositoryPort.findByUsername(USERNAME)).thenReturn(Optional.of(usuarioValido));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(tokenGenerationPort.generateToken(USERNAME, ROL)).thenReturn(TOKEN);

        LoginDto dto = new LoginDto(USERNAME, PASSWORD);
        assertDoesNotThrow(() -> loginService.login(dto));
    }

    @Test
    void usuarioInactivoLanzaExcepcion() {
        Usuario usuarioInactivo = Usuario.builder()
                .username("inactive")
                .email("inactive@example.com")
                .password(ENCODED_PASSWORD)
                .rol(ROL)
                .activo(false)
                .build();

        when(usuarioRepositoryPort.findByUsername("inactive")).thenReturn(Optional.of(usuarioInactivo));

        LoginDto dto = new LoginDto("inactive", PASSWORD);
        CredencialesInvalidasException ex = assertThrows(CredencialesInvalidasException.class, () ->
                loginService.login(dto)
        );

        assertEquals("Usuario inactivo", ex.getMessage());
        verify(tokenGenerationPort, never()).generateToken(anyString(), anyString());
    }
}
