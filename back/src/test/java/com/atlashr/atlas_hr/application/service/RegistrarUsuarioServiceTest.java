package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.CrearUsuarioDto;
import com.atlashr.atlas_hr.application.dto.UsuarioCreadoDto;
import com.atlashr.atlas_hr.application.mapper.UsuarioApplicationMapper;
import com.atlashr.atlas_hr.application.ports.out.UsuarioRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.UsuarioNotValidException;
import com.atlashr.atlas_hr.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarUsuarioServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioApplicationMapper mapper;

    private RegistrarUsuarioService registrarUsuarioService;

    private static final String USERNAME = "john123";
    private static final String EMAIL = "john@example.com";
    private static final String PASSWORD = "secret123";
    private static final String ENCODED_PASSWORD = "$2a$encoded";

    @BeforeEach
    void setUp() {
        registrarUsuarioService = new RegistrarUsuarioService(usuarioRepositoryPort, passwordEncoder, mapper);
    }

    @Test
    void registrarUsuarioExitoso() {
        when(usuarioRepositoryPort.existsByUsername(USERNAME)).thenReturn(false);
        when(usuarioRepositoryPort.existsByEmail(EMAIL)).thenReturn(false);
        when(mapper.toDomain(any(CrearUsuarioDto.class))).thenReturn(
                Usuario.builder().username(USERNAME).email(EMAIL).password(PASSWORD).build()
        );
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toCreadoDto(any(Usuario.class))).thenReturn(new UsuarioCreadoDto(USERNAME, EMAIL, "USER"));

        CrearUsuarioDto dto = new CrearUsuarioDto(USERNAME, EMAIL, PASSWORD);
        UsuarioCreadoDto resultado = registrarUsuarioService.registrar(dto);

        assertEquals(USERNAME, resultado.username());
        assertEquals(EMAIL, resultado.email());
        assertEquals("USER", resultado.rol());
    }

    @Test
    void usernameDuplicadoLanzaExcepcion() {
        when(usuarioRepositoryPort.existsByUsername(USERNAME)).thenReturn(true);

        CrearUsuarioDto dto = new CrearUsuarioDto(USERNAME, EMAIL, PASSWORD);
        UsuarioNotValidException ex = assertThrows(UsuarioNotValidException.class, () ->
                registrarUsuarioService.registrar(dto)
        );
        assertTrue(ex.getErrors().contains("El nombre de usuario ya está en uso"));
    }

    @Test
    void emailDuplicadoLanzaExcepcion() {
        when(usuarioRepositoryPort.existsByUsername(USERNAME)).thenReturn(false);
        when(usuarioRepositoryPort.existsByEmail(EMAIL)).thenReturn(true);

        CrearUsuarioDto dto = new CrearUsuarioDto(USERNAME, EMAIL, PASSWORD);
        UsuarioNotValidException ex = assertThrows(UsuarioNotValidException.class, () ->
                registrarUsuarioService.registrar(dto)
        );
        assertTrue(ex.getErrors().contains("El email ya está registrado"));
    }

    @Test
    void passwordSeEncriptaConBCrypt() {
        when(usuarioRepositoryPort.existsByUsername(USERNAME)).thenReturn(false);
        when(usuarioRepositoryPort.existsByEmail(EMAIL)).thenReturn(false);
        when(mapper.toDomain(any(CrearUsuarioDto.class))).thenReturn(
                Usuario.builder().username(USERNAME).email(EMAIL).password(PASSWORD).build()
        );
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toCreadoDto(any(Usuario.class))).thenReturn(new UsuarioCreadoDto(USERNAME, EMAIL, "USER"));

        CrearUsuarioDto dto = new CrearUsuarioDto(USERNAME, EMAIL, PASSWORD);
        registrarUsuarioService.registrar(dto);

        verify(passwordEncoder).encode(PASSWORD);
    }

    @Test
    void seGuardaEnRepositorio() {
        when(usuarioRepositoryPort.existsByUsername(USERNAME)).thenReturn(false);
        when(usuarioRepositoryPort.existsByEmail(EMAIL)).thenReturn(false);
        when(mapper.toDomain(any(CrearUsuarioDto.class))).thenReturn(
                Usuario.builder().username(USERNAME).email(EMAIL).password(PASSWORD).build()
        );
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toCreadoDto(any(Usuario.class))).thenReturn(new UsuarioCreadoDto(USERNAME, EMAIL, "USER"));

        CrearUsuarioDto dto = new CrearUsuarioDto(USERNAME, EMAIL, PASSWORD);
        registrarUsuarioService.registrar(dto);

        verify(usuarioRepositoryPort).save(any(Usuario.class));
    }

    @Test
    void usernameDuplicadoNoLlamaSave() {
        when(usuarioRepositoryPort.existsByUsername(USERNAME)).thenReturn(true);

        CrearUsuarioDto dto = new CrearUsuarioDto(USERNAME, EMAIL, PASSWORD);
        assertThrows(UsuarioNotValidException.class, () ->
                registrarUsuarioService.registrar(dto)
        );

        verify(usuarioRepositoryPort, never()).save(any());
    }
}
