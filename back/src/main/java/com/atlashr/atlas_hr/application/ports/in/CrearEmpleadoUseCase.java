package com.atlashr.atlas_hr.application.ports.in;

import com.atlashr.atlas_hr.application.dto.CrearEmpleadoDto;
import com.atlashr.atlas_hr.application.dto.EmpleadoDto;

public interface CrearEmpleadoUseCase {
    EmpleadoDto crear(CrearEmpleadoDto dto);
}
