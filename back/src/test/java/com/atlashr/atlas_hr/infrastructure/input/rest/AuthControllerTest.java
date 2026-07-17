package com.atlashr.atlas_hr.infrastructure.input.rest;

import com.atlashr.atlas_hr.application.dto.CrearUsuarioDto;
import com.atlashr.atlas_hr.application.dto.LoginDto;
import com.atlashr.atlas_hr.application.dto.TokenResponseDto;
import com.atlashr.atlas_hr.application.dto.UsuarioCreadoDto;
import com.atlashr.atlas_hr.application.ports.in.LoginUseCase;
import com.atlashr.atlas_hr.application.ports.in.RegistrarUsuarioUseCase;
import com.atlashr.atlas_hr.domain.exception.CredencialesInvalidasException;
import com.atlashr.atlas_hr.domain.exception.UsuarioNotValidException;
import com.atlashr.atlas_hr.infrastructure.security.JwtUtil;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerExitoso() throws Exception {
        CrearUsuarioDto dto = new CrearUsuarioDto("john123", "john@example.com", "secret123");
        UsuarioCreadoDto creado = new UsuarioCreadoDto("john123", "john@example.com", "USER");
        when(registrarUsuarioUseCase.registrar(any(CrearUsuarioDto.class))).thenReturn(creado);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data.username").value("john123"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    void registerDatosInvalidosEmail() throws Exception {
        CrearUsuarioDto dto = new CrearUsuarioDto("john123", "noemail", "secret123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));
    }

    @Test
    void registerDatosInvalidosUsernameVacio() throws Exception {
        CrearUsuarioDto dto = new CrearUsuarioDto("", "john@example.com", "secret123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));
    }

    @Test
    void registerUsernameDuplicado() throws Exception {
        CrearUsuarioDto dto = new CrearUsuarioDto("john123", "john@example.com", "secret123");
        when(registrarUsuarioUseCase.registrar(any(CrearUsuarioDto.class)))
                .thenThrow(new UsuarioNotValidException(List.of("El nombre de usuario ya está en uso")));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errors[0]").value("El nombre de usuario ya está en uso"));
    }

    @Test
    void loginExitoso() throws Exception {
        LoginDto dto = new LoginDto("john123", "secret123");
        TokenResponseDto tokenResponse = new TokenResponseDto("jwt-token", "john123", "USER");
        when(loginUseCase.login(any(LoginDto.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data.username").value("john123"))
                .andExpect(header().string("Authorization", "jwt-token"));
    }

    @Test
    void loginCredencialesInvalidas() throws Exception {
        LoginDto dto = new LoginDto("john123", "wrongpassword");
        when(loginUseCase.login(any(LoginDto.class)))
                .thenThrow(new CredencialesInvalidasException());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errors[0]").value("Credenciales inválidas"));
    }

    @Test
    void loginBodyInvalido() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));
    }
}
