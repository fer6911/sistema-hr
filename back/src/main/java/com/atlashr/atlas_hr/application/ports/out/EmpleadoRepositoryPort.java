package com.atlashr.atlas_hr.application.ports.out;

import com.atlashr.atlas_hr.domain.model.Empleado;

import java.util.List;

public interface EmpleadoRepositoryPort {
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    Empleado save(Empleado empleado);
    List<Empleado> findAll();
}
