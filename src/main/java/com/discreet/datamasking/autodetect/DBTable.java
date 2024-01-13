package com.discreet.datamasking.autodetect;

import java.util.List;

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
}
