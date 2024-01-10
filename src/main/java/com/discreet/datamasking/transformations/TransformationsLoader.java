package com.discreet.datamasking.transformations;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class TransformationsLoader {
    public Transformations loadDefinitions() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("transformations.yaml");
        Map<String, Object> yamlTree = yaml.load(inputStream);
        Transformations transformations = parseTransformations(yamlTree);

        return transformations;
    }

    private Transformations parseTransformations(Map<String, Object> yamlTree) {
        Transformations transformations = new Transformations();
        for (String schemaName: yamlTree.keySet()) {
            Schema schema = new Schema(schemaName);
            addTables(schema, (Map<String, Object>) yamlTree.get(schemaName));

            transformations.addSchema(schema);
        }
        return transformations;
    }

    private void addTables(Schema schema, Map<String, Object> tableMap) {
        for (String tableName: tableMap.keySet()) {
            Table table = new Table(tableName);
            schema.addTable(table);
            addColumns(table, (Map<String, Object>) tableMap.get(tableName));
        }
    }

    private void addColumns(Table table, Map<String, Object> columnMap) {
        for (String colunmName: columnMap.keySet()) {
            Column column = new Column(colunmName, (String) columnMap.get(colunmName));
            table.addColumn(column);
        }
    }
}
