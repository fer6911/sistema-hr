package com.atlashr.atlas_hr.infrastructure.output.persistence;

import com.atlashr.atlas_hr.application.ports.out.BeneficioRepositoryPort;
import com.atlashr.atlas_hr.domain.model.Beneficio;
import com.atlashr.atlas_hr.infrastructure.mapper.BeneficioPersistenceMapper;
import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.BeneficioEntity;
import com.atlashr.atlas_hr.infrastructure.output.persistence.repository.BeneficioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BeneficioPersistenceAdapter implements BeneficioRepositoryPort {

    private final BeneficioRepository repository;
    private final BeneficioPersistenceMapper mapper;

    public BeneficioPersistenceAdapter(BeneficioRepository repository, BeneficioPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Beneficio> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Beneficio save(Beneficio beneficio) {
        BeneficioEntity entity = mapper.toEntity(beneficio);
        BeneficioEntity guardado = repository.save(entity);
        return mapper.toDomain(guardado);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Beneficio> findByEmpleadoId(Long empleadoId) {
        return repository.findByEmpleadoId(empleadoId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
