package com.discreet.datamasking.transformations;

import java.util.Objects;

public class Column {
    private String columnName;
    private String anonymizerName;

    public Column(String columnName, String anonymizerName) {
        this.columnName = columnName;
        this.anonymizerName = anonymizerName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getAnonymizerName() {
        return anonymizerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return Objects.equals(columnName, column.columnName) && Objects.equals(anonymizerName, column.anonymizerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, anonymizerName);
    }
}
