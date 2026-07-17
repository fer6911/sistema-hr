package com.atlashr.atlas_hr.domain.exception;

import java.util.List;

public class EmpleadoNotValidException extends RuntimeException {

    private final List<String> errors;

    public EmpleadoNotValidException(List<String> errors) {
        super("Empleado no válido");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
