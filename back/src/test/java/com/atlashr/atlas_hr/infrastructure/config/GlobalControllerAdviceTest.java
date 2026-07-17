package com.atlashr.atlas_hr.infrastructure.config;

import com.atlashr.atlas_hr.domain.exception.CredencialesInvalidasException;
import com.atlashr.atlas_hr.domain.exception.UsuarioNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalControllerAdviceTest {

    private GlobalControllerAdvice advice;

    @BeforeEach
    void setUp() {
        advice = new GlobalControllerAdvice();
    }

    @Test
    void handleUsuarioNotValidDevuelve400() {
        UsuarioNotValidException ex = new UsuarioNotValidException(List.of("email invalido", "username vacio"));

        ResponseEntity<ApiResponse<Void>> response = advice.handleUsuarioNotValid(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().isError());
        assertEquals(2, response.getBody().getErrors().size());
        assertTrue(response.getBody().getErrors().contains("email invalido"));
        assertTrue(response.getBody().getErrors().contains("username vacio"));
    }

    @Test
    void handleCredencialesInvalidasDevuelve400() {
        CredencialesInvalidasException ex = new CredencialesInvalidasException();

        ResponseEntity<ApiResponse<Void>> response = advice.handleCredencialesInvalidas(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().isError());
        assertEquals("Credenciales inválidas", response.getBody().getMessage());
        assertTrue(response.getBody().getErrors().contains("Credenciales inválidas"));
    }

    @Test
    void handleValidationErrorsDevuelve400() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("dto", "email", "email no valido");
        FieldError fieldError2 = new FieldError("dto", "username", "username vacio");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiResponse<Void>> response = advice.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().isError());
        assertEquals(2, response.getBody().getErrors().size());
    }

    @Test
    void handleGenericExceptionDevuelve500() {
        Exception ex = new RuntimeException("fallo inesperado");

        ResponseEntity<ApiResponse<Void>> response = advice.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().isError());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertTrue(response.getBody().getErrors().contains("fallo inesperado"));
    }
}
