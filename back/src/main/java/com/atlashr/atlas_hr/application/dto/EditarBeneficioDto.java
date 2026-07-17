package com.atlashr.atlas_hr.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record EditarBeneficioDto(
    @NotBlank(message = "El nombre del beneficio no puede estar vacío")
    String nombreBeneficio,

    @NotNull(message = "El monto no puede estar vacío")
    @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor a 0")
    BigDecimal monto
) {}
