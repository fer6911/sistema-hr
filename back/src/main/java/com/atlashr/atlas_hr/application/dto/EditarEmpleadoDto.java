package com.atlashr.atlas_hr.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EditarEmpleadoDto(
    @Size(max = 50, message = "El nombre es demasiado largo")
    String nombre,

    @Size(max = 50, message = "El apellido es demasiado largo")
    String apellido,

    @Email(message = "El email no tiene un formato válido")
    String email,

    String cargo,

    @DecimalMin(value = "0.0", inclusive = false, message = "El salario no puede ser negativo")
    BigDecimal salario,

    LocalDate fechaIngreso,

    String ciudad,

    Boolean activo
) {}
