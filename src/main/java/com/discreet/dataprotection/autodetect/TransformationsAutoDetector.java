package com.discreet.dataprotection.autodetect;

import com.discreet.dataprotection.transformations.Transformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.capitalize;
import static org.springframework.util.StringUtils.uncapitalize;

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

    private final HashMap<String, Set<String>> capitalizedCache = new HashMap<>();

    public List<Transformation> autodetectTransformations(String schemaFilename, String schemaName,
        boolean ignoreMissingIds, String defaultSchemaName, String dbEngine) {
        log.debug("Auto-detecting transformations...");
        if (columnToAnonymizerTable == null) {
            columnToAnonymizerTable = columnToAnonymizerLoader.getColumnToAnonymizerTable();
        }
        Set<String> columnToAnonymizerKeys = columnToAnonymizerTable.keySet();
        Set<String> columnToAnonymizerUpperKeys = capitalizedValues(columnToAnonymizerKeys);
        Map<String, Set<String>> columnTranslations = columnTranslationsLoader.readColumns();
        List<DBTable> tables = readSchema(schemaFilename, schemaName, defaultSchemaName, dbEngine);

        return tables.stream().map(table -> mapTableToTransformation(table, columnToAnonymizerKeys, columnTranslations,
                        ignoreMissingIds, columnToAnonymizerUpperKeys))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Set<String> capitalizedValues(Set<String> values) {
        return values.stream().map(StringUtils::capitalize).collect(Collectors.toSet());
    }

    private List<DBTable> readSchema(String schemaFileName, String schemaName, String defaultSchemaName,
                                     String dbEngine) {
        log.info("Reading schema...");
        List<DBTable> tables = null;
        if (schemaFileName != null) {
            tables = readSchemaFromFile(defaultSchemaName, dbEngine, schemaFileName);
        } else if (schemaName != null) {
            tables = readSchemaFromMetadata(schemaName);
        }
        return tables;
    }

    private List<DBTable> readSchemaFromFile(String defaultSchemaName, String dbEngine, String schemaFileName) {
        schemaSqlReader.setDefaultSchema(defaultSchemaName);
        schemaSqlReader.setDBEngine(dbEngine);
        return schemaSqlReader.readDDL(schemaFileName);
    }

    private List<DBTable> readSchemaFromMetadata(String schemaName) {
        return schemaMetadataReader.read(schemaName);
    }

    private Transformation mapTableToTransformation(DBTable table,
                                                    Set<String> columnToAnonymizerKeys,
                                                    Map<String, Set<String>> columnTranslationsMap,
                                                    boolean isIgnoreMissingIds,
                                                    Set<String> columnToAnonymizerUpperKeys) {
        log.info("Processing db table {}.{}...", table.getSchema(), table.getTable());

        Transformation transformation = new Transformation(table.getSchema(), table.getTable());

        Transformation finalTransformation = transformation;
        table.getColumns().forEach(schemaColumn ->
                setAnonymizerForColumn(schemaColumn, finalTransformation, columnToAnonymizerKeys,
                        columnToAnonymizerUpperKeys, columnTranslationsMap));
        transformation = detectAndSetIdColumns(transformation, table.getColumns(), isIgnoreMissingIds);
        return transformation;
    }

    private void setAnonymizerForColumn(Column schemaColumn, Transformation transformation,
                                        Set<String> columnToAnonymizerKeys,
                                        Set<String> columnToAnonymizerUpperKeys,
                                        Map<String, Set<String>> columnTranslationsMap) {
        String columnToAnonymizerKey = getColumnKey(schemaColumn.getName(), columnToAnonymizerKeys, columnToAnonymizerUpperKeys);
        if (columnToAnonymizerKey == null) {
            columnToAnonymizerKey = getTranslatedColumnKey(schemaColumn.getName(), columnTranslationsMap);
        }
        if (columnToAnonymizerKey != null) {
            String anonymizer = columnToAnonymizerTable.get(columnToAnonymizerKey);
            if (anonymizer != null) {
                transformation.getColumnToAnonymizerMap().put(schemaColumn.getName(),
                        columnToAnonymizerTable.get(columnToAnonymizerKey));
            } else {
                throw new RuntimeException("Error, column to anonymizer table doesn't contain mapping for " + columnToAnonymizerKey);
            }
        }
    }

    private String getTranslatedColumnKey(String schemaColumnName, Map<String, Set<String>> columnTranslationsMap) {
        return columnTranslationsMap.entrySet().stream().filter(columnTranslationsEntry -> {
            Set<String> columnTranslations = columnTranslationsEntry.getValue();
            Set<String> capitalizedTranslations = getOrSetInCache(columnTranslationsEntry.getKey(), columnTranslations);
            return getColumnKey(schemaColumnName, columnTranslations, capitalizedTranslations) != null;
        }).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    private Set<String> getOrSetInCache(String key, Set<String> columnTranslations) {
        Set<String> columnCapitalizedTranslations = capitalizedCache.get(key);
        if (columnCapitalizedTranslations == null) {
            columnCapitalizedTranslations = columnTranslations.stream().map(StringUtils::capitalize).collect(Collectors.toSet());
            capitalizedCache.put(key, columnCapitalizedTranslations);
        }
        return columnCapitalizedTranslations;
    }

    private String getColumnKey(String columnName, Set<String> anonymizerColumnNames, Set<String> anonymizerColumnCapitalizedNames) {
        String lowerCaseColumnName = columnName.toLowerCase();
        if (anonymizerColumnNames.contains(lowerCaseColumnName)){
            return lowerCaseColumnName;
        } else if (columnName.indexOf('_') > -1) {
            return findColumnSplittedBy(lowerCaseColumnName, anonymizerColumnNames, "_");
        } else if (columnName.indexOf('-') > -1) {
            return findColumnSplittedBy(lowerCaseColumnName, anonymizerColumnNames, "-");
        } else {
            return orCamelCaseCheckedColumnName(columnName, anonymizerColumnCapitalizedNames);
        }
    }

    private String findColumnSplittedBy(String lowerCaseColumnName, Set<String> anonymizerColumnNames, String delim) {
        Set<String> schemaColumnNameTokens = split(lowerCaseColumnName, delim);
        schemaColumnNameTokens.retainAll(anonymizerColumnNames);
        return schemaColumnNameTokens.stream().findFirst().orElse(null);
    }

    protected String orCamelCaseCheckedColumnName(String columnName, Set<String> anonymizerColumnUpperNames) {
        return anonymizerColumnUpperNames.stream().filter( anonymizerColumnUpperName ->
            capitalize(columnName).contains(anonymizerColumnUpperName)
        )
        .map(StringUtils::uncapitalize)
        .findFirst()
        .orElse(null);
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
                    .filter(column -> {
                        String lowerCaseColumn = column.toLowerCase();
                        return lowerCaseColumn.endsWith("id") || lowerCaseColumn.endsWith("code");
                    })
                    .findFirst().orElse(null);
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
            throw new RuntimeException(String.format("Error: can't autodetect id column for %s.%s. " +
                    "As option run the application with -iid argument.",
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
