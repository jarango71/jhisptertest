package com.condominium.repository.rowmapper;

import com.condominium.domain.Cuota;
import com.condominium.domain.enumeration.CuotaType;
import com.condominium.domain.enumeration.RegisterState;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Cuota}, with proper type conversions.
 */
@Service
public class CuotaRowMapper implements BiFunction<Row, String, Cuota> {

    private final ColumnConverter converter;

    public CuotaRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Cuota} stored in the database.
     */
    @Override
    public Cuota apply(Row row, String prefix) {
        Cuota entity = new Cuota();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombre(converter.fromRow(row, prefix + "_nombre", String.class));
        entity.setTipo(converter.fromRow(row, prefix + "_tipo", CuotaType.class));
        entity.setPeriodicidad(converter.fromRow(row, prefix + "_periodicidad", String.class));
        entity.setAplica(converter.fromRow(row, prefix + "_aplica", String.class));
        entity.setMonto(converter.fromRow(row, prefix + "_monto", Double.class));
        entity.setDiponibilidad(converter.fromRow(row, prefix + "_diponibilidad", String.class));
        entity.setObservacion(converter.fromRow(row, prefix + "_observacion", String.class));
        entity.setEstado(converter.fromRow(row, prefix + "_estado", RegisterState.class));
        return entity;
    }
}
