package com.atlashr.atlas_hr.application.dto;

import java.util.List;

public record BeneficiosConUbicacionDto(
        List<BeneficioDto> beneficios,
        UbicacionDto ubicacion
) {}
