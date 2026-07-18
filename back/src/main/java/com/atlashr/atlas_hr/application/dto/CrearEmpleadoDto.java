package com.atlashr.atlas_hr.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CrearEmpleadoDto(
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre es demasiado largo")
    String nombre,

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 50, message = "El apellido es demasiado largo")
    String apellido,

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no tiene un formato válido")
    String email,

    @NotBlank(message = "El cargo no puede estar vacío")
    String cargo,

    @NotNull(message = "El salario no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El salario no puede ser negativo")
    BigDecimal salario,

    @NotNull(message = "La fecha de ingreso no puede estar vacía")
    LocalDate fechaIngreso,

    @NotBlank(message = "La ciudad no puede estar vacía")
    String ciudad
) {}
