package com.atlashr.atlas_hr.infrastructure.output.persistence.repository;

import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.BeneficioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeneficioRepository extends JpaRepository<BeneficioEntity, Long> {
    List<BeneficioEntity> findByEmpleadoId(Long empleadoId);
}
