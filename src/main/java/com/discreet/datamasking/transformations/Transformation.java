package com.discreet.datamasking.transformations;

import java.util.Objects;

public class Transformation {
    private String schema;
    private String table;
    private String column;
    private String anonymizerName;

    public Transformation() {

    }

    public Transformation(String schema, String table, String column, String anonymizerName) {
        this.schema = schema;
        this.table = table;
        this.column = column;
        this.anonymizerName = anonymizerName;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public String getColumn() {
        return column;
    }

    public String getAnonymizerName() {
        return anonymizerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transformation that = (Transformation) o;
        return Objects.equals(schema, that.schema) && Objects.equals(table, that.table) && Objects.equals(column, that.column) && Objects.equals(anonymizerName, that.anonymizerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema, table, column, anonymizerName);
    }
}
