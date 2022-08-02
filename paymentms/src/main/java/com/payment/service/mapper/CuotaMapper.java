package com.payment.service.mapper;

import com.payment.domain.Cuota;
import com.payment.service.dto.CuotaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cuota} and its DTO {@link CuotaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CuotaMapper extends EntityMapper<CuotaDTO, Cuota> {}
