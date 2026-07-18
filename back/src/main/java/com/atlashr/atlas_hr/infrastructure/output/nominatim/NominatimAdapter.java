package com.atlashr.atlas_hr.infrastructure.output.nominatim;

import com.atlashr.atlas_hr.application.ports.out.NominatimPort;
import com.atlashr.atlas_hr.domain.model.Ubicacion;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NominatimAdapter implements NominatimPort {

    private static final String BASE_URL = "https://nominatim.openstreetmap.org/search";
    private static final String USER_AGENT = "AtlasHR/1.0";
    private static final long TTL_MILLIS = 24 * 60 * 60 * 1000L;

    private final RestTemplate restTemplate;
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public NominatimAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<Ubicacion> buscarPorCiudad(String ciudad) {
        String claveNormalizada = ciudad.trim().toLowerCase();

        CacheEntry cacheado = cache.get(claveNormalizada);
        if (cacheado != null && !cacheado.expiro()) {
            return cacheado.ubicacion();
        }

        Optional<Ubicacion> resultado = consultarNominatim(ciudad);
        cache.put(claveNormalizada, new CacheEntry(resultado, Instant.now().plusMillis(TTL_MILLIS)));
        return resultado;
    }

    private Optional<Ubicacion> consultarNominatim(String ciudad) {
        try {
            String url = UriComponentsBuilder.fromUriString(BASE_URL)
                    .queryParam("q", ciudad)
                    .queryParam("format", "xml")
                    .queryParam("limit", 1)
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", USER_AGENT);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getBody() == null || response.getBody().isBlank()) {
                return Optional.empty();
            }

            return parsearXml(response.getBody());

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<Ubicacion> parsearXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        NodeList lugares = doc.getElementsByTagName("place");
        if (lugares.getLength() == 0) {
            return Optional.empty();
        }

        Element primerLugar = (Element) lugares.item(0);
        String lat = primerLugar.getAttribute("lat");
        String lon = primerLugar.getAttribute("lon");
        String displayName = primerLugar.getAttribute("display_name");

        return Optional.of(Ubicacion.builder()
                .latitud(lat)
                .longitud(lon)
                .displayName(displayName)
                .build());
    }

    private record CacheEntry(Optional<Ubicacion> ubicacion, Instant expiracion) {
        boolean expiro() {
            return Instant.now().isAfter(expiracion);
        }
    }
}
