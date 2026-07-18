package com.atlashr.atlas_hr.infrastructure.output.nominatim;

import com.atlashr.atlas_hr.domain.model.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NominatimAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    private NominatimAdapter adapter;

    private static final String XML_RESPONSE = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <searchresults querystring="Bogotá">
              <place place_id="125279639"
                     osm_type="way"
                     osm_id="90394480"
                     lat="4.7110"
                     lon="-74.0721"
                     display_name="Bogotá, Bogota, Colombia"
                     class="place"
                     type="city"
                     importance="0.8">
              </place>
            </searchresults>
            """;

    private static final String XML_EMPTY = """
            <?xml version="1.0" encoding="UTF-8" ?>
            <searchresults querystring="CiudadInexistente">
            </searchresults>
            """;

    @BeforeEach
    void setUp() {
        adapter = new NominatimAdapter(restTemplate);
    }

    @Test
    void buscarPorCiudadRetornaUbicacion() {
        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(XML_RESPONSE));

        Optional<Ubicacion> resultado = adapter.buscarPorCiudad("Bogotá");

        assertTrue(resultado.isPresent());
        assertEquals("4.7110", resultado.get().getLatitud());
        assertEquals("-74.0721", resultado.get().getLongitud());
        assertEquals("Bogotá, Bogota, Colombia", resultado.get().getDisplayName());
    }

    @Test
    void buscarPorCiudadSinResultadosRetornaEmpty() {
        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(XML_EMPTY));

        Optional<Ubicacion> resultado = adapter.buscarPorCiudad("CiudadInexistente");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorCiudadRespuestaVaciaRetornaEmpty() {
        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(""));

        Optional<Ubicacion> resultado = adapter.buscarPorCiudad("Bogotá");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorCiudadRespuestaNulaRetornaEmpty() {
        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(null));

        Optional<Ubicacion> resultado = adapter.buscarPorCiudad("Bogotá");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorCiudadErrorDeRedRetornaEmpty() {
        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("Connection refused"));

        Optional<Ubicacion> resultado = adapter.buscarPorCiudad("Bogotá");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void cacheSegundaLlamadaNoLlamaRest() {
        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(XML_RESPONSE));

        adapter.buscarPorCiudad("Bogotá");
        adapter.buscarPorCiudad("Bogotá");

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void cacheNormalizaClave() {
        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(XML_RESPONSE));

        adapter.buscarPorCiudad("Bogotá");
        adapter.buscarPorCiudad("  bogotá  ");

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void formatoUrlContieneFormatXml() {
        when(restTemplate.exchange(any(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(XML_RESPONSE));

        adapter.buscarPorCiudad("Bogotá");

        verify(restTemplate).exchange(
                any(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        );
    }
}
