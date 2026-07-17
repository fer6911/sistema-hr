package com.atlashr.atlas_hr.infrastructure.output.persistence.repository;

import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {
    boolean existsByEmail(String email);
}
