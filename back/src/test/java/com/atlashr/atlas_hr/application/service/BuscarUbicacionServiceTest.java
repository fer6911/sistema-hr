package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.BeneficiosConUbicacionDto;
import com.atlashr.atlas_hr.application.ports.in.ListarBeneficiosUseCase;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.application.ports.out.NominatimPort;
import com.atlashr.atlas_hr.domain.exception.EmpleadoNotValidException;
import com.atlashr.atlas_hr.domain.model.Empleado;
import com.atlashr.atlas_hr.domain.model.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarUbicacionServiceTest {

    @Mock
    private ListarBeneficiosUseCase listarBeneficiosUseCase;

    @Mock
    private NominatimPort nominatimPort;

    @Mock
    private EmpleadoRepositoryPort empleadoRepositoryPort;

    private BuscarUbicacionService service;

    @BeforeEach
    void setUp() {
        service = new BuscarUbicacionService(listarBeneficiosUseCase, nominatimPort, empleadoRepositoryPort);
    }

    private Empleado crearEmpleado(Long id, String ciudad) {
        return Empleado.builder()
                .id(id)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@example.com")
                .cargo("Dev")
                .salario(new BigDecimal("50000"))
                .fechaIngreso(LocalDate.of(2026, 1, 1))
                .ciudad(ciudad)
                .build();
    }

    private BeneficioDto crearBeneficioDto() {
        return new BeneficioDto(1L, 1L, "Seguro Médico", new BigDecimal("500.00"));
    }

    @Test
    void empleadoNoExisteLanzaExcepcion() {
        when(empleadoRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        EmpleadoNotValidException ex = assertThrows(EmpleadoNotValidException.class, () ->
                service.listarConUbicacion(999L)
        );
        assertTrue(ex.getErrors().contains("El empleado no existe"));
    }

    @Test
    void empleadoConCiudadRetornaUbicacion() {
        Empleado empleado = crearEmpleado(1L, "Bogotá");
        when(empleadoRepositoryPort.findById(1L)).thenReturn(Optional.of(empleado));
        when(listarBeneficiosUseCase.listarPorEmpleado(1L)).thenReturn(List.of(crearBeneficioDto()));
        when(nominatimPort.buscarPorCiudad("Bogotá")).thenReturn(
                Optional.of(Ubicacion.builder().latitud("4.71").longitud("-74.07").displayName("Bogotá").build())
        );

        BeneficiosConUbicacionDto resultado = service.listarConUbicacion(1L);

        assertEquals(1, resultado.beneficios().size());
        assertNotNull(resultado.ubicacion());
        assertEquals("4.71", resultado.ubicacion().latitud());
    }

    @Test
    void empleadoSinCiudadRetornaUbicacionNull() {
        Empleado empleado = crearEmpleado(1L, null);
        when(empleadoRepositoryPort.findById(1L)).thenReturn(Optional.of(empleado));
        when(listarBeneficiosUseCase.listarPorEmpleado(1L)).thenReturn(List.of(crearBeneficioDto()));

        BeneficiosConUbicacionDto resultado = service.listarConUbicacion(1L);

        assertEquals(1, resultado.beneficios().size());
        assertNull(resultado.ubicacion());
        verify(nominatimPort, never()).buscarPorCiudad(any());
    }

    @Test
    void nominatimNoEncuentraCiudadRetornaUbicacionNull() {
        Empleado empleado = crearEmpleado(1L, "CiudadFantasma");
        when(empleadoRepositoryPort.findById(1L)).thenReturn(Optional.of(empleado));
        when(listarBeneficiosUseCase.listarPorEmpleado(1L)).thenReturn(List.of(crearBeneficioDto()));
        when(nominatimPort.buscarPorCiudad("CiudadFantasma")).thenReturn(Optional.empty());

        BeneficiosConUbicacionDto resultado = service.listarConUbicacion(1L);

        assertEquals(1, resultado.beneficios().size());
        assertNull(resultado.ubicacion());
    }

    @Test
    void listadoVacioSinUbicacion() {
        Empleado empleado = crearEmpleado(1L, "Bogotá");
        when(empleadoRepositoryPort.findById(1L)).thenReturn(Optional.of(empleado));
        when(listarBeneficiosUseCase.listarPorEmpleado(1L)).thenReturn(List.of());
        when(nominatimPort.buscarPorCiudad("Bogotá")).thenReturn(
                Optional.of(Ubicacion.builder().latitud("4.71").longitud("-74.07").displayName("Bogotá").build())
        );

        BeneficiosConUbicacionDto resultado = service.listarConUbicacion(1L);

        assertTrue(resultado.beneficios().isEmpty());
        assertNotNull(resultado.ubicacion());
    }
}
