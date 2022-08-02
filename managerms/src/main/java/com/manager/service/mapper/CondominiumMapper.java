package com.manager.service.mapper;

import com.manager.domain.Condominium;
import com.manager.service.dto.CondominiumDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Condominium} and its DTO {@link CondominiumDTO}.
 */
@Mapper(componentModel = "spring")
public interface CondominiumMapper extends EntityMapper<CondominiumDTO, Condominium> {}
