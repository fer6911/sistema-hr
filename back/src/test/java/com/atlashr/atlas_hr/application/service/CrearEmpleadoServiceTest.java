package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.CrearEmpleadoDto;
import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.EmpleadoNotValidException;
import com.atlashr.atlas_hr.domain.model.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearEmpleadoServiceTest {

    @Mock
    private EmpleadoRepositoryPort empleadoRepositoryPort;

    private CrearEmpleadoService crearEmpleadoService;

    private static final String NOMBRE = "Juan";
    private static final String APELLIDO = "Pérez";
    private static final String EMAIL = "juan@example.com";
    private static final String CARGO = "Desarrollador";
    private static final BigDecimal SALARIO = new BigDecimal("50000.00");
    private static final LocalDate FECHA = LocalDate.of(2026, 1, 15);

    @BeforeEach
    void setUp() {
        crearEmpleadoService = new CrearEmpleadoService(empleadoRepositoryPort);
    }

    @Test
    void crearEmpleadoExitoso() {
        when(empleadoRepositoryPort.existsByEmail(EMAIL)).thenReturn(false);
        when(empleadoRepositoryPort.save(any(Empleado.class))).thenAnswer(inv -> {
            Empleado e = inv.getArgument(0);
            return Empleado.builder()
                    .id(1L)
                    .nombre(e.getNombre())
                    .apellido(e.getApellido())
                    .email(e.getEmail())
                    .cargo(e.getCargo())
                    .salario(e.getSalario())
                    .fechaIngreso(e.getFechaIngreso())
                    .build();
        });

        CrearEmpleadoDto dto = new CrearEmpleadoDto(NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA);
        EmpleadoDto resultado = crearEmpleadoService.crear(dto);

        assertEquals(NOMBRE, resultado.nombre());
        assertEquals(APELLIDO, resultado.apellido());
        assertEquals(EMAIL, resultado.email());
        assertEquals(CARGO, resultado.cargo());
        assertEquals(SALARIO, resultado.salario());
        assertEquals(FECHA, resultado.fechaIngreso());
        assertTrue(resultado.activo());
    }

    @Test
    void emailDuplicadoLanzaExcepcion() {
        when(empleadoRepositoryPort.existsByEmail(EMAIL)).thenReturn(true);

        CrearEmpleadoDto dto = new CrearEmpleadoDto(NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA);
        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                crearEmpleadoService.crear(dto)
        );
        assertTrue(ex.getErrors().contains("El email ya está registrado"));
    }

    @Test
    void emailDuplicadoNoLlamaSave() {
        when(empleadoRepositoryPort.existsByEmail(EMAIL)).thenReturn(true);

        CrearEmpleadoDto dto = new CrearEmpleadoDto(NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA);
        assertThrows(EmpleadoNotValidException.class, () ->
                crearEmpleadoService.crear(dto)
        );

        verify(empleadoRepositoryPort, never()).save(any());
    }

    @Test
    void seGuardaEnRepositorio() {
        when(empleadoRepositoryPort.existsByEmail(EMAIL)).thenReturn(false);
        when(empleadoRepositoryPort.save(any(Empleado.class))).thenAnswer(inv -> inv.getArgument(0));

        CrearEmpleadoDto dto = new CrearEmpleadoDto(NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA);
        crearEmpleadoService.crear(dto);

        verify(empleadoRepositoryPort).save(any(Empleado.class));
    }

    @Test
    void empleadoGuardadoTieneActivoTrue() {
        when(empleadoRepositoryPort.existsByEmail(EMAIL)).thenReturn(false);
        when(empleadoRepositoryPort.save(any(Empleado.class))).thenAnswer(inv -> inv.getArgument(0));

        CrearEmpleadoDto dto = new CrearEmpleadoDto(NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA);
        crearEmpleadoService.crear(dto);

        ArgumentCaptor<Empleado> captor = ArgumentCaptor.forClass(Empleado.class);
        verify(empleadoRepositoryPort).save(captor.capture());
        assertTrue(captor.getValue().isActivo());
    }

    @Test
    void seVerificaExistenciaEmail() {
        when(empleadoRepositoryPort.existsByEmail(EMAIL)).thenReturn(false);
        when(empleadoRepositoryPort.save(any(Empleado.class))).thenAnswer(inv -> inv.getArgument(0));

        CrearEmpleadoDto dto = new CrearEmpleadoDto(NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA);
        crearEmpleadoService.crear(dto);

        verify(empleadoRepositoryPort).existsByEmail(EMAIL);
    }
}
