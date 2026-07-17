package com.atlashr.atlas_hr.infrastructure.config;

import com.atlashr.atlas_hr.domain.exception.BeneficioNotValidException;
import com.atlashr.atlas_hr.domain.exception.CredencialesInvalidasException;
import com.atlashr.atlas_hr.domain.exception.EmpleadoNotValidException;
import com.atlashr.atlas_hr.domain.exception.UsuarioNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(EmpleadoNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmpleadoNotValid(EmpleadoNotValidException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error de validación", ex.getErrors()));
    }

    @ExceptionHandler(BeneficioNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleBeneficioNotValid(BeneficioNotValidException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error de validación", ex.getErrors()));
    }

    @ExceptionHandler(UsuarioNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsuarioNotValid(UsuarioNotValidException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error de validación", ex.getErrors()));
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ApiResponse<Void>> handleCredencialesInvalidas(CredencialesInvalidasException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(ex.getMessage(), ex.getErrors()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Error de validación", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error("Error interno del servidor", List.of(ex.getMessage())));
    }
}
