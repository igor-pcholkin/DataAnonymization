package com.discreet.dataprotection.transformations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Transformation {
    final private String schema;
    final private String table;
    private Map<String, String> columnToAnonymizerMap;
    private List<String> idColumns;

    public Transformation(String schema, String table) {
        this.schema = schema;
        this.table = table;
        columnToAnonymizerMap = new HashMap<>();
    }

    public Transformation(String schema, String table, Map<String, String> columnToAnonymizerMap) {
        this.schema = schema;
        this.table = table;
        this.columnToAnonymizerMap = columnToAnonymizerMap;
    }

    public Transformation(String schema, String table, Map<String, String> columnToAnonymizerMap, List<String> idColumns) {
        this(schema, table, columnToAnonymizerMap);
        this.idColumns = idColumns;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public Map<String, String> getColumnToAnonymizerMap() {
        return columnToAnonymizerMap;
    }

    public void setColumnToAnonymizerMap(Map<String, String> columnToAnonymizerMap) {
        this.columnToAnonymizerMap = columnToAnonymizerMap;
    }

    public void setIdColumns(List<String> idColumns) {
        this.idColumns = idColumns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transformation that = (Transformation) o;
        return Objects.equals(schema, that.schema) && Objects.equals(table, that.table) && Objects.equals(columnToAnonymizerMap, that.columnToAnonymizerMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema, table, columnToAnonymizerMap);
    }

    public List<String> getIdColumns() {
        return idColumns;
    }

    @Override
    public String toString() {
        return "Transformation{" +
                "schema='" + schema + '\'' +
                ", table='" + table + '\'' +
                ", columnToAnonymizerMap=" + columnToAnonymizerMap +
                ", idColumns=" + idColumns +
                '}';
    }
}
