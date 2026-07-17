package com.atlashr.atlas_hr.application.ports.in;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;

import java.util.List;

public interface ListarBeneficiosUseCase {
    List<BeneficioDto> listarPorEmpleado(Long empleadoId);
}
