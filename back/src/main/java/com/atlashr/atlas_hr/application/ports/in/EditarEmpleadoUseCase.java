package com.atlashr.atlas_hr.application.ports.in;

import com.atlashr.atlas_hr.application.dto.EditarEmpleadoDto;
import com.atlashr.atlas_hr.application.dto.EmpleadoDto;

import java.util.Optional;

public interface EditarEmpleadoUseCase {
    Optional<EmpleadoDto> editar(Long id, EditarEmpleadoDto dto);
}
