package com.atlashr.atlas_hr.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmpleadoDto(
    Long id,
    String nombre,
    String apellido,
    String email,
    String cargo,
    BigDecimal salario,
    LocalDate fechaIngreso,
    String ciudad,
    boolean activo
) {}
