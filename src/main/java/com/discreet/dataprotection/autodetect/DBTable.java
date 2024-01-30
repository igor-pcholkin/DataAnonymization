package com.discreet.dataprotection.autodetect;

import java.util.List;
import java.util.Objects;

public class DBTable {
    private String schema;

    private String table;
    private List<Column> columns;

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public DBTable(String schema, String table, List<Column> columns) {
        this.schema = schema;
        this.table = table;
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "DBTable{" +
                "schema='" + schema + '\'' +
                ", table='" + table + '\'' +
                ", columns=" + columns +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBTable dbTable = (DBTable) o;
        return Objects.equals(schema, dbTable.schema) && Objects.equals(table, dbTable.table) && Objects.equals(columns, dbTable.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema, table, columns);
    }
}
