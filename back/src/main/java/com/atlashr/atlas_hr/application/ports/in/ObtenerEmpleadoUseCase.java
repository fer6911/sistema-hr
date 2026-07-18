package com.atlashr.atlas_hr.application.ports.in;

import com.atlashr.atlas_hr.application.dto.EmpleadoDto;

import java.util.Optional;

public interface ObtenerEmpleadoUseCase {
    Optional<EmpleadoDto> obtenerPorId(Long id);
}
