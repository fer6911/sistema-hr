package com.atlashr.atlas_hr.application.service;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EliminarBeneficioServiceTest {

    @Mock
    private BeneficioRepositoryPort beneficioRepositoryPort;

    private EliminarBeneficioService eliminarBeneficioService;

    @BeforeEach
    void setUp() {
        eliminarBeneficioService = new EliminarBeneficioService(beneficioRepositoryPort);
    }

    @Test
    void eliminarBeneficioExitoso() {
        Beneficio beneficio = Beneficio.builder()
                .id(1L).empleadoId(1L).nombreBeneficio("Seguro").monto(new BigDecimal("500")).build();

        when(beneficioRepositoryPort.findById(1L)).thenReturn(Optional.of(beneficio));

        assertDoesNotThrow(() -> eliminarBeneficioService.eliminar(1L));
        verify(beneficioRepositoryPort).deleteById(1L);
    }

    @Test
    void beneficioNoExisteLanzaExcepcion() {
        when(beneficioRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        BeneficioNotValidException ex = assertThrows(BeneficioNotValidException.class, () ->
                eliminarBeneficioService.eliminar(999L)
        );
        assertTrue(ex.getErrors().contains("El beneficio no existe"));
    }

    @Test
    void beneficioNoExisteNoLlamaDelete() {
        when(beneficioRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BeneficioNotValidException.class, () ->
                eliminarBeneficioService.eliminar(999L)
        );

        verify(beneficioRepositoryPort, never()).deleteById(any());
    }

    @Test
    void seLlamaDeleteByIdConIdCorrecto() {
        Beneficio beneficio = Beneficio.builder()
                .id(5L).empleadoId(1L).nombreBeneficio("Seguro").monto(new BigDecimal("500")).build();

        when(beneficioRepositoryPort.findById(5L)).thenReturn(Optional.of(beneficio));

        eliminarBeneficioService.eliminar(5L);

        verify(beneficioRepositoryPort).deleteById(5L);
    }
}
