package com.atlashr.atlas_hr.infrastructure.mapper;

import com.atlashr.atlas_hr.domain.model.Empleado;
import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.EmpleadoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmpleadoPersistenceMapper {

    @Mapping(target = "id", ignore = true)
    EmpleadoEntity toEntity(Empleado empleado);

    @Mapping(target = "activo", expression = "java(true)")
    Empleado toDomain(EmpleadoEntity entity);
}
