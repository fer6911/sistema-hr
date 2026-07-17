package com.atlashr.atlas_hr.domain.exception;

import java.util.List;

public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException() {
        super("Credenciales inválidas");
    }

    public List<String> getErrors() {
        return List.of(getMessage());
    }
}
