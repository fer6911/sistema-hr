package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.CrearEmpleadoDto;
import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.mapper.EmpleadoApplicationMapper;
import com.atlashr.atlas_hr.application.ports.in.CrearEmpleadoUseCase;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.EmpleadoNotValidException;
import com.atlashr.atlas_hr.domain.model.Empleado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrearEmpleadoService implements CrearEmpleadoUseCase {

    private final EmpleadoRepositoryPort empleadoRepositoryPort;
    private final EmpleadoApplicationMapper mapper;

    public CrearEmpleadoService(EmpleadoRepositoryPort empleadoRepositoryPort, EmpleadoApplicationMapper mapper) {
        this.empleadoRepositoryPort = empleadoRepositoryPort;
        this.mapper = mapper;
    }

    @Override
    public EmpleadoDto crear(CrearEmpleadoDto dto) {
        if (empleadoRepositoryPort.existsByEmail(dto.email())) {
            throw new EmpleadoNotValidException(List.of("El email ya está registrado"));
        }

        Empleado empleado = mapper.toDomain(dto);
        Empleado guardado = empleadoRepositoryPort.save(empleado);

        return mapper.toDto(guardado);
    }
}
