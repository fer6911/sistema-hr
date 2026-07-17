package com.atlashr.atlas_hr.domain.model;

import com.atlashr.atlas_hr.domain.exception.UsuarioNotValidException;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private final String username;
    private final String email;
    private final String password;
    private final String rol;
    private final boolean activo;

    private Usuario(Builder builder) {
        List<String> errores = new ArrayList<>();
        validarUsername(builder.username, errores);
        validarEmail(builder.email, errores);
        validarPassword(builder.password, errores);

        if (!errores.isEmpty()) throw new UsuarioNotValidException(errores);

        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.rol = builder.rol == null ? "USER" : builder.rol;
        this.activo = builder.activo == null ? true : builder.activo;
    }

    private void validarUsername(String username, List<String> errores) {
        if (username == null || username.isBlank()) errores.add("El nombre de usuario no puede estar vacío");
        else if (username.length() > 30) errores.add("El nombre de usuario es demasiado largo");
    }

    private void validarEmail(String email, List<String> errores) {
        if (email == null || email.isBlank()) errores.add("El email no puede estar vacío");
        else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) errores.add("El email no tiene un formato válido");
    }

    private void validarPassword(String password, List<String> errores) {
        if (password == null || password.isBlank()) errores.add("La contraseña no puede estar vacía");
        else if (password.length() < 6) errores.add("La contraseña debe tener al menos 6 caracteres");
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }
    public boolean isActivo() { return activo; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String username;
        private String email;
        private String password;
        private String rol;
        private Boolean activo;

        public Builder username(String username) { this.username = username; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder rol(String rol) { this.rol = rol; return this; }
        public Builder activo(Boolean activo) { this.activo = activo; return this; }

        public Usuario build() { return new Usuario(this); }
    }
}
