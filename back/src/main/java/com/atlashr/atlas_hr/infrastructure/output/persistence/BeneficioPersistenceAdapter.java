package com.atlashr.atlas_hr.infrastructure.output.persistence;

import com.atlashr.atlas_hr.application.ports.out.BeneficioRepositoryPort;
import com.atlashr.atlas_hr.domain.model.Beneficio;
import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.BeneficioEntity;
import com.atlashr.atlas_hr.infrastructure.output.persistence.repository.BeneficioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BeneficioPersistenceAdapter implements BeneficioRepositoryPort {

    private final BeneficioRepository repository;

    public BeneficioPersistenceAdapter(BeneficioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Beneficio> findById(Long id) {
        return repository.findById(id).map(e -> Beneficio.builder()
                .id(e.getId())
                .empleadoId(e.getEmpleadoId())
                .nombreBeneficio(e.getNombreBeneficio())
                .monto(e.getMonto())
                .build());
    }

    @Override
    public Beneficio save(Beneficio beneficio) {
        BeneficioEntity entity = new BeneficioEntity(
                beneficio.getEmpleadoId(),
                beneficio.getNombreBeneficio(),
                beneficio.getMonto()
        );
        BeneficioEntity guardado = repository.save(entity);
        return Beneficio.builder()
                .id(guardado.getId())
                .empleadoId(guardado.getEmpleadoId())
                .nombreBeneficio(guardado.getNombreBeneficio())
                .monto(guardado.getMonto())
                .build();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Beneficio> findByEmpleadoId(Long empleadoId) {
        return repository.findByEmpleadoId(empleadoId).stream()
                .map(e -> Beneficio.builder()
                        .id(e.getId())
                        .empleadoId(e.getEmpleadoId())
                        .nombreBeneficio(e.getNombreBeneficio())
                        .monto(e.getMonto())
                        .build()
                )
                .toList();
    }
}
