package com.atlashr.atlas_hr.infrastructure.output.microservicio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class BeneficiosMicroservicioClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public BeneficiosMicroservicioClient(RestTemplate restTemplate,
                                          @Value("${microservicio.beneficios.url:http://localhost:3000}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> listarPorEmpleado(Long empleadoId, String ciudad) {
        var builder = UriComponentsBuilder
                .fromUriString(baseUrl + "/api/v1/beneficios/empleado/{empleadoId}");

        if (ciudad != null && !ciudad.isBlank()) {
            builder.queryParam("ciudad", ciudad);
        }

        String url = builder.buildAndExpand(empleadoId).toUriString();

        ResponseEntity<Map> respuesta = restTemplate.exchange(
                url, HttpMethod.GET, null, Map.class);

        return respuesta.getBody();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> crear(Map<String, Object> datos) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entidad = new HttpEntity<>(datos, headers);

        ResponseEntity<Map> respuesta = restTemplate.exchange(
                baseUrl + "/api/v1/beneficios", HttpMethod.POST, entidad, Map.class);

        return respuesta.getBody();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> editar(Long id, Map<String, Object> datos) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entidad = new HttpEntity<>(datos, headers);

        ResponseEntity<Map> respuesta = restTemplate.exchange(
                baseUrl + "/api/v1/beneficios/" + id, HttpMethod.PUT, entidad, Map.class);

        return respuesta.getBody();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> eliminar(Long id) {
        ResponseEntity<Map> respuesta = restTemplate.exchange(
                baseUrl + "/api/v1/beneficios/" + id, HttpMethod.DELETE, null, Map.class);

        return respuesta.getBody();
    }
}
