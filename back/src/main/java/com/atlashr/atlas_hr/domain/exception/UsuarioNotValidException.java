package com.atlashr.atlas_hr.domain.exception;

import java.util.List;

public class UsuarioNotValidException extends RuntimeException {
    private final List<String> errores;

    public UsuarioNotValidException(List<String> errores) {
        super("Usuario no válido: " + String.join(", ", errores));
        this.errores = errores;
    }

    public List<String> getErrores() { return errores; }
}
