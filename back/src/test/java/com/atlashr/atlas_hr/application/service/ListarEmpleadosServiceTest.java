package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.mapper.EmpleadoApplicationMapper;
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

    @Mock
    private EmpleadoApplicationMapper mapper;

    private ListarEmpleadosService listarEmpleadosService;

    @BeforeEach
    void setUp() {
        listarEmpleadosService = new ListarEmpleadosService(empleadoRepositoryPort, mapper);
    }

    @Test
    void listarTodosRetornaLista() {
        Empleado e1 = Empleado.builder().id(1L).nombre("Juan").apellido("Pérez").email("juan@example.com")
                .cargo("Dev").salario(new BigDecimal("50000")).fechaIngreso(LocalDate.of(2026, 1, 1)).ciudad("Bogotá").build();
        Empleado e2 = Empleado.builder().id(2L).nombre("Ana").apellido("García").email("ana@example.com")
                .cargo("PM").salario(new BigDecimal("60000")).fechaIngreso(LocalDate.of(2026, 2, 1)).ciudad("Medellín").build();

        when(empleadoRepositoryPort.findAll()).thenReturn(List.of(e1, e2));
        when(mapper.toDto(e1)).thenReturn(new EmpleadoDto(1L, "Juan", "Pérez", "juan@example.com", "Dev", new BigDecimal("50000"), LocalDate.of(2026, 1, 1), "Bogotá", true));
        when(mapper.toDto(e2)).thenReturn(new EmpleadoDto(2L, "Ana", "García", "ana@example.com", "PM", new BigDecimal("60000"), LocalDate.of(2026, 2, 1), "Medellín", true));

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
    void listarTodosLlamaAlRepositorio() {
        when(empleadoRepositoryPort.findAll()).thenReturn(List.of());

        listarEmpleadosService.listarTodos();

        verify(empleadoRepositoryPort).findAll();
    }
}
