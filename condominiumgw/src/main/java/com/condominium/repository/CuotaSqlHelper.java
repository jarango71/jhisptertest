package com.condominium.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CuotaSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("tipo", table, columnPrefix + "_tipo"));
        columns.add(Column.aliased("periodicidad", table, columnPrefix + "_periodicidad"));
        columns.add(Column.aliased("aplica", table, columnPrefix + "_aplica"));
        columns.add(Column.aliased("monto", table, columnPrefix + "_monto"));
        columns.add(Column.aliased("diponibilidad", table, columnPrefix + "_diponibilidad"));
        columns.add(Column.aliased("observacion", table, columnPrefix + "_observacion"));
        columns.add(Column.aliased("estado", table, columnPrefix + "_estado"));

        return columns;
    }
}
