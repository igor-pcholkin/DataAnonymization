package com.discreet.dataprotection.transformations;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransformationsWriterTest {

    TransformationsLoader loader = new TransformationsLoader();
    @Test
    public void testWriteNonEmptyAnonimizers() throws IOException {
        List<Transformation> transformations = List.of(
                new Transformation("test", "users",
                        Map.of("name", "name", "passport", "pid"), List.of("id"))
        );
        TransformationsWriter writer = new TransformationsWriter();
        File outputFile = File.createTempFile("anonymizer", "gen_transformations.yaml");
        writer.write(transformations, outputFile);

        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);

        List<Transformation> transformations2 = loader.loadDefinitions(
                "src/test/resources/transformations.yaml");
    }

    @Test
    public void testWriteEmptyAnonimizers() throws IOException {
        List<Transformation> transformations = List.of(
                new Transformation("test", "users",
                        Map.of(), List.of("id"))
        );
        TransformationsWriter writer = new TransformationsWriter();
        File outputFile = File.createTempFile("anonymizer", "gen_transformations.yaml");
        writer.write(transformations, outputFile);

        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);

        List<Transformation> transformations2 = loader.loadDefinitions(
                "src/test/resources/transformations.yaml");
    }
}