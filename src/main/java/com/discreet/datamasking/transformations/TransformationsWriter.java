package com.discreet.datamasking.transformations;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
public class TransformationsWriter {
    public void write(List<Transformation> transformations, File file) {
        Map<String, Object> tree = generateTree(transformations);
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Representer representer = new Representer(options);
        representer.addClassTag(TransformationsFileEntry.class, Tag.MAP);

        Yaml yaml = new Yaml(representer, options);
        try {
            yaml.dump(tree, new FileWriter(file));
        } catch (IOException e) {
            throw new RuntimeException("Can't write transformations to file" + e.getMessage());
        }
    }

    private Map<String, Object> generateTree(List<Transformation> transformations) {
        Map<String, List<Transformation>> transformationsGroupedBySchema = transformations.stream()
                .collect(groupingBy(Transformation::getSchema));
        Map<String, Object> tree = transformationsGroupedBySchema.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> getTransformationsBySchemaAndTable(e.getValue()) ));
        return tree;
    }

    private Map<String, TransformationsFileEntry> getTransformationsBySchemaAndTable
            (List<Transformation> sameSchemaTransformations) {
        Map<String, List<Transformation>> transformationsGroupedByTable = sameSchemaTransformations.stream()
                .collect(groupingBy(Transformation::getTable));
        return transformationsGroupedByTable.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    Transformation transformation = e.getValue().stream().findFirst().orElse(null);
                    return new TransformationsFileEntry(transformation.getColumnToAnonymizerMap(),
                            transformation.getIdColumns());
                }));
    }
}
