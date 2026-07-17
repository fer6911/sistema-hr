package com.atlashr.atlas_hr.application.dto;

import java.math.BigDecimal;

public record BeneficioDto(
    Long id,
    Long empleadoId,
    String nombreBeneficio,
    BigDecimal monto
) {}
