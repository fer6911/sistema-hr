package com.atlashr.atlas_hr.infrastructure.input.rest;

import com.atlashr.atlas_hr.application.dto.CrearUsuarioDto;
import com.atlashr.atlas_hr.application.dto.LoginDto;
import com.atlashr.atlas_hr.application.dto.TokenResponseDto;
import com.atlashr.atlas_hr.application.dto.UsuarioCreadoDto;
import com.atlashr.atlas_hr.application.dto.UsuarioLoginDto;
import com.atlashr.atlas_hr.application.ports.in.LoginUseCase;
import com.atlashr.atlas_hr.application.ports.in.RegistrarUsuarioUseCase;
import com.atlashr.atlas_hr.infrastructure.config.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(RegistrarUsuarioUseCase registrarUsuarioUseCase, LoginUseCase loginUseCase) {
        this.registrarUsuarioUseCase = registrarUsuarioUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UsuarioCreadoDto>> register(@Valid @RequestBody CrearUsuarioDto dto) {
        UsuarioCreadoDto creado = registrarUsuarioUseCase.registrar(dto);
        return ResponseEntity.ok(ApiResponse.success("Usuario registrado exitosamente", creado));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UsuarioLoginDto>> login(@Valid @RequestBody LoginDto dto) {
        TokenResponseDto tokenResponse = loginUseCase.login(dto);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.token())
                .body(ApiResponse.success("Login exitoso",
                        new UsuarioLoginDto(tokenResponse.username(), tokenResponse.rol())));
    }
}
