package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.ports.in.EliminarBeneficioUseCase;
import com.atlashr.atlas_hr.application.ports.out.BeneficioRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.BeneficioNotValidException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EliminarBeneficioService implements EliminarBeneficioUseCase {

    private final BeneficioRepositoryPort beneficioRepositoryPort;

    public EliminarBeneficioService(BeneficioRepositoryPort beneficioRepositoryPort) {
        this.beneficioRepositoryPort = beneficioRepositoryPort;
    }

    @Override
    public void eliminar(Long id) {
        if (beneficioRepositoryPort.findById(id).isEmpty()) {
            throw new BeneficioNotValidException(List.of("El beneficio no existe"));
        }

        beneficioRepositoryPort.deleteById(id);
    }
}
