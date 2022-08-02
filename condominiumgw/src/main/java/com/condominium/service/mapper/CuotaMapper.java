package com.condominium.service.mapper;

import com.condominium.domain.Cuota;
import com.condominium.service.dto.CuotaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cuota} and its DTO {@link CuotaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CuotaMapper extends EntityMapper<CuotaDTO, Cuota> {}
