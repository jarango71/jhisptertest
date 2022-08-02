package com.condominium.service.mapper;

import com.condominium.domain.Cuota;
import com.condominium.domain.Pago;
import com.condominium.service.dto.CuotaDTO;
import com.condominium.service.dto.PagoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pago} and its DTO {@link PagoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PagoMapper extends EntityMapper<PagoDTO, Pago> {
    @Mapping(target = "cuota", source = "cuota", qualifiedByName = "cuotaNombre")
    PagoDTO toDto(Pago s);

    @Named("cuotaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    CuotaDTO toDtoCuotaNombre(Cuota cuota);
}
