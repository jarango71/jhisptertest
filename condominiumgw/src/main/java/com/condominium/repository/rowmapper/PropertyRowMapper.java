package com.condominium.repository.rowmapper;

import com.condominium.domain.Property;
import com.condominium.domain.enumeration.RegisterState;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Property}, with proper type conversions.
 */
@Service
public class PropertyRowMapper implements BiFunction<Row, String, Property> {

    private final ColumnConverter converter;

    public PropertyRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Property} stored in the database.
     */
    @Override
    public Property apply(Row row, String prefix) {
        Property entity = new Property();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setManzana(converter.fromRow(row, prefix + "_manzana", String.class));
        entity.setBloque(converter.fromRow(row, prefix + "_bloque", String.class));
        entity.setNumero(converter.fromRow(row, prefix + "_numero", String.class));
        entity.setUbicacion(converter.fromRow(row, prefix + "_ubicacion", String.class));
        entity.setTipo(converter.fromRow(row, prefix + "_tipo", String.class));
        entity.setDiponibilidad(converter.fromRow(row, prefix + "_diponibilidad", String.class));
        entity.setObservacion(converter.fromRow(row, prefix + "_observacion", String.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", RegisterState.class));
        entity.setCondominiumId(converter.fromRow(row, prefix + "_condominium_id", Long.class));
        return entity;
    }
}
