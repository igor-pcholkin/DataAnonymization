package com.discreet.datamasking.autodetect;

import com.discreet.datamasking.CommandLineArgs;
import com.discreet.datamasking.transformations.Transformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TransformationsAutoDetector {

    @Autowired
    private SchemaSqlReader schemaSqlReader;

    @Autowired
    private SchemaMetadataReader schemaMetadataReader;

    @Autowired
    private ColumnTranslationsLoader columnTranslationsLoader;

    @Autowired
    private ColumnToAnonymizerLoader columnToAnonymizerLoader;

    private Map<String, String> columnToAnonymizerTable;

    private static final String DEFAULT_ID_COLUMN = "id";

    public List<Transformation> autodetectTransformations(CommandLineArgs commandLineArgs) {
        if (columnToAnonymizerTable == null) {
            columnToAnonymizerTable = columnToAnonymizerLoader.getColumnToAnonymizerTable();
        }
        Set<String> columnToAnonymizerKeys = columnToAnonymizerTable.keySet();
        Map<String, Set<String>> columnTranslations = columnTranslationsLoader.readColumns();
        List<DBTable> tables = readSchema(commandLineArgs);

        return tables.stream().map(table -> mapTableToTransformation(table, columnToAnonymizerKeys, columnTranslations))
                .collect(Collectors.toList());
    }

    private List<DBTable> readSchema(CommandLineArgs commandLineArgs) {
        List<DBTable> tables = null;
        if (commandLineArgs.getSchemaFileName() != null) {
            if (commandLineArgs.getDefaultSchemaName() != null) {
                schemaSqlReader.setDefaultSchema(commandLineArgs.getDefaultSchemaName());
            }
            tables = schemaSqlReader.readDDL(commandLineArgs.getSchemaFileName());
        } else if (commandLineArgs.getSchemaName() != null) {
            tables = schemaMetadataReader.read(commandLineArgs.getSchemaName());
        }
        return tables;
    }

    private Transformation mapTableToTransformation(DBTable table,
                                                    Set<String> columnToAnonymizerKeys,
                                                    Map<String, Set<String>> columnTranslationsMap) {
        Transformation transformation = new Transformation(table.getSchema(), table.getTable());

        table.getColumns().forEach(schemaColumn ->
                setAnonymizerForColumn(schemaColumn, transformation, columnToAnonymizerKeys, columnTranslationsMap));
        detectAndSetIdColumns(transformation, table.getColumns());
        return transformation;
    }

    private void setAnonymizerForColumn(Column schemaColumn, Transformation transformation,
                                        Set<String> columnToAnonymizerKeys,
                                        Map<String, Set<String>> columnTranslationsMap) {
        String schemaColumnName = schemaColumn.getName();
        String columnToAnonymizerKey;
        if (columnToAnonymizerKeys.contains(schemaColumnName)) {
            columnToAnonymizerKey = schemaColumnName;
        } else {
            columnToAnonymizerKey = getTranslatedColumnKey(schemaColumnName, columnTranslationsMap);
        }
        if (columnToAnonymizerKey != null) {
            String anonymizer = columnToAnonymizerTable.get(columnToAnonymizerKey);
            if (anonymizer != null) {
                transformation.getColumnToAnonymizerMap().put(schemaColumnName,
                        columnToAnonymizerTable.get(columnToAnonymizerKey));
            } else {
                throw new RuntimeException("Error, column to anonymizer table doesn't contain mapping for " + columnToAnonymizerKey);
            }
        }
    }

    private String getTranslatedColumnKey(String schemaColumnName, Map<String, Set<String>> columnTranslationsMap) {
        return columnTranslationsMap.entrySet().stream().filter(columnTranslationsEntry -> {
            Set<String> columnTranslations = columnTranslationsEntry.getValue();
            return columnTranslations.contains(schemaColumnName);
        }).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    private void detectAndSetIdColumns(Transformation transformation, List<Column> columns) {
        List<String> columnNames = columns.stream().map(Column::getName).toList();
        String idCandidate;
        if (columnNames.contains(DEFAULT_ID_COLUMN)) {
            idCandidate = DEFAULT_ID_COLUMN;
        } else {
            idCandidate = columnNames.stream()
                    .filter(column -> column.endsWith("_id")).findFirst().orElseGet(null);
        }
        if (idCandidate != null) {
            transformation.setIdColumns(List.of(idCandidate));
        } else {
            throw new RuntimeException(String.format("Error: can't autodetect id column for %s.%s",
                    transformation.getSchema(), transformation.getTable()));
        }
    }

    public void setSchemaSqlReader(SchemaSqlReader schemaSqlReader) {
        this.schemaSqlReader = schemaSqlReader;
    }

    public void setColumnTranslationsLoader(ColumnTranslationsLoader columnTranslationsLoader) {
        this.columnTranslationsLoader = columnTranslationsLoader;
    }

    public void setColumnToAnonymizerLoader(ColumnToAnonymizerLoader columnToAnonymizerLoader) {
        this.columnToAnonymizerLoader = columnToAnonymizerLoader;
    }
}
