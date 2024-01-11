package com.discreet.datamasking.transformations;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Transformation {
    private String schema;
    private String table;
    private Map<String, String> columns;

    public Transformation(String schema, String table) {
        this.schema = schema;
        this.table = table;
        columns = new HashMap<>();
    }

    public Transformation(String schema, String table, Map<String, String> columns) {
        this.schema = schema;
        this.table = table;
        this.columns = columns;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public void add(String column, String anonymizer) {
        columns.put(column, anonymizer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transformation that = (Transformation) o;
        return Objects.equals(schema, that.schema) && Objects.equals(table, that.table) && Objects.equals(columns, that.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema, table, columns);
    }
}
