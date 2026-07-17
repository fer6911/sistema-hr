package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.ports.in.ListarBeneficiosUseCase;
import com.atlashr.atlas_hr.application.ports.out.BeneficioRepositoryPort;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.BeneficioNotValidException;
import com.atlashr.atlas_hr.domain.model.Beneficio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarBeneficiosService implements ListarBeneficiosUseCase {

    private final BeneficioRepositoryPort beneficioRepositoryPort;
    private final EmpleadoRepositoryPort empleadoRepositoryPort;

    public ListarBeneficiosService(BeneficioRepositoryPort beneficioRepositoryPort, EmpleadoRepositoryPort empleadoRepositoryPort) {
        this.beneficioRepositoryPort = beneficioRepositoryPort;
        this.empleadoRepositoryPort = empleadoRepositoryPort;
    }

    @Override
    public List<BeneficioDto> listarPorEmpleado(Long empleadoId) {
        if (!empleadoRepositoryPort.existsById(empleadoId)) {
            throw new BeneficioNotValidException(List.of("El empleado no existe"));
        }

        return beneficioRepositoryPort.findByEmpleadoId(empleadoId).stream()
                .map(b -> new BeneficioDto(
                        b.getId(),
                        b.getEmpleadoId(),
                        b.getNombreBeneficio(),
                        b.getMonto()
                ))
                .toList();
    }
}
