package com.condominium.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PropertySqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("manzana", table, columnPrefix + "_manzana"));
        columns.add(Column.aliased("bloque", table, columnPrefix + "_bloque"));
        columns.add(Column.aliased("numero", table, columnPrefix + "_numero"));
        columns.add(Column.aliased("ubicacion", table, columnPrefix + "_ubicacion"));
        columns.add(Column.aliased("tipo", table, columnPrefix + "_tipo"));
        columns.add(Column.aliased("diponibilidad", table, columnPrefix + "_diponibilidad"));
        columns.add(Column.aliased("observacion", table, columnPrefix + "_observacion"));
        columns.add(Column.aliased("estado", table, columnPrefix + "_estado"));

        columns.add(Column.aliased("condominium_id", table, columnPrefix + "_condominium_id"));
        return columns;
    }
}
