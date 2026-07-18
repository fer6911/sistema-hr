package com.atlashr.atlas_hr.application.ports.out;

import com.atlashr.atlas_hr.domain.model.Ubicacion;

import java.util.Optional;

public interface NominatimPort {
    Optional<Ubicacion> buscarPorCiudad(String ciudad);
}
