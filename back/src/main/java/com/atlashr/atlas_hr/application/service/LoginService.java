package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.LoginDto;
import com.atlashr.atlas_hr.application.dto.TokenResponseDto;
import com.atlashr.atlas_hr.application.ports.in.LoginUseCase;
import com.atlashr.atlas_hr.application.ports.out.TokenGenerationPort;
import com.atlashr.atlas_hr.application.ports.out.UsuarioRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.CredencialesInvalidasException;
import com.atlashr.atlas_hr.domain.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerationPort tokenGenerationPort;

    public LoginService(UsuarioRepositoryPort usuarioRepositoryPort, PasswordEncoder passwordEncoder, TokenGenerationPort tokenGenerationPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerationPort = tokenGenerationPort;
    }

    @Override
    public TokenResponseDto login(LoginDto dto) {
        Usuario usuario = usuarioRepositoryPort.findByUsername(dto.username())
                .orElseThrow(() -> new CredencialesInvalidasException());

        if (!passwordEncoder.matches(dto.password(), usuario.getPassword())) {
            throw new CredencialesInvalidasException();
        }

        String token = tokenGenerationPort.generateToken(usuario.getUsername(), usuario.getRol());
        return new TokenResponseDto(token, usuario.getUsername(), usuario.getRol());
    }
}
