package com.atlashr.atlas_hr.application.ports.in;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.CrearBeneficioDto;

public interface CrearBeneficioUseCase {
    BeneficioDto crear(CrearBeneficioDto dto);
}
