package com.discreet.dataprotection.autodetect;

import com.discreet.dataprotection.CommandLineArgs;
import com.discreet.dataprotection.transformations.Transformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
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

        return tables.stream().map(table -> mapTableToTransformation(table, columnToAnonymizerKeys, columnTranslations,
                        commandLineArgs))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<DBTable> readSchema(CommandLineArgs commandLineArgs) {
        log.info("Reading schema...");
        List<DBTable> tables = null;
        if (commandLineArgs.getSchemaFileName() != null) {
            tables = readSchemaFromFile(commandLineArgs);
        } else if (commandLineArgs.getSchemaName() != null) {
            tables = readSchemaFromMetadata(commandLineArgs.getSchemaName());
        }
        return tables;
    }

    private List<DBTable> readSchemaFromFile(CommandLineArgs commandLineArgs) {
        schemaSqlReader.setDefaultSchema(commandLineArgs.getDefaultSchemaName());
        schemaSqlReader.setDBEngine(commandLineArgs.getDbEngine());
        return schemaSqlReader.readDDL(commandLineArgs.getSchemaFileName());
    }

    private List<DBTable> readSchemaFromMetadata(String schemaName) {
        return schemaMetadataReader.read(schemaName);
    }

    private Transformation mapTableToTransformation(DBTable table,
                                                    Set<String> columnToAnonymizerKeys,
                                                    Map<String, Set<String>> columnTranslationsMap,
                                                    CommandLineArgs commandLineArgs) {
        log.info("Processing db table {}.{}...", table.getSchema(), table.getTable());

        Transformation transformation = new Transformation(table.getSchema(), table.getTable());

        Transformation finalTransformation = transformation;
        table.getColumns().forEach(schemaColumn ->
                setAnonymizerForColumn(schemaColumn, finalTransformation, columnToAnonymizerKeys, columnTranslationsMap));
        transformation = detectAndSetIdColumns(transformation, table.getColumns(), commandLineArgs.isIgnoreMissingIds());
        return transformation;
    }

    private void setAnonymizerForColumn(Column schemaColumn, Transformation transformation,
                                        Set<String> columnToAnonymizerKeys,
                                        Map<String, Set<String>> columnTranslationsMap) {
        String schemaColumnName = schemaColumn.getName();
        String columnToAnonymizerKey = getColumnKey(schemaColumnName, columnToAnonymizerKeys);
        if (columnToAnonymizerKey == null) {
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
            return getColumnKey(schemaColumnName, columnTranslations) != null;
        }).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    private String getColumnKey(String columnName, Set<String> names) {
        if (names.contains(columnName)) {
            return columnName;
        } else if (columnName.indexOf('_') > -1) {
            Set<String> schemaColumnNameTokens = split(columnName, "_");
            schemaColumnNameTokens.retainAll(names);
            return schemaColumnNameTokens.stream().findFirst().orElse(null);
        } else {
            return null;
        }
    }

    private Set<String> split(String name, String delim) {
        Set<String> splitted = new HashSet<>();
        Collections.addAll(splitted, name.split(delim));
        return splitted;
    }

    private Transformation detectAndSetIdColumns(Transformation transformation, List<Column> columns, boolean ignoreMissingIds) {
        List<String> columnNames = columns.stream().map(Column::getName).toList();
        String idCandidate;
        if (columnNames.contains(DEFAULT_ID_COLUMN)) {
            idCandidate = DEFAULT_ID_COLUMN;
        } else {
            idCandidate = columnNames.stream()
                    .filter(column -> column.endsWith("_id")).findFirst().orElse(null);
        }
        if (idCandidate != null) {
            transformation.setIdColumns(List.of(idCandidate));
            return transformation;
        } else {
            if (ignoreMissingIds) {
                log.info("Skipping table {}.{} as no id column was detected",
                        transformation.getSchema(),
                        transformation.getTable());
                return null;
            }
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
