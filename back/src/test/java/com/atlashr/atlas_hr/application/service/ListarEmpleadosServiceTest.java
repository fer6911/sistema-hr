package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarEmpleadosServiceTest {

    @Mock
    private EmpleadoRepositoryPort empleadoRepositoryPort;

    private ListarEmpleadosService listarEmpleadosService;

    @BeforeEach
    void setUp() {
        listarEmpleadosService = new ListarEmpleadosService(empleadoRepositoryPort);
    }

    @Test
    void listarTodosRetornaLista() {
        Empleado e1 = Empleado.builder()
                .id(1L).nombre("Juan").apellido("Pérez").email("juan@example.com")
                .cargo("Dev").salario(new BigDecimal("50000")).fechaIngreso(LocalDate.of(2026, 1, 1))
                .build();
        Empleado e2 = Empleado.builder()
                .id(2L).nombre("Ana").apellido("García").email("ana@example.com")
                .cargo("PM").salario(new BigDecimal("60000")).fechaIngreso(LocalDate.of(2026, 2, 1))
                .build();

        when(empleadoRepositoryPort.findAll()).thenReturn(List.of(e1, e2));

        List<EmpleadoDto> resultado = listarEmpleadosService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.get(0).nombre());
        assertEquals("Ana", resultado.get(1).nombre());
    }

    @Test
    void listarTodosSinEmpleadosRetornaListaVacia() {
        when(empleadoRepositoryPort.findAll()).thenReturn(List.of());

        List<EmpleadoDto> resultado = listarEmpleadosService.listarTodos();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarTodosMapeaCamposCorrectamente() {
        Empleado empleado = Empleado.builder()
                .id(1L).nombre("Juan").apellido("Pérez").email("juan@example.com")
                .cargo("Dev").salario(new BigDecimal("50000")).fechaIngreso(LocalDate.of(2026, 1, 1))
                .activo(false)
                .build();

        when(empleadoRepositoryPort.findAll()).thenReturn(List.of(empleado));

        List<EmpleadoDto> resultado = listarEmpleadosService.listarTodos();

        EmpleadoDto dto = resultado.get(0);
        assertEquals(1L, dto.id());
        assertEquals("Juan", dto.nombre());
        assertEquals("Pérez", dto.apellido());
        assertEquals("juan@example.com", dto.email());
        assertEquals("Dev", dto.cargo());
        assertEquals(new BigDecimal("50000"), dto.salario());
        assertEquals(LocalDate.of(2026, 1, 1), dto.fechaIngreso());
        assertFalse(dto.activo());
    }

    @Test
    void listarTodosLlamaAlRepositorio() {
        when(empleadoRepositoryPort.findAll()).thenReturn(List.of());

        listarEmpleadosService.listarTodos();

        verify(empleadoRepositoryPort).findAll();
    }
}
