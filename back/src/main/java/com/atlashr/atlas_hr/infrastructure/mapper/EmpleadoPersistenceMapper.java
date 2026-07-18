package com.atlashr.atlas_hr.infrastructure.mapper;

import com.atlashr.atlas_hr.domain.model.Empleado;
import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.EmpleadoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmpleadoPersistenceMapper {

    EmpleadoEntity toEntity(Empleado empleado);

    Empleado toDomain(EmpleadoEntity entity);
}