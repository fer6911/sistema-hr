package com.atlashr.atlas_hr.domain.model;

import com.atlashr.atlas_hr.domain.exception.BeneficioNotValidException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BeneficioTest {

    private static final Long VALID_EMPLEADO_ID = 1L;
    private static final String VALID_NOMBRE = "Seguro Médico";
    private static final BigDecimal VALID_MONTO = new BigDecimal("500.00");

    @Test
    void creacionExitosaConDatosValidos() {
        Beneficio beneficio = Beneficio.builder()
                .empleadoId(VALID_EMPLEADO_ID)
                .nombreBeneficio(VALID_NOMBRE)
                .monto(VALID_MONTO)
                .build();

        assertEquals(VALID_EMPLEADO_ID, beneficio.getEmpleadoId());
        assertEquals(VALID_NOMBRE, beneficio.getNombreBeneficio());
        assertEquals(VALID_MONTO, beneficio.getMonto());
    }

    @Test
    void empleadoIdNuloLanzaExcepcion() {
        BeneficioNotValidException ex = assertThrows(BeneficioNotValidException.class, () ->
                Beneficio.builder()
                        .empleadoId(null)
                        .nombreBeneficio(VALID_NOMBRE)
                        .monto(VALID_MONTO)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El ID del empleado no puede estar vacío"));
    }

    @Test
    void nombreBeneficioVacioLanzaExcepcion() {
        BeneficioNotValidException ex = assertThrows(BeneficioNotValidException.class, () ->
                Beneficio.builder()
                        .empleadoId(VALID_EMPLEADO_ID)
                        .nombreBeneficio("")
                        .monto(VALID_MONTO)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El nombre del beneficio no puede estar vacío"));
    }

    @Test
    void nombreBeneficioNuloLanzaExcepcion() {
        BeneficioNotValidException ex = assertThrows(BeneficioNotValidException.class, () ->
                Beneficio.builder()
                        .empleadoId(VALID_EMPLEADO_ID)
                        .nombreBeneficio(null)
                        .monto(VALID_MONTO)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El nombre del beneficio no puede estar vacío"));
    }

    @Test
    void montoNuloLanzaExcepcion() {
        BeneficioNotValidException ex = assertThrows(BeneficioNotValidException.class, () ->
                Beneficio.builder()
                        .empleadoId(VALID_EMPLEADO_ID)
                        .nombreBeneficio(VALID_NOMBRE)
                        .monto(null)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El monto no puede estar vacío"));
    }

    @Test
    void montoCeroLanzaExcepcion() {
        BeneficioNotValidException ex = assertThrows(BeneficioNotValidException.class, () ->
                Beneficio.builder()
                        .empleadoId(VALID_EMPLEADO_ID)
                        .nombreBeneficio(VALID_NOMBRE)
                        .monto(BigDecimal.ZERO)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El monto debe ser mayor a 0"));
    }

    @Test
    void montoNegativoLanzaExcepcion() {
        BeneficioNotValidException ex = assertThrows(BeneficioNotValidException.class, () ->
                Beneficio.builder()
                        .empleadoId(VALID_EMPLEADO_ID)
                        .nombreBeneficio(VALID_NOMBRE)
                        .monto(new BigDecimal("-100"))
                        .build()
        );
        assertTrue(ex.getErrors().contains("El monto debe ser mayor a 0"));
    }

    @Test
    void multiplesErroresSeAcumulan() {
        BeneficioNotValidException ex = assertThrows(BeneficioNotValidException.class, () ->
                Beneficio.builder()
                        .empleadoId(null)
                        .nombreBeneficio("")
                        .monto(null)
                        .build()
        );
        assertEquals(3, ex.getErrors().size());
    }

    @Test
    void idSeGuarda() {
        Beneficio beneficio = Beneficio.builder()
                .id(10L)
                .empleadoId(VALID_EMPLEADO_ID)
                .nombreBeneficio(VALID_NOMBRE)
                .monto(VALID_MONTO)
                .build();

        assertEquals(10L, beneficio.getId());
    }

    @Test
    void montoPositivoAceptado() {
        Beneficio beneficio = Beneficio.builder()
                .empleadoId(VALID_EMPLEADO_ID)
                .nombreBeneficio(VALID_NOMBRE)
                .monto(new BigDecimal("0.01"))
                .build();

        assertEquals(new BigDecimal("0.01"), beneficio.getMonto());
    }
}
