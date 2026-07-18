package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.EditarEmpleadoDto;
import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.mapper.EmpleadoApplicationMapper;
import com.atlashr.atlas_hr.application.ports.in.EditarEmpleadoUseCase;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.model.Empleado;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditarEmpleadoService implements EditarEmpleadoUseCase {

    private final EmpleadoRepositoryPort empleadoRepositoryPort;
    private final EmpleadoApplicationMapper mapper;

    public EditarEmpleadoService(EmpleadoRepositoryPort empleadoRepositoryPort, EmpleadoApplicationMapper mapper) {
        this.empleadoRepositoryPort = empleadoRepositoryPort;
        this.mapper = mapper;
    }

    @Override
    public Optional<EmpleadoDto> editar(Long id, EditarEmpleadoDto dto) {
        return empleadoRepositoryPort.findById(id).map(empleado -> {
            Empleado actualizado = Empleado.builder()
                .id(empleado.getId())
                .nombre(dto.nombre() != null ? dto.nombre() : empleado.getNombre())
                .apellido(dto.apellido() != null ? dto.apellido() : empleado.getApellido())
                .email(dto.email() != null ? dto.email() : empleado.getEmail())
                .cargo(dto.cargo() != null ? dto.cargo() : empleado.getCargo())
                .salario(dto.salario() != null ? dto.salario() : empleado.getSalario())
                .fechaIngreso(dto.fechaIngreso() != null ? dto.fechaIngreso() : empleado.getFechaIngreso())
                .ciudad(dto.ciudad() != null ? dto.ciudad() : empleado.getCiudad())
                .activo(dto.activo() != null ? dto.activo() : empleado.isActivo())
                .build();

            Empleado guardado = empleadoRepositoryPort.save(actualizado);
            return mapper.toDto(guardado);
        });
    }
}
