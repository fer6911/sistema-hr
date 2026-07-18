package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.CrearEmpleadoDto;
import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.mapper.EmpleadoApplicationMapper;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.EmpleadoNotValidException;
import com.atlashr.atlas_hr.domain.model.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Mock
    private EmpleadoApplicationMapper mapper;

    private CrearEmpleadoService crearEmpleadoService;

    private static final String NOMBRE = "Juan";
    private static final String APELLIDO = "Pérez";
    private static final String EMAIL = "juan@example.com";
    private static final String CARGO = "Desarrollador";
    private static final BigDecimal SALARIO = new BigDecimal("50000.00");
    private static final LocalDate FECHA = LocalDate.of(2026, 1, 15);
    private static final String CIUDAD = "Bogotá";

    @BeforeEach
    void setUp() {
        crearEmpleadoService = new CrearEmpleadoService(empleadoRepositoryPort, mapper);
    }

    @Test
    void crearEmpleadoExitoso() {
        when(empleadoRepositoryPort.existsByEmail(EMAIL)).thenReturn(false);
        when(mapper.toDomain(any(CrearEmpleadoDto.class))).thenReturn(
                Empleado.builder().nombre(NOMBRE).apellido(APELLIDO).email(EMAIL)
                        .cargo(CARGO).salario(SALARIO).fechaIngreso(FECHA).ciudad(CIUDAD).build()
        );
        when(empleadoRepositoryPort.save(any(Empleado.class))).thenAnswer(inv -> {
            Empleado e = inv.getArgument(0);
            return Empleado.builder().id(1L).nombre(e.getNombre()).apellido(e.getApellido())
                    .email(e.getEmail()).cargo(e.getCargo()).salario(e.getSalario())
                    .fechaIngreso(e.getFechaIngreso()).ciudad(e.getCiudad()).build();
        });
        when(mapper.toDto(any(Empleado.class))).thenReturn(
                new EmpleadoDto(1L, NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA, CIUDAD, true)
        );

        CrearEmpleadoDto dto = new CrearEmpleadoDto(NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA, CIUDAD);
        EmpleadoDto resultado = crearEmpleadoService.crear(dto);

        assertEquals(NOMBRE, resultado.nombre());
        assertEquals(EMAIL, resultado.email());
    }

    @Test
    void emailDuplicadoLanzaExcepcion() {
        when(empleadoRepositoryPort.existsByEmail(EMAIL)).thenReturn(true);

        CrearEmpleadoDto dto = new CrearEmpleadoDto(NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA, CIUDAD);
        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                crearEmpleadoService.crear(dto)
        );
        assertTrue(ex.getErrors().contains("El email ya está registrado"));
    }

    @Test
    void emailDuplicadoNoLlamaSave() {
        when(empleadoRepositoryPort.existsByEmail(EMAIL)).thenReturn(true);

        CrearEmpleadoDto dto = new CrearEmpleadoDto(NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA, CIUDAD);
        assertThrows(EmpleadoNotValidException.class, () ->
                crearEmpleadoService.crear(dto)
        );

        verify(empleadoRepositoryPort, never()).save(any());
    }

    @Test
    void seGuardaEnRepositorio() {
        when(empleadoRepositoryPort.existsByEmail(EMAIL)).thenReturn(false);
        when(mapper.toDomain(any(CrearEmpleadoDto.class))).thenReturn(
                Empleado.builder().nombre(NOMBRE).apellido(APELLIDO).email(EMAIL)
                        .cargo(CARGO).salario(SALARIO).fechaIngreso(FECHA).ciudad(CIUDAD).build()
        );
        when(empleadoRepositoryPort.save(any(Empleado.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any(Empleado.class))).thenReturn(
                new EmpleadoDto(1L, NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA, CIUDAD, true)
        );

        CrearEmpleadoDto dto = new CrearEmpleadoDto(NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA, CIUDAD);
        crearEmpleadoService.crear(dto);

        verify(empleadoRepositoryPort).save(any(Empleado.class));
    }

    @Test
    void seVerificaExistenciaEmail() {
        when(empleadoRepositoryPort.existsByEmail(EMAIL)).thenReturn(false);
        when(mapper.toDomain(any(CrearEmpleadoDto.class))).thenReturn(
                Empleado.builder().nombre(NOMBRE).apellido(APELLIDO).email(EMAIL)
                        .cargo(CARGO).salario(SALARIO).fechaIngreso(FECHA).ciudad(CIUDAD).build()
        );
        when(empleadoRepositoryPort.save(any(Empleado.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any(Empleado.class))).thenReturn(
                new EmpleadoDto(1L, NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA, CIUDAD, true)
        );

        CrearEmpleadoDto dto = new CrearEmpleadoDto(NOMBRE, APELLIDO, EMAIL, CARGO, SALARIO, FECHA, CIUDAD);
        crearEmpleadoService.crear(dto);

        verify(empleadoRepositoryPort).existsByEmail(EMAIL);
    }
}
