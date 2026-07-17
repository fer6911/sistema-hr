package com.atlashr.atlas_hr.application.ports.out;

import com.atlashr.atlas_hr.domain.model.Beneficio;

import java.util.List;
import java.util.Optional;

public interface BeneficioRepositoryPort {
    Optional<Beneficio> findById(Long id);
    Beneficio save(Beneficio beneficio);
    void deleteById(Long id);
    List<Beneficio> findByEmpleadoId(Long empleadoId);
}
