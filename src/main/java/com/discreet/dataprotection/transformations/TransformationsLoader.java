package com.discreet.dataprotection.transformations;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransformationsLoader {
    private static final String KEY_ANONYMIZERS = "anonymizers";
    private static final String KEY_IDS = "ids";

    public List<Transformation> loadDefinitions(String transformationsFile) {
        Yaml yaml = new Yaml();
        Map<String, Object> yamlTree;
        if (transformationsFile == null) {
            transformationsFile = "transformations.yaml";
        }
        try (InputStream inputStream = new FileInputStream(transformationsFile)) {
            yamlTree = yaml.load(inputStream);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot load transformations file: " + transformationsFile +
                    ", please use the -tfn (--transformationsFileName) option to set the path.");
        }
        return linearilizeTransformations(yamlTree);
    }

    private List<Transformation> linearilizeTransformations(Map<String, Object> yamlTree) {
        List<Transformation> transformations = new ArrayList<>();
        for (String schema: yamlTree.keySet()) {
            List<Transformation> transformationsForSchema = createTransformationsForSchema(schema,
                    (Map<String, Object>) yamlTree.get(schema));
            transformations.addAll(transformationsForSchema);
        }
        return transformations;
    }

    private List<Transformation> createTransformationsForSchema(String schema, Map<String, Object> tableMap) {
        List<Transformation> transformations = new ArrayList<>();
        for (String table: tableMap.keySet()) {
            Transformation transformation = createTransformationForTable(schema, table,
                    (Map<String, Object>) tableMap.get(table));
            transformations.add(transformation);
        }
        return transformations;
    }

    private Transformation createTransformationForTable(String schema, String table, Map<String, Object> tableEntryMap) {
        Transformation transformation = new Transformation(schema, table);
        for (Map.Entry<String, Object> tableEntry: tableEntryMap.entrySet()) {
            if (tableEntry.getKey().equals(KEY_ANONYMIZERS)) {
                transformation.setColumnToAnonymizerMap ((Map<String, String>) tableEntry.getValue());
            } else if (tableEntry.getKey().equals(KEY_IDS)) {
                transformation.setIdColumns ((List<String>) tableEntry.getValue());
            }
        }
        return transformation;
    }

}
