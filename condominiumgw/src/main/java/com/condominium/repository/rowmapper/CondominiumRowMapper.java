package com.condominium.repository.rowmapper;

import com.condominium.domain.Condominium;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Condominium}, with proper type conversions.
 */
@Service
public class CondominiumRowMapper implements BiFunction<Row, String, Condominium> {

    private final ColumnConverter converter;

    public CondominiumRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Condominium} stored in the database.
     */
    @Override
    public Condominium apply(Row row, String prefix) {
        Condominium entity = new Condominium();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setDireccion(converter.fromRow(row, prefix + "_direccion", String.class));
        entity.setLogo(converter.fromRow(row, prefix + "_logo", String.class));
        entity.setLatitud(converter.fromRow(row, prefix + "_latitud", Double.class));
        entity.setLongitud(converter.fromRow(row, prefix + "_longitud", Double.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", Boolean.class));
        return entity;
    }
}
