package com.atlashr.atlas_hr.infrastructure.output.persistence;

import com.atlashr.atlas_hr.application.ports.out.UsuarioRepositoryPort;
import com.atlashr.atlas_hr.domain.model.Usuario;
import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.UsuarioEntity;
import com.atlashr.atlas_hr.infrastructure.output.persistence.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    private final UsuarioRepository repository;

    public UsuarioPersistenceAdapter(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity(
                usuario.getUsername(), usuario.getEmail(), usuario.getPassword(),
                usuario.getRol(), usuario.isActivo()
        );
        repository.save(entity);
        return usuario;
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return repository.findByUsername(username).map(e ->
                Usuario.builder()
                        .username(e.getUsername())
                        .email(e.getEmail())
                        .password(e.getPassword())
                        .rol(e.getRol())
                        .build()
        );
    }
}
