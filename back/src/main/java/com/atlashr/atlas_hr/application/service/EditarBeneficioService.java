package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.EditarBeneficioDto;
import com.atlashr.atlas_hr.application.mapper.BeneficioApplicationMapper;
import com.atlashr.atlas_hr.application.ports.in.EditarBeneficioUseCase;
import com.atlashr.atlas_hr.application.ports.out.BeneficioRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.BeneficioNotValidException;
import com.atlashr.atlas_hr.domain.model.Beneficio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditarBeneficioService implements EditarBeneficioUseCase {

    private final BeneficioRepositoryPort beneficioRepositoryPort;
    private final BeneficioApplicationMapper mapper;

    public EditarBeneficioService(BeneficioRepositoryPort beneficioRepositoryPort, BeneficioApplicationMapper mapper) {
        this.beneficioRepositoryPort = beneficioRepositoryPort;
        this.mapper = mapper;
    }

    @Override
    public BeneficioDto editar(Long id, EditarBeneficioDto dto) {
        Beneficio existente = beneficioRepositoryPort.findById(id)
                .orElseThrow(() -> new BeneficioNotValidException(List.of("El beneficio no existe")));

        mapper.actualizar(existente, dto);
        Beneficio guardado = beneficioRepositoryPort.save(existente);

        return mapper.toDto(guardado);
    }
}
