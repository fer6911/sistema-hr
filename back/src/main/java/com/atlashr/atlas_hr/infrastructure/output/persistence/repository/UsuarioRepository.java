package com.atlashr.atlas_hr.infrastructure.output.persistence.repository;

import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<UsuarioEntity> findByUsername(String username);
}
