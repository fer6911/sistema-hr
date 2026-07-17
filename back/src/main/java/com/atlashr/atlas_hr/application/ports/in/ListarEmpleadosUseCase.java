package com.atlashr.atlas_hr.application.ports.in;

import com.atlashr.atlas_hr.application.dto.EmpleadoDto;

import java.util.List;

public interface ListarEmpleadosUseCase {
    List<EmpleadoDto> listarTodos();
}
