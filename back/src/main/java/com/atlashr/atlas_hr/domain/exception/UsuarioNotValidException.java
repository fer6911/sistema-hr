package com.atlashr.atlas_hr.domain.exception;

import java.util.List;

public class UsuarioNotValidException extends RuntimeException {
    private final List<String> errors;

    public UsuarioNotValidException(List<String> errors) {
        super("Usuario no válido");
        this.errors = errors;
    }

    public List<String> getErrors() { return errors; }
}
