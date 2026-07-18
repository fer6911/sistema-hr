package com.atlashr.atlas_hr.application.service;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.BeneficiosConUbicacionDto;
import com.atlashr.atlas_hr.application.dto.UbicacionDto;
import com.atlashr.atlas_hr.application.ports.in.ListarBeneficiosConUbicacionUseCase;
import com.atlashr.atlas_hr.application.ports.in.ListarBeneficiosUseCase;
import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.application.ports.out.NominatimPort;
import com.atlashr.atlas_hr.domain.exception.EmpleadoNotValidException;
import com.atlashr.atlas_hr.domain.model.Empleado;
import com.atlashr.atlas_hr.domain.model.Ubicacion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscarUbicacionService implements ListarBeneficiosConUbicacionUseCase {

    private final ListarBeneficiosUseCase listarBeneficiosUseCase;
    private final NominatimPort nominatimPort;
    private final EmpleadoRepositoryPort empleadoRepositoryPort;

    public BuscarUbicacionService(ListarBeneficiosUseCase listarBeneficiosUseCase,
                                  NominatimPort nominatimPort,
                                  EmpleadoRepositoryPort empleadoRepositoryPort) {
        this.listarBeneficiosUseCase = listarBeneficiosUseCase;
        this.nominatimPort = nominatimPort;
        this.empleadoRepositoryPort = empleadoRepositoryPort;
    }

    @Override
    public BeneficiosConUbicacionDto listarConUbicacion(Long empleadoId) {
        Empleado empleado = empleadoRepositoryPort.findById(empleadoId)
                .orElseThrow(() -> new EmpleadoNotValidException(List.of("El empleado no existe")));

        List<BeneficioDto> beneficios = listarBeneficiosUseCase.listarPorEmpleado(empleadoId);

        UbicacionDto ubicacionDto = null;
        if (empleado.getCiudad() != null && !empleado.getCiudad().isBlank()) {
            Optional<Ubicacion> ubicacion = nominatimPort.buscarPorCiudad(empleado.getCiudad());
            ubicacionDto = ubicacion.map(UbicacionDto::fromDomain).orElse(null);
        }

        return new BeneficiosConUbicacionDto(beneficios, ubicacionDto);
    }
}
