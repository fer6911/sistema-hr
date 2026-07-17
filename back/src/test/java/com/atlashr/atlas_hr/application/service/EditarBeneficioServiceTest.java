package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.EditarBeneficioDto;
import com.atlashr.atlas_hr.application.mapper.BeneficioApplicationMapper;
import com.atlashr.atlas_hr.application.ports.out.BeneficioRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.BeneficioNotValidException;
import com.atlashr.atlas_hr.domain.model.Beneficio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditarBeneficioServiceTest {

    @Mock
    private BeneficioRepositoryPort beneficioRepositoryPort;

    @Mock
    private BeneficioApplicationMapper mapper;

    private EditarBeneficioService editarBeneficioService;

    private static final Long ID = 1L;
    private static final Long EMPLEADO_ID = 1L;
    private static final String NOMBRE_NUEVO = "Seguro Dental";
    private static final BigDecimal MONTO_NUEVO = new BigDecimal("800.00");

    @BeforeEach
    void setUp() {
        editarBeneficioService = new EditarBeneficioService(beneficioRepositoryPort, mapper);
    }

    @Test
    void editarBeneficioExitoso() {
        Beneficio existente = Beneficio.builder().id(ID).empleadoId(EMPLEADO_ID).nombreBeneficio("Seguro Médico").monto(new BigDecimal("500")).build();
        when(beneficioRepositoryPort.findById(ID)).thenReturn(Optional.of(existente));
        when(beneficioRepositoryPort.save(any(Beneficio.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any(Beneficio.class))).thenReturn(new BeneficioDto(ID, EMPLEADO_ID, NOMBRE_NUEVO, MONTO_NUEVO));

        EditarBeneficioDto dto = new EditarBeneficioDto(NOMBRE_NUEVO, MONTO_NUEVO);
        BeneficioDto resultado = editarBeneficioService.editar(ID, dto);

        assertEquals(NOMBRE_NUEVO, resultado.nombreBeneficio());
        assertEquals(MONTO_NUEVO, resultado.monto());
        verify(mapper).actualizar(eq(existente), eq(dto));
    }

    @Test
    void beneficioNoExisteLanzaExcepcion() {
        when(beneficioRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        EditarBeneficioDto dto = new EditarBeneficioDto(NOMBRE_NUEVO, MONTO_NUEVO);
        BeneficioNotValidException ex = assertThrows(BeneficioNotValidException.class, () ->
                editarBeneficioService.editar(999L, dto)
        );
        assertTrue(ex.getErrors().contains("El beneficio no existe"));
    }

    @Test
    void beneficioNoExisteNoLlamaSave() {
        when(beneficioRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        EditarBeneficioDto dto = new EditarBeneficioDto(NOMBRE_NUEVO, MONTO_NUEVO);
        assertThrows(BeneficioNotValidException.class, () ->
                editarBeneficioService.editar(999L, dto)
        );

        verify(beneficioRepositoryPort, never()).save(any());
    }

    @Test
    void seGuardaEnRepositorio() {
        Beneficio existente = Beneficio.builder().id(ID).empleadoId(EMPLEADO_ID).nombreBeneficio("Seguro Médico").monto(new BigDecimal("500")).build();
        when(beneficioRepositoryPort.findById(ID)).thenReturn(Optional.of(existente));
        when(beneficioRepositoryPort.save(any(Beneficio.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any(Beneficio.class))).thenReturn(new BeneficioDto(ID, EMPLEADO_ID, NOMBRE_NUEVO, MONTO_NUEVO));

        EditarBeneficioDto dto = new EditarBeneficioDto(NOMBRE_NUEVO, MONTO_NUEVO);
        editarBeneficioService.editar(ID, dto);

        verify(beneficioRepositoryPort).save(any(Beneficio.class));
    }
}
