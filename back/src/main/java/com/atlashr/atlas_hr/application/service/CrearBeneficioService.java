package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.CrearBeneficioDto;
import com.atlashr.atlas_hr.application.ports.in.CrearBeneficioUseCase;
import com.atlashr.atlas_hr.application.ports.out.BeneficioRepositoryPort;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.BeneficioNotValidException;
import com.atlashr.atlas_hr.domain.model.Beneficio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrearBeneficioService implements CrearBeneficioUseCase {

    private final BeneficioRepositoryPort beneficioRepositoryPort;
    private final EmpleadoRepositoryPort empleadoRepositoryPort;

    public CrearBeneficioService(BeneficioRepositoryPort beneficioRepositoryPort, EmpleadoRepositoryPort empleadoRepositoryPort) {
        this.beneficioRepositoryPort = beneficioRepositoryPort;
        this.empleadoRepositoryPort = empleadoRepositoryPort;
    }

    @Override
    public BeneficioDto crear(CrearBeneficioDto dto) {
        if (!empleadoRepositoryPort.existsById(dto.empleadoId())) {
            throw new BeneficioNotValidException(List.of("El empleado no existe"));
        }

        Beneficio beneficio = Beneficio.builder()
                .empleadoId(dto.empleadoId())
                .nombreBeneficio(dto.nombreBeneficio())
                .monto(dto.monto())
                .build();

        Beneficio guardado = beneficioRepositoryPort.save(beneficio);

        return new BeneficioDto(
                guardado.getId(),
                guardado.getEmpleadoId(),
                guardado.getNombreBeneficio(),
                guardado.getMonto()
        );
    }
}
