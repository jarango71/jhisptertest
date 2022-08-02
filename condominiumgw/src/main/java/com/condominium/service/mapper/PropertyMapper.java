package com.condominium.service.mapper;

import com.condominium.domain.Condominium;
import com.condominium.domain.Property;
import com.condominium.service.dto.CondominiumDTO;
import com.condominium.service.dto.PropertyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Property} and its DTO {@link PropertyDTO}.
 */
@Mapper(componentModel = "spring")
public interface PropertyMapper extends EntityMapper<PropertyDTO, Property> {
    @Mapping(target = "condominium", source = "condominium", qualifiedByName = "condominiumNombre")
    PropertyDTO toDto(Property s);

    @Named("condominiumNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    CondominiumDTO toDtoCondominiumNombre(Condominium condominium);
}
