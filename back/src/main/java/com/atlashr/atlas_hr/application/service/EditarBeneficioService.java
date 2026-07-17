package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.EditarBeneficioDto;
import com.atlashr.atlas_hr.application.ports.in.EditarBeneficioUseCase;
import com.atlashr.atlas_hr.application.ports.out.BeneficioRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.BeneficioNotValidException;
import com.atlashr.atlas_hr.domain.model.Beneficio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditarBeneficioService implements EditarBeneficioUseCase {

    private final BeneficioRepositoryPort beneficioRepositoryPort;

    public EditarBeneficioService(BeneficioRepositoryPort beneficioRepositoryPort) {
        this.beneficioRepositoryPort = beneficioRepositoryPort;
    }

    @Override
    public BeneficioDto editar(Long id, EditarBeneficioDto dto) {
        Beneficio existente = beneficioRepositoryPort.findById(id)
                .orElseThrow(() -> new BeneficioNotValidException(List.of("El beneficio no existe")));

        Beneficio actualizado = Beneficio.builder()
                .id(existente.getId())
                .empleadoId(existente.getEmpleadoId())
                .nombreBeneficio(dto.nombreBeneficio())
                .monto(dto.monto())
                .build();

        Beneficio guardado = beneficioRepositoryPort.save(actualizado);

        return new BeneficioDto(
                guardado.getId(),
                guardado.getEmpleadoId(),
                guardado.getNombreBeneficio(),
                guardado.getMonto()
        );
    }
}
