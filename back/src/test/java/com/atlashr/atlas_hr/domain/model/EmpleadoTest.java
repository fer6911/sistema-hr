package com.atlashr.atlas_hr.domain.model;

import com.atlashr.atlas_hr.domain.exception.EmpleadoNotValidException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmpleadoTest {

    private static final String VALID_NOMBRE = "Juan";
    private static final String VALID_APELLIDO = "Pérez";
    private static final String VALID_EMAIL = "juan@example.com";
    private static final String VALID_CARGO = "Desarrollador";
    private static final BigDecimal VALID_SALARIO = new BigDecimal("50000.00");
    private static final LocalDate VALID_FECHA = LocalDate.of(2026, 1, 15);
    private static final String VALID_CIUDAD = "Bogotá";

    @Test
    void creacionExitosaConDatosValidos() {
        Empleado empleado = Empleado.builder()
                .nombre(VALID_NOMBRE)
                .apellido(VALID_APELLIDO)
                .email(VALID_EMAIL)
                .cargo(VALID_CARGO)
                .salario(VALID_SALARIO)
                .fechaIngreso(VALID_FECHA)
                .ciudad(VALID_CIUDAD)
                .build();

        assertEquals(VALID_NOMBRE, empleado.getNombre());
        assertEquals(VALID_APELLIDO, empleado.getApellido());
        assertEquals(VALID_EMAIL, empleado.getEmail());
        assertEquals(VALID_CARGO, empleado.getCargo());
        assertEquals(VALID_SALARIO, empleado.getSalario());
        assertEquals(VALID_FECHA, empleado.getFechaIngreso());
        assertEquals(VALID_CIUDAD, empleado.getCiudad());
        assertTrue(empleado.isActivo());
    }

    @Test
    void nombreVacioLanzaExcepcion() {
        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                Empleado.builder()
                        .nombre("")
                        .apellido(VALID_APELLIDO)
                        .email(VALID_EMAIL)
                        .cargo(VALID_CARGO)
                        .salario(VALID_SALARIO)
                        .fechaIngreso(VALID_FECHA)
                        .ciudad(VALID_CIUDAD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El nombre no puede estar vacío"));
    }

    @Test
    void nombreNuloLanzaExcepcion() {
        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                Empleado.builder()
                        .nombre(null)
                        .apellido(VALID_APELLIDO)
                        .email(VALID_EMAIL)
                        .cargo(VALID_CARGO)
                        .salario(VALID_SALARIO)
                        .fechaIngreso(VALID_FECHA)
                        .ciudad(VALID_CIUDAD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El nombre no puede estar vacío"));
    }

    @Test
    void nombreMayorA50CharsLanzaExcepcion() {
        String largo = "a".repeat(51);

        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                Empleado.builder()
                        .nombre(largo)
                        .apellido(VALID_APELLIDO)
                        .email(VALID_EMAIL)
                        .cargo(VALID_CARGO)
                        .salario(VALID_SALARIO)
                        .fechaIngreso(VALID_FECHA)
                        .ciudad(VALID_CIUDAD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El nombre es demasiado largo"));
    }

    @Test
    void apellidoVacioLanzaExcepcion() {
        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                Empleado.builder()
                        .nombre(VALID_NOMBRE)
                        .apellido("")
                        .email(VALID_EMAIL)
                        .cargo(VALID_CARGO)
                        .salario(VALID_SALARIO)
                        .fechaIngreso(VALID_FECHA)
                        .ciudad(VALID_CIUDAD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El apellido no puede estar vacío"));
    }

    @Test
    void apellidoMayorA50CharsLanzaExcepcion() {
        String largo = "a".repeat(51);

        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                Empleado.builder()
                        .nombre(VALID_NOMBRE)
                        .apellido(largo)
                        .email(VALID_EMAIL)
                        .cargo(VALID_CARGO)
                        .salario(VALID_SALARIO)
                        .fechaIngreso(VALID_FECHA)
                        .ciudad(VALID_CIUDAD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El apellido es demasiado largo"));
    }

    @Test
    void emailVacioLanzaExcepcion() {
        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                Empleado.builder()
                        .nombre(VALID_NOMBRE)
                        .apellido(VALID_APELLIDO)
                        .email("")
                        .cargo(VALID_CARGO)
                        .salario(VALID_SALARIO)
                        .fechaIngreso(VALID_FECHA)
                        .ciudad(VALID_CIUDAD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El email no puede estar vacío"));
    }

    @Test
    void emailFormatoInvalidoLanzaExcepcion() {
        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                Empleado.builder()
                        .nombre(VALID_NOMBRE)
                        .apellido(VALID_APELLIDO)
                        .email("noemail")
                        .cargo(VALID_CARGO)
                        .salario(VALID_SALARIO)
                        .fechaIngreso(VALID_FECHA)
                        .ciudad(VALID_CIUDAD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El email no tiene un formato válido"));
    }

    @Test
    void cargoVacioLanzaExcepcion() {
        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                Empleado.builder()
                        .nombre(VALID_NOMBRE)
                        .apellido(VALID_APELLIDO)
                        .email(VALID_EMAIL)
                        .cargo("")
                        .salario(VALID_SALARIO)
                        .fechaIngreso(VALID_FECHA)
                        .ciudad(VALID_CIUDAD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El cargo no puede estar vacío"));
    }

    @Test
    void salarioNuloLanzaExcepcion() {
        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                Empleado.builder()
                        .nombre(VALID_NOMBRE)
                        .apellido(VALID_APELLIDO)
                        .email(VALID_EMAIL)
                        .cargo(VALID_CARGO)
                        .salario(null)
                        .fechaIngreso(VALID_FECHA)
                        .ciudad(VALID_CIUDAD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El salario no puede estar vacío"));
    }

    @Test
    void salarioNegativoLanzaExcepcion() {
        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                Empleado.builder()
                        .nombre(VALID_NOMBRE)
                        .apellido(VALID_APELLIDO)
                        .email(VALID_EMAIL)
                        .cargo(VALID_CARGO)
                        .salario(new BigDecimal("-1000"))
                        .fechaIngreso(VALID_FECHA)
                        .ciudad(VALID_CIUDAD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("El salario no puede ser negativo"));
    }

    @Test
    void fechaIngresoNulaLanzaExcepcion() {
        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                Empleado.builder()
                        .nombre(VALID_NOMBRE)
                        .apellido(VALID_APELLIDO)
                        .email(VALID_EMAIL)
                        .cargo(VALID_CARGO)
                        .salario(VALID_SALARIO)
                        .fechaIngreso(null)
                        .ciudad(VALID_CIUDAD)
                        .build()
        );
        assertTrue(ex.getErrors().contains("La fecha de ingreso no puede estar vacía"));
    }

    @Test
    void multiplesErroresSeAcumulan() {
        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                Empleado.builder()
                        .nombre("")
                        .apellido("")
                        .email("invalido")
                        .cargo("")
                        .salario(null)
                        .fechaIngreso(null)
                        .build()
        );
        assertEquals(6, ex.getErrors().size());
    }

    @Test
    void activoPorDefectoEsTrue() {
        Empleado empleado = Empleado.builder()
                .nombre(VALID_NOMBRE)
                .apellido(VALID_APELLIDO)
                .email(VALID_EMAIL)
                .cargo(VALID_CARGO)
                .salario(VALID_SALARIO)
                .fechaIngreso(VALID_FECHA)
                .ciudad(VALID_CIUDAD)
                .build();

        assertTrue(empleado.isActivo());
    }

    @Test
    void activoFalseSeGuarda() {
        Empleado empleado = Empleado.builder()
                .nombre(VALID_NOMBRE)
                .apellido(VALID_APELLIDO)
                .email(VALID_EMAIL)
                .cargo(VALID_CARGO)
                .salario(VALID_SALARIO)
                .fechaIngreso(VALID_FECHA)
                .ciudad(VALID_CIUDAD)
                .activo(false)
                .build();

        assertFalse(empleado.isActivo());
    }

    @Test
    void idSeGuarda() {
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre(VALID_NOMBRE)
                .apellido(VALID_APELLIDO)
                .email(VALID_EMAIL)
                .cargo(VALID_CARGO)
                .salario(VALID_SALARIO)
                .fechaIngreso(VALID_FECHA)
                .ciudad(VALID_CIUDAD)
                .build();

        assertEquals(1L, empleado.getId());
    }
}
