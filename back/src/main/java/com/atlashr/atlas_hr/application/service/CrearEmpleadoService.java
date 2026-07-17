package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.CrearEmpleadoDto;
import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.ports.in.CrearEmpleadoUseCase;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.EmpleadoNotValidException;
import com.atlashr.atlas_hr.domain.model.Empleado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrearEmpleadoService implements CrearEmpleadoUseCase {

    private final EmpleadoRepositoryPort empleadoRepositoryPort;

    public CrearEmpleadoService(EmpleadoRepositoryPort empleadoRepositoryPort) {
        this.empleadoRepositoryPort = empleadoRepositoryPort;
    }

    @Override
    public EmpleadoDto crear(CrearEmpleadoDto dto) {
        if (empleadoRepositoryPort.existsByEmail(dto.email())) {
            throw new EmpleadoNotValidException(List.of("El email ya está registrado"));
        }

        Empleado empleado = Empleado.builder()
                .nombre(dto.nombre())
                .apellido(dto.apellido())
                .email(dto.email())
                .cargo(dto.cargo())
                .salario(dto.salario())
                .fechaIngreso(dto.fechaIngreso())
                .build();

        Empleado guardado = empleadoRepositoryPort.save(empleado);

        return new EmpleadoDto(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getApellido(),
                guardado.getEmail(),
                guardado.getCargo(),
                guardado.getSalario(),
                guardado.getFechaIngreso(),
                guardado.isActivo()
        );
    }
}
