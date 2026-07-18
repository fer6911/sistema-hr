package com.atlashr.atlas_hr.application.ports.in;

import com.atlashr.atlas_hr.application.dto.BeneficiosConUbicacionDto;

public interface ListarBeneficiosConUbicacionUseCase {
    BeneficiosConUbicacionDto listarConUbicacion(Long empleadoId);
}
