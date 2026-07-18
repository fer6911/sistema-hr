package com.atlashr.atlas_hr.application.dto;

import com.atlashr.atlas_hr.domain.model.Ubicacion;

public record UbicacionDto(
        String latitud,
        String longitud,
        String displayName
) {
    public static UbicacionDto fromDomain(Ubicacion ubicacion) {
        if (ubicacion == null) return null;
        return new UbicacionDto(ubicacion.getLatitud(), ubicacion.getLongitud(), ubicacion.getDisplayName());
    }
}
