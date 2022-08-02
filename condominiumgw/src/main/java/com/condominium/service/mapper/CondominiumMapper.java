package com.condominium.service.mapper;

import com.condominium.domain.Condominium;
import com.condominium.service.dto.CondominiumDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Condominium} and its DTO {@link CondominiumDTO}.
 */
@Mapper(componentModel = "spring")
public interface CondominiumMapper extends EntityMapper<CondominiumDTO, Condominium> {}
