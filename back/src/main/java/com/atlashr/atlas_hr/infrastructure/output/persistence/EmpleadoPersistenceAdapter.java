package com.atlashr.atlas_hr.infrastructure.output.persistence;

import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.model.Empleado;
import com.atlashr.atlas_hr.infrastructure.mapper.EmpleadoPersistenceMapper;
import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.EmpleadoEntity;
import com.atlashr.atlas_hr.infrastructure.output.persistence.repository.EmpleadoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmpleadoPersistenceAdapter implements EmpleadoRepositoryPort {

    private final EmpleadoRepository repository;
    private final EmpleadoPersistenceMapper mapper;

    public EmpleadoPersistenceAdapter(EmpleadoRepository repository, EmpleadoPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<Empleado> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Empleado save(Empleado empleado) {
        EmpleadoEntity entity = mapper.toEntity(empleado);
        EmpleadoEntity guardado = repository.save(entity);
        return mapper.toDomain(guardado);
    }

    @Override
    public List<Empleado> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
