package com.atlashr.atlas_hr.application.mapper;

import com.atlashr.atlas_hr.application.dto.BeneficioDto;
import com.atlashr.atlas_hr.application.dto.CrearBeneficioDto;
import com.atlashr.atlas_hr.application.dto.EditarBeneficioDto;
import com.atlashr.atlas_hr.domain.model.Beneficio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BeneficioApplicationMapper {

    @Mapping(target = "id", ignore = true)
    Beneficio toDomain(CrearBeneficioDto dto);

    BeneficioDto toDto(Beneficio beneficio);

    void actualizar(@MappingTarget Beneficio existente, EditarBeneficioDto dto);
}
