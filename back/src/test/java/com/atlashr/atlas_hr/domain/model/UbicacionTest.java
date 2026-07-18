package com.atlashr.atlas_hr.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UbicacionTest {

    @Test
    void creacionConDatosValidos() {
        Ubicacion ubicacion = Ubicacion.builder()
                .latitud("4.7110")
                .longitud("-74.0721")
                .displayName("Bogotá, Bogota, Colombia")
                .build();

        assertEquals("4.7110", ubicacion.getLatitud());
        assertEquals("-74.0721", ubicacion.getLongitud());
        assertEquals("Bogotá, Bogota, Colombia", ubicacion.getDisplayName());
    }

    @Test
    void camposNulosPermitidos() {
        Ubicacion ubicacion = Ubicacion.builder().build();

        assertNull(ubicacion.getLatitud());
        assertNull(ubicacion.getLongitud());
        assertNull(ubicacion.getDisplayName());
    }

    @Test
    void builderParcial() {
        Ubicacion ubicacion = Ubicacion.builder()
                .latitud("6.2442")
                .build();

        assertEquals("6.2442", ubicacion.getLatitud());
        assertNull(ubicacion.getLongitud());
        assertNull(ubicacion.getDisplayName());
    }
}
