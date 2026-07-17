package com.atlashr.atlas_hr.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearUsuarioDto(
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(max = 30, message = "El nombre de usuario es demasiado largo")
    String username,

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no tiene un formato válido")
    String email,

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String password
) {}
