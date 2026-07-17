package com.atlashr.atlas_hr.application.ports.out;

import com.atlashr.atlas_hr.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepositoryPort {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Usuario save(Usuario usuario);
    Optional<Usuario> findByUsername(String username);
}
