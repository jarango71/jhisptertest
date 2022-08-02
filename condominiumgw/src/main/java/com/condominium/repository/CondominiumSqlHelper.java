package com.condominium.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CondominiumSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombre", table, columnPrefix + "_nombre"));
        columns.add(Column.aliased("direccion", table, columnPrefix + "_direccion"));
        columns.add(Column.aliased("logo", table, columnPrefix + "_logo"));
        columns.add(Column.aliased("latitud", table, columnPrefix + "_latitud"));
        columns.add(Column.aliased("longitud", table, columnPrefix + "_longitud"));
        columns.add(Column.aliased("estado", table, columnPrefix + "_estado"));

        return columns;
    }
}
