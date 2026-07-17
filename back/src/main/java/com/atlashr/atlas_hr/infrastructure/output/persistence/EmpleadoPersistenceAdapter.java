package com.atlashr.atlas_hr.infrastructure.output.persistence;

import com.atlashr.atlas_hr.application.ports.out.EmpleadoRepositoryPort;
import com.atlashr.atlas_hr.domain.model.Empleado;
import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.EmpleadoEntity;
import com.atlashr.atlas_hr.infrastructure.output.persistence.repository.EmpleadoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmpleadoPersistenceAdapter implements EmpleadoRepositoryPort {

    private final EmpleadoRepository repository;

    public EmpleadoPersistenceAdapter(EmpleadoRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Empleado save(Empleado empleado) {
        EmpleadoEntity entity = new EmpleadoEntity(
                empleado.getNombre(), empleado.getApellido(), empleado.getEmail(),
                empleado.getCargo(), empleado.getSalario(), empleado.getFechaIngreso(),
                empleado.isActivo()
        );
        EmpleadoEntity guardado = repository.save(entity);
        return Empleado.builder()
                .id(guardado.getId())
                .nombre(guardado.getNombre())
                .apellido(guardado.getApellido())
                .email(guardado.getEmail())
                .cargo(guardado.getCargo())
                .salario(guardado.getSalario())
                .fechaIngreso(guardado.getFechaIngreso())
                .activo(guardado.isActivo())
                .build();
    }

    @Override
    public List<Empleado> findAll() {
        return repository.findAll().stream()
                .map(e -> Empleado.builder()
                        .id(e.getId())
                        .nombre(e.getNombre())
                        .apellido(e.getApellido())
                        .email(e.getEmail())
                        .cargo(e.getCargo())
                        .salario(e.getSalario())
                        .fechaIngreso(e.getFechaIngreso())
                        .activo(e.isActivo())
                        .build()
                )
                .toList();
    }
}
