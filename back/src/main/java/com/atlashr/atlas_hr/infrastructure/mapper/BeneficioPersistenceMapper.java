package com.atlashr.atlas_hr.infrastructure.mapper;

import com.atlashr.atlas_hr.domain.model.Beneficio;
import com.atlashr.atlas_hr.infrastructure.output.persistence.entity.BeneficioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BeneficioPersistenceMapper {

    @Mapping(target = "id", ignore = true)
    BeneficioEntity toEntity(Beneficio beneficio);

    Beneficio toDomain(BeneficioEntity entity);
}
