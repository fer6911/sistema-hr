package com.atlashr.atlas_hr.application.ports.in;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.EditarBeneficioDto;

public interface EditarBeneficioUseCase {
    BeneficioDto editar(Long id, EditarBeneficioDto dto);
}
