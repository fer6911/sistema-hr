package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.mapper.EmpleadoApplicationMapper;
import com.atlashr.atlas_hr.application.ports.in.ListarEmpleadosUseCase;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.model.Empleado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarEmpleadosService implements ListarEmpleadosUseCase {

    private final EmpleadoRepositoryPort empleadoRepositoryPort;
    private final EmpleadoApplicationMapper mapper;

    public ListarEmpleadosService(EmpleadoRepositoryPort empleadoRepositoryPort, EmpleadoApplicationMapper mapper) {
        this.empleadoRepositoryPort = empleadoRepositoryPort;
        this.mapper = mapper;
    }

    @Override
    public List<EmpleadoDto> listarTodos() {
        return empleadoRepositoryPort.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }
}
