package com.atlashr.atlas_hr.infrastructure.output.persistence;

import com.atlashr.atlas_hr.application.ports.out.UsuarioRepositoryPort;
import com.atlashr.atlas_hr.domain.model.Usuario;
import com.atlashr.atlas_hr.infrastructure.mapper.UsuarioPersistenceMapper;
import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.UsuarioEntity;
import com.atlashr.atlas_hr.infrastructure.output.persistence.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    private final UsuarioRepository repository;
    private final UsuarioPersistenceMapper mapper;

    public UsuarioPersistenceAdapter(UsuarioRepository repository, UsuarioPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
        UsuarioEntity entity = mapper.toEntity(usuario);
        repository.save(entity);
        return usuario;
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toDomain);
    }
}
