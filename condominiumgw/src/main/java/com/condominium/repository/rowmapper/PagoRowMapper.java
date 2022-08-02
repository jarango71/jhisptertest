package com.condominium.repository.rowmapper;

import com.condominium.domain.Pago;
import com.condominium.domain.enumeration.RegisterState;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Pago}, with proper type conversions.
 */
@Service
public class PagoRowMapper implements BiFunction<Row, String, Pago> {

    private final ColumnConverter converter;

    public PagoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Pago} stored in the database.
     */
    @Override
    public Pago apply(Row row, String prefix) {
        Pago entity = new Pago();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAnio(converter.fromRow(row, prefix + "_anio", String.class));
        entity.setMes(converter.fromRow(row, prefix + "_mes", String.class));
        entity.setValor(converter.fromRow(row, prefix + "_valor", Double.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", RegisterState.class));
        entity.setFechaGeneracion(converter.fromRow(row, prefix + "_fecha_generacion", LocalDate.class));
        entity.setFechaPago(converter.fromRow(row, prefix + "_fecha_pago", LocalDate.class));
        entity.setCuotaId(converter.fromRow(row, prefix + "_cuota_id", Long.class));
        return entity;
    }
}
