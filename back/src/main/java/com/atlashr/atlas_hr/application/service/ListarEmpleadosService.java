package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.ports.in.ListarEmpleadosUseCase;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.model.Empleado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarEmpleadosService implements ListarEmpleadosUseCase {

    private final EmpleadoRepositoryPort empleadoRepositoryPort;

    public ListarEmpleadosService(EmpleadoRepositoryPort empleadoRepositoryPort) {
        this.empleadoRepositoryPort = empleadoRepositoryPort;
    }

    @Override
    public List<EmpleadoDto> listarTodos() {
        return empleadoRepositoryPort.findAll().stream()
                .map(e -> new EmpleadoDto(
                        e.getId(),
                        e.getNombre(),
                        e.getApellido(),
                        e.getEmail(),
                        e.getCargo(),
                        e.getSalario(),
                        e.getFechaIngreso(),
                        e.isActivo()
                ))
                .toList();
    }
}
