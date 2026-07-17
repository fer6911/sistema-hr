package com.atlashr.atlas_hr.application.mapper;

import com.atlashr.atlas_hr.application.dto.CrearUsuarioDto;
import com.atlashr.atlas_hr.application.dto.UsuarioCreadoDto;
import com.atlashr.atlas_hr.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UsuarioApplicationMapper {

    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "rawPassword")
    Usuario toDomain(CrearUsuarioDto dto);

    @Named("rawPassword")
    default String rawPassword(String password) {
        return password;
    }

    UsuarioCreadoDto toCreadoDto(Usuario usuario);
}
