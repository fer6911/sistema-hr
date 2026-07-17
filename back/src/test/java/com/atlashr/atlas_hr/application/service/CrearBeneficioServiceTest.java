package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.CrearBeneficioDto;
import com.atlashr.atlas_hr.application.mapper.BeneficioApplicationMapper;
import com.atlashr.atlas_hr.application.ports.out.BeneficioRepositoryPort;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.BeneficioNotValidException;
import com.atlashr.atlas_hr.domain.model.Beneficio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearBeneficioServiceTest {

    @Mock
    private BeneficioRepositoryPort beneficioRepositoryPort;

    @Mock
    private EmpleadoRepositoryPort empleadoRepositoryPort;

    @Mock
    private BeneficioApplicationMapper mapper;

    private CrearBeneficioService crearBeneficioService;

    private static final Long EMPLEADO_ID = 1L;
    private static final String NOMBRE = "Seguro Médico";
    private static final BigDecimal MONTO = new BigDecimal("500.00");

    @BeforeEach
    void setUp() {
        crearBeneficioService = new CrearBeneficioService(beneficioRepositoryPort, empleadoRepositoryPort, mapper);
    }

    @Test
    void crearBeneficioExitoso() {
        when(empleadoRepositoryPort.existsById(EMPLEADO_ID)).thenReturn(true);
        when(mapper.toDomain(any(CrearBeneficioDto.class))).thenReturn(
                Beneficio.builder().empleadoId(EMPLEADO_ID).nombreBeneficio(NOMBRE).monto(MONTO).build()
        );
        when(beneficioRepositoryPort.save(any(Beneficio.class))).thenAnswer(inv -> {
            Beneficio b = inv.getArgument(0);
            return Beneficio.builder().id(1L).empleadoId(b.getEmpleadoId()).nombreBeneficio(b.getNombreBeneficio()).monto(b.getMonto()).build();
        });
        when(mapper.toDto(any(Beneficio.class))).thenReturn(new BeneficioDto(1L, EMPLEADO_ID, NOMBRE, MONTO));

        CrearBeneficioDto dto = new CrearBeneficioDto(EMPLEADO_ID, NOMBRE, MONTO);
        BeneficioDto resultado = crearBeneficioService.crear(dto);

        assertEquals(1L, resultado.id());
        assertEquals(NOMBRE, resultado.nombreBeneficio());
    }

    @Test
    void empleadoNoExisteLanzaExcepcion() {
        when(empleadoRepositoryPort.existsById(EMPLEADO_ID)).thenReturn(false);

        CrearBeneficioDto dto = new CrearBeneficioDto(EMPLEADO_ID, NOMBRE, MONTO);
        BeneficioNotValidException ex = assertThrows(BeneficioNotValidException.class, () ->
                crearBeneficioService.crear(dto)
        );
        assertTrue(ex.getErrors().contains("El empleado no existe"));
    }

    @Test
    void empleadoNoExisteNoLlamaSave() {
        when(empleadoRepositoryPort.existsById(EMPLEADO_ID)).thenReturn(false);

        CrearBeneficioDto dto = new CrearBeneficioDto(EMPLEADO_ID, NOMBRE, MONTO);
        assertThrows(BeneficioNotValidException.class, () ->
                crearBeneficioService.crear(dto)
        );

        verify(beneficioRepositoryPort, never()).save(any());
    }

    @Test
    void seGuardaEnRepositorio() {
        when(empleadoRepositoryPort.existsById(EMPLEADO_ID)).thenReturn(true);
        when(mapper.toDomain(any(CrearBeneficioDto.class))).thenReturn(
                Beneficio.builder().empleadoId(EMPLEADO_ID).nombreBeneficio(NOMBRE).monto(MONTO).build()
        );
        when(beneficioRepositoryPort.save(any(Beneficio.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any(Beneficio.class))).thenReturn(new BeneficioDto(1L, EMPLEADO_ID, NOMBRE, MONTO));

        CrearBeneficioDto dto = new CrearBeneficioDto(EMPLEADO_ID, NOMBRE, MONTO);
        crearBeneficioService.crear(dto);

        verify(beneficioRepositoryPort).save(any(Beneficio.class));
    }

    @Test
    void seVerificaExistenciaEmpleado() {
        when(empleadoRepositoryPort.existsById(EMPLEADO_ID)).thenReturn(true);
        when(mapper.toDomain(any(CrearBeneficioDto.class))).thenReturn(
                Beneficio.builder().empleadoId(EMPLEADO_ID).nombreBeneficio(NOMBRE).monto(MONTO).build()
        );
        when(beneficioRepositoryPort.save(any(Beneficio.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any(Beneficio.class))).thenReturn(new BeneficioDto(1L, EMPLEADO_ID, NOMBRE, MONTO));

        CrearBeneficioDto dto = new CrearBeneficioDto(EMPLEADO_ID, NOMBRE, MONTO);
        crearBeneficioService.crear(dto);

        verify(empleadoRepositoryPort).existsById(EMPLEADO_ID);
    }
}
