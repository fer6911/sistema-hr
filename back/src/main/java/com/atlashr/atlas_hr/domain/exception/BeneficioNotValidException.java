package com.atlashr.atlas_hr.domain.exception;

import java.util.List;

public class BeneficioNotValidException extends RuntimeException {

    private final List<String> errors;

    public BeneficioNotValidException(List<String> errors) {
        super("Beneficio no válido");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
