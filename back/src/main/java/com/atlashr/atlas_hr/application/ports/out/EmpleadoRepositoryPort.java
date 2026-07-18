package com.atlashr.atlas_hr.application.ports.out;

import com.atlashr.atlas_hr.domain.model.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepositoryPort {
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    Optional<Empleado> findById(Long id);
    Empleado save(Empleado empleado);
    List<Empleado> findAll();
}
