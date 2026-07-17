package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarBeneficiosServiceTest {

    @Mock
    private BeneficioRepositoryPort beneficioRepositoryPort;

    @Mock
    private EmpleadoRepositoryPort empleadoRepositoryPort;

    @Mock
    private BeneficioApplicationMapper mapper;

    private ListarBeneficiosService listarBeneficiosService;

    @BeforeEach
    void setUp() {
        listarBeneficiosService = new ListarBeneficiosService(beneficioRepositoryPort, empleadoRepositoryPort, mapper);
    }

    @Test
    void listarPorEmpleadoRetornaLista() {
        when(empleadoRepositoryPort.existsById(1L)).thenReturn(true);
        Beneficio b1 = Beneficio.builder().id(1L).empleadoId(1L).nombreBeneficio("Seguro").monto(new BigDecimal("500")).build();
        Beneficio b2 = Beneficio.builder().id(2L).empleadoId(1L).nombreBeneficio("Bonificación").monto(new BigDecimal("1000")).build();
        when(beneficioRepositoryPort.findByEmpleadoId(1L)).thenReturn(List.of(b1, b2));
        when(mapper.toDto(b1)).thenReturn(new BeneficioDto(1L, 1L, "Seguro", new BigDecimal("500")));
        when(mapper.toDto(b2)).thenReturn(new BeneficioDto(2L, 1L, "Bonificación", new BigDecimal("1000")));

        List<BeneficioDto> resultado = listarBeneficiosService.listarPorEmpleado(1L);

        assertEquals(2, resultado.size());
        assertEquals("Seguro", resultado.get(0).nombreBeneficio());
        assertEquals("Bonificación", resultado.get(1).nombreBeneficio());
    }

    @Test
    void listarPorEmpleadoSinBeneficiosRetornaListaVacia() {
        when(empleadoRepositoryPort.existsById(1L)).thenReturn(true);
        when(beneficioRepositoryPort.findByEmpleadoId(1L)).thenReturn(List.of());

        List<BeneficioDto> resultado = listarBeneficiosService.listarPorEmpleado(1L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void empleadoNoExisteLanzaExcepcion() {
        when(empleadoRepositoryPort.existsById(999L)).thenReturn(false);

        BeneficioNotValidException ex = assertThrows(BeneficioNotValidException.class, () ->
                listarBeneficiosService.listarPorEmpleado(999L)
        );
        assertTrue(ex.getErrors().contains("El empleado no existe"));
    }

    @Test
    void empleadoNoExisteNoLlamaRepositorioBeneficios() {
        when(empleadoRepositoryPort.existsById(999L)).thenReturn(false);

        assertThrows(BeneficioNotValidException.class, () ->
                listarBeneficiosService.listarPorEmpleado(999L)
        );

        verify(beneficioRepositoryPort, never()).findByEmpleadoId(any());
    }

    @Test
    void listarPorEmpleadoLlamaAlRepositorio() {
        when(empleadoRepositoryPort.existsById(1L)).thenReturn(true);
        when(beneficioRepositoryPort.findByEmpleadoId(1L)).thenReturn(List.of());

        listarBeneficiosService.listarPorEmpleado(1L);

        verify(beneficioRepositoryPort).findByEmpleadoId(1L);
    }
}
