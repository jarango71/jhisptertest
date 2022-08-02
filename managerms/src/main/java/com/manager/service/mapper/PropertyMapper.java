package com.manager.service.mapper;

import com.manager.domain.Condominium;
import com.manager.domain.Property;
import com.manager.service.dto.CondominiumDTO;
import com.manager.service.dto.PropertyDTO;
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
