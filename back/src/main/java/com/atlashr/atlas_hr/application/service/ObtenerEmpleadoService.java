package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.mapper.EmpleadoApplicationMapper;
import com.atlashr.atlas_hr.application.ports.in.ObtenerEmpleadoUseCase;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ObtenerEmpleadoService implements ObtenerEmpleadoUseCase {

    private final EmpleadoRepositoryPort empleadoRepositoryPort;
    private final EmpleadoApplicationMapper mapper;

    public ObtenerEmpleadoService(EmpleadoRepositoryPort empleadoRepositoryPort, EmpleadoApplicationMapper mapper) {
        this.empleadoRepositoryPort = empleadoRepositoryPort;
        this.mapper = mapper;
    }

    @Override
    public Optional<EmpleadoDto> obtenerPorId(Long id) {
        return empleadoRepositoryPort.findById(id).map(mapper::toDto);
    }
}
