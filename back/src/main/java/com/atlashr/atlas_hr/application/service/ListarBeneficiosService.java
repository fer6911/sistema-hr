package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.mapper.BeneficioApplicationMapper;
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
    private final BeneficioApplicationMapper mapper;

    public ListarBeneficiosService(BeneficioRepositoryPort beneficioRepositoryPort, EmpleadoRepositoryPort empleadoRepositoryPort, BeneficioApplicationMapper mapper) {
        this.beneficioRepositoryPort = beneficioRepositoryPort;
        this.empleadoRepositoryPort = empleadoRepositoryPort;
        this.mapper = mapper;
    }

    @Override
    public List<BeneficioDto> listarPorEmpleado(Long empleadoId) {
        if (!empleadoRepositoryPort.existsById(empleadoId)) {
            throw new BeneficioNotValidException(List.of("El empleado no existe"));
        }

        return beneficioRepositoryPort.findByEmpleadoId(empleadoId).stream()
                .map(mapper::toDto)
                .toList();
    }
}
