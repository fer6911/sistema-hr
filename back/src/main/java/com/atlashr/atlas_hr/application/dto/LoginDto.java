package com.atlashr.atlas_hr.application.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    String username,

    @NotBlank(message = "La contraseña no puede estar vacía")
    String password
) {}
