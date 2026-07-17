package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.CrearUsuarioDto;
import com.atlashr.atlas_hr.application.dto.UsuarioCreadoDto;
import com.atlashr.atlas_hr.application.ports.in.RegistrarUsuarioUseCase;
import com.atlashr.atlas_hr.application.ports.out.UsuarioRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.UsuarioNotValidException;
import com.atlashr.atlas_hr.domain.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrarUsuarioService implements RegistrarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public RegistrarUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort, PasswordEncoder passwordEncoder) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioCreadoDto registrar(CrearUsuarioDto dto) {
        if (usuarioRepositoryPort.existsByUsername(dto.username())) {
            throw new UsuarioNotValidException(List.of("El nombre de usuario ya está en uso"));
        }
        if (usuarioRepositoryPort.existsByEmail(dto.email())) {
            throw new UsuarioNotValidException(List.of("El email ya está registrado"));
        }

        Usuario usuario = Usuario.builder()
                .username(dto.username())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .build();

        Usuario guardado = usuarioRepositoryPort.save(usuario);

        return new UsuarioCreadoDto(guardado.getUsername(), guardado.getEmail(), guardado.getRol());
    }
}
