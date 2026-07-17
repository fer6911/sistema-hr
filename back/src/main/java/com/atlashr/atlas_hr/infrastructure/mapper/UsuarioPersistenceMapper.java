package com.atlashr.atlas_hr.infrastructure.mapper;

import com.atlashr.atlas_hr.domain.model.Usuario;
import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioPersistenceMapper {

    @Mapping(target = "id", ignore = true)
    UsuarioEntity toEntity(Usuario usuario);

    @Mapping(target = "activo", expression = "java(true)")
    Usuario toDomain(UsuarioEntity entity);
}
