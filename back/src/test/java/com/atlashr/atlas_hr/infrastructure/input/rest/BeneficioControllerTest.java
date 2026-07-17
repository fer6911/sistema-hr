package com.atlashr.atlas_hr.infrastructure.input.rest;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.CrearBeneficioDto;
import com.atlashr.atlas_hr.application.dto.EditarBeneficioDto;
import com.atlashr.atlas_hr.application.ports.in.CrearBeneficioUseCase;
import com.atlashr.atlas_hr.application.ports.in.EditarBeneficioUseCase;
import com.atlashr.atlas_hr.application.ports.in.EliminarBeneficioUseCase;
import com.atlashr.atlas_hr.application.ports.in.ListarBeneficiosUseCase;
import com.atlashr.atlas_hr.domain.exception.BeneficioNotValidException;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeneficioController.class)
@AutoConfigureMockMvc(addFilters = false)
class BeneficioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrearBeneficioUseCase crearBeneficioUseCase;

    @MockitoBean
    private ListarBeneficiosUseCase listarBeneficiosUseCase;

    @MockitoBean
    private EditarBeneficioUseCase editarBeneficioUseCase;

    @MockitoBean
    private EliminarBeneficioUseCase eliminarBeneficioUseCase;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private CrearBeneficioDto crearDto() {
        return new CrearBeneficioDto(1L, "Seguro Médico", new BigDecimal("500.00"));
    }

    private BeneficioDto beneficioResponse() {
        return new BeneficioDto(1L, 1L, "Seguro Médico", new BigDecimal("500.00"));
    }

    @Test
    void crearBeneficioExitoso() throws Exception {
        when(crearBeneficioUseCase.crear(any(CrearBeneficioDto.class))).thenReturn(beneficioResponse());

        mockMvc.perform(post("/api/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data.nombreBeneficio").value("Seguro Médico"))
                .andExpect(jsonPath("$.data.monto").value(500.00))
                .andExpect(jsonPath("$.data.empleadoId").value(1));
    }

    @Test
    void crearBeneficioEmpleadoNoExiste() throws Exception {
        when(crearBeneficioUseCase.crear(any(CrearBeneficioDto.class)))
                .thenThrow(new BeneficioNotValidException(List.of("El empleado no existe")));

        mockMvc.perform(post("/api/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearDto())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errors[0]").value("El empleado no existe"));
    }

    @Test
    void crearBeneficioSinNombre() throws Exception {
        String json = """
                {
                    "empleadoId": 1,
                    "monto": 500.00
                }
                """;

        mockMvc.perform(post("/api/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));
    }

    @Test
    void crearBeneficioSinMonto() throws Exception {
        String json = """
                {
                    "empleadoId": 1,
                    "nombreBeneficio": "Seguro Médico"
                }
                """;

        mockMvc.perform(post("/api/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));
    }

    @Test
    void crearBeneficioSinEmpleadoId() throws Exception {
        String json = """
                {
                    "nombreBeneficio": "Seguro Médico",
                    "monto": 500.00
                }
                """;

        mockMvc.perform(post("/api/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));
    }

    @Test
    void listarBeneficiosPorEmpleado() throws Exception {
        when(listarBeneficiosUseCase.listarPorEmpleado(1L)).thenReturn(List.of(beneficioResponse()));

        mockMvc.perform(get("/api/beneficios/empleado/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data[0].nombreBeneficio").value("Seguro Médico"))
                .andExpect(jsonPath("$.data[0].monto").value(500.00));
    }

    @Test
    void listarBeneficiosSinRegistros() throws Exception {
        when(listarBeneficiosUseCase.listarPorEmpleado(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/beneficios/empleado/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void editarBeneficioExitoso() throws Exception {
        BeneficioDto editado = new BeneficioDto(1L, 1L, "Seguro Dental", new BigDecimal("800.00"));
        when(editarBeneficioUseCase.editar(eq(1L), any(EditarBeneficioDto.class))).thenReturn(editado);

        String json = """
                {
                    "nombreBeneficio": "Seguro Dental",
                    "monto": 800.00
                }
                """;

        mockMvc.perform(put("/api/beneficios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.data.nombreBeneficio").value("Seguro Dental"))
                .andExpect(jsonPath("$.data.monto").value(800.00));
    }

    @Test
    void editarBeneficioNoExiste() throws Exception {
        when(editarBeneficioUseCase.editar(eq(999L), any(EditarBeneficioDto.class)))
                .thenThrow(new BeneficioNotValidException(List.of("El beneficio no existe")));

        String json = """
                {
                    "nombreBeneficio": "Seguro Dental",
                    "monto": 800.00
                }
                """;

        mockMvc.perform(put("/api/beneficios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errors[0]").value("El beneficio no existe"));
    }

    @Test
    void editarBeneficioSinNombre() throws Exception {
        String json = """
                {
                    "monto": 800.00
                }
                """;

        mockMvc.perform(put("/api/beneficios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true));
    }

    @Test
    void eliminarBeneficioExitoso() throws Exception {
        mockMvc.perform(delete("/api/beneficios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false));

        verify(eliminarBeneficioUseCase).eliminar(1L);
    }

    @Test
    void eliminarBeneficioNoExiste() throws Exception {
        doThrow(new BeneficioNotValidException(List.of("El beneficio no existe")))
                .when(eliminarBeneficioUseCase).eliminar(999L);

        mockMvc.perform(delete("/api/beneficios/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.errors[0]").value("El beneficio no existe"));
    }
}
