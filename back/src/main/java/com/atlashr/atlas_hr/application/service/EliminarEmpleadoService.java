package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.ports.in.EliminarEmpleadoUseCase;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class EliminarEmpleadoService implements EliminarEmpleadoUseCase {

    private final EmpleadoRepositoryPort empleadoRepositoryPort;

    public EliminarEmpleadoService(EmpleadoRepositoryPort empleadoRepositoryPort) {
        this.empleadoRepositoryPort = empleadoRepositoryPort;
    }

    @Override
    public boolean eliminar(Long id) {
        if (!empleadoRepositoryPort.existsById(id)) {
            return false;
        }
        empleadoRepositoryPort.deleteById(id);
        return true;
    }
}
