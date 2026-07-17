package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.EditarBeneficioDto;
import com.atlashr.atlas_hr.application.ports.out.BeneficioRepositoryPort;
import com.atlashr.atlas_hr.domain.exception.BeneficioNotValidException;
import com.atlashr.atlas_hr.domain.model.Beneficio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

    private EditarBeneficioService editarBeneficioService;

    private static final Long ID = 1L;
    private static final Long EMPLEADO_ID = 1L;
    private static final String NOMBRE_ACTUAL = "Seguro Médico";
    private static final BigDecimal MONTO_ACTUAL = new BigDecimal("500.00");
    private static final String NOMBRE_NUEVO = "Seguro Dental";
    private static final BigDecimal MONTO_NUEVO = new BigDecimal("800.00");

    @BeforeEach
    void setUp() {
        editarBeneficioService = new EditarBeneficioService(beneficioRepositoryPort);
    }

    private Beneficio beneficioExistente() {
        return Beneficio.builder()
                .id(ID).empleadoId(EMPLEADO_ID).nombreBeneficio(NOMBRE_ACTUAL).monto(MONTO_ACTUAL).build();
    }

    @Test
    void editarBeneficioExitoso() {
        when(beneficioRepositoryPort.findById(ID)).thenReturn(Optional.of(beneficioExistente()));
        when(beneficioRepositoryPort.save(any(Beneficio.class))).thenAnswer(inv -> inv.getArgument(0));

        EditarBeneficioDto dto = new EditarBeneficioDto(NOMBRE_NUEVO, MONTO_NUEVO);
        BeneficioDto resultado = editarBeneficioService.editar(ID, dto);

        assertEquals(NOMBRE_NUEVO, resultado.nombreBeneficio());
        assertEquals(MONTO_NUEVO, resultado.monto());
        assertEquals(EMPLEADO_ID, resultado.empleadoId());
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
        when(beneficioRepositoryPort.findById(ID)).thenReturn(Optional.of(beneficioExistente()));
        when(beneficioRepositoryPort.save(any(Beneficio.class))).thenAnswer(inv -> inv.getArgument(0));

        EditarBeneficioDto dto = new EditarBeneficioDto(NOMBRE_NUEVO, MONTO_NUEVO);
        editarBeneficioService.editar(ID, dto);

        verify(beneficioRepositoryPort).save(any(Beneficio.class));
    }

    @Test
    void seMantieneElMismoEmpleadoId() {
        when(beneficioRepositoryPort.findById(ID)).thenReturn(Optional.of(beneficioExistente()));
        when(beneficioRepositoryPort.save(any(Beneficio.class))).thenAnswer(inv -> inv.getArgument(0));

        EditarBeneficioDto dto = new EditarBeneficioDto(NOMBRE_NUEVO, MONTO_NUEVO);
        editarBeneficioService.editar(ID, dto);

        ArgumentCaptor<Beneficio> captor = ArgumentCaptor.forClass(Beneficio.class);
        verify(beneficioRepositoryPort).save(captor.capture());
        assertEquals(EMPLEADO_ID, captor.getValue().getEmpleadoId());
        assertEquals(ID, captor.getValue().getId());
    }
}
