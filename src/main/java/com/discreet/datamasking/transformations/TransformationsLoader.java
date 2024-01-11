package com.discreet.datamasking.transformations;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransformationsLoader {
    public List<Transformation> loadDefinitions() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("transformations.yaml");
        Map<String, Object> yamlTree = yaml.load(inputStream);
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
            List<Transformation> transformationsForTable = createTransofmationsForTable(schema, table,
                    (Map<String, Object>) tableMap.get(table));
            transformations.addAll(transformationsForTable);
        }
        return transformations;
    }

    private List<Transformation> createTransofmationsForTable(String schema, String table, Map<String, Object> columnMap) {
        List<Transformation> transformations = new ArrayList<>();
        for (String column: columnMap.keySet()) {
            Transformation transformation = new Transformation(schema, table, column, (String) columnMap.get(column));
            transformations.add(transformation);
        }
        return transformations;
    }
}
