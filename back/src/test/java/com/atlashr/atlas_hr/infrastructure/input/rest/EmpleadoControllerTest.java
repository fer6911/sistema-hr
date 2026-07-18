package com.atlashr.atlas_hr.infrastructure.input.rest;

import com.atlashr.atlas_hr.application.dto.CrearEmpleadoDto;
import com.atlashr.atlas_hr.application.dto.EmpleadoDto;
import com.atlashr.atlas_hr.application.ports.in.CrearEmpleadoUseCase;
import com.atlashr.atlas_hr.application.ports.in.ListarEmpleadosUseCase;
import com.atlashr.atlas_hr.domain.exception.EmpleadoNotValidException;
import com.atlashr.atlas_hr.infrastructure.security.JwtUtil;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpleadoController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrearEmpleadoUseCase crearEmpleadoUseCase;

    @MockitoBean
    private ListarEmpleadosUseCase listarEmpleadosUseCase;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private CrearEmpleadoDto crearDto() {
        return new CrearEmpleadoDto("Juan", "Pérez", "juan@example.com",
                "Desarrollador", new BigDecimal("50000.00"), LocalDate.of(2026, 1, 15), "Bogotá");
    }

    private EmpleadoDto empleadoResponse() {
        return new EmpleadoDto(1L, "Juan", "Pérez", "juan@example.com",
                "Desarrollador", new BigDecimal("50000.00"), LocalDate.of(2026, 1, 15), "Bogotá", true);
    }

    @Test
    void crearEmpleadoExitoso() throws Exception {
        when(crearEmpleadoUseCase.crear(any(CrearEmpleadoDto.class))).thenReturn(empleadoResponse());

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data.nombre").value("Juan"))
                .andExpect(jsonPath("$.data.apellido").value("Pérez"))
                .andExpect(jsonPath("$.data.email").value("juan@example.com"))
                .andExpect(jsonPath("$.data.cargo").value("Desarrollador"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void crearEmpleadoEmailInvalido() throws Exception {
        CrearEmpleadoDto dto = new CrearEmpleadoDto("Juan", "Pérez", "noemail",
                "Desarrollador", new BigDecimal("50000.00"), LocalDate.of(2026, 1, 15), "Bogotá");

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));
    }

    @Test
    void crearEmpleadoNombreVacio() throws Exception {
        CrearEmpleadoDto dto = new CrearEmpleadoDto("", "Pérez", "juan@example.com",
                "Desarrollador", new BigDecimal("50000.00"), LocalDate.of(2026, 1, 15), "Bogotá");

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));
    }

    @Test
    void crearEmpleadoEmailDuplicado() throws Exception {
        when(crearEmpleadoUseCase.crear(any(CrearEmpleadoDto.class)))
                .thenThrow(new EmpleadoNotValidException(List.of("El email ya está registrado")));

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearDto())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errors[0]").value("El email ya está registrado"));
    }

    @Test
    void crearEmpleadoSinSalario() throws Exception {
        String json = """
                {
                    "nombre": "Juan",
                    "apellido": "Pérez",
                    "email": "juan@example.com",
                    "cargo": "Desarrollador",
                    "ciudad": "Bogotá",
                    "fechaIngreso": "2026-01-15"
                }
                """;

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));
    }

    @Test
    void listarEmpleadosRetornaLista() throws Exception {
        when(listarEmpleadosUseCase.listarTodos()).thenReturn(List.of(empleadoResponse()));

        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data[0].nombre").value("Juan"))
                .andExpect(jsonPath("$.data[0].email").value("juan@example.com"));
    }

    @Test
    void listarEmpleadosSinRegistros() throws Exception {
        when(listarEmpleadosUseCase.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
