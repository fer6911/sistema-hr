package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.LoginDto;
import com.atlashr.atlas_hr.application.dto.TokenResponseDto;
import com.atlashr.atlas_hr.application.ports.in.LoginUseCase;
import com.atlashr.atlas_hr.application.ports.out.UsuarioRepositoryPort;
import com.atlashr.atlas_hr.domain.model.Usuario;
import com.atlashr.atlas_hr.infrastructure.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginService(UsuarioRepositoryPort usuarioRepositoryPort, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public TokenResponseDto login(LoginDto dto) {
        Usuario usuario = usuarioRepositoryPort.findByUsername(dto.username())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(dto.password(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRol());
        return new TokenResponseDto(token, usuario.getUsername(), usuario.getRol());
    }
}
