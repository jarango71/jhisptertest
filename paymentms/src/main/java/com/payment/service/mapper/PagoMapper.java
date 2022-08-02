package com.payment.service.mapper;

import com.payment.domain.Cuota;
import com.payment.domain.Pago;
import com.payment.service.dto.CuotaDTO;
import com.payment.service.dto.PagoDTO;
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
