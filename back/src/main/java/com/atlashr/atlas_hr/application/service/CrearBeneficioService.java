package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.CrearBeneficioDto;
import com.atlashr.atlas_hr.application.mapper.BeneficioApplicationMapper;
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
    private final BeneficioApplicationMapper mapper;

    public CrearBeneficioService(BeneficioRepositoryPort beneficioRepositoryPort, EmpleadoRepositoryPort empleadoRepositoryPort, BeneficioApplicationMapper mapper) {
        this.beneficioRepositoryPort = beneficioRepositoryPort;
        this.empleadoRepositoryPort = empleadoRepositoryPort;
        this.mapper = mapper;
    }

    @Override
    public BeneficioDto crear(CrearBeneficioDto dto) {
        if (!empleadoRepositoryPort.existsById(dto.empleadoId())) {
            throw new BeneficioNotValidException(List.of("El empleado no existe"));
        }

        Beneficio beneficio = mapper.toDomain(dto);
        Beneficio guardado = beneficioRepositoryPort.save(beneficio);

        return mapper.toDto(guardado);
    }
}
