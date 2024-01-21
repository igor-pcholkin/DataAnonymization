package com.discreet.datamasking.autodetect;

import com.discreet.datamasking.CommandLineArgs;
import com.discreet.datamasking.transformations.Transformation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransformationsAutoDetectorTest {
    TransformationsAutoDetector autoDetector = new TransformationsAutoDetector();

    @Test
    public void testAutodetectSchemaWithoutTranslations() {
        SchemaSqlReader schemaSqlReader = mock(SchemaSqlReader.class);
        when(schemaSqlReader.readDDL("schema.sql")).thenReturn(List.of(
                new DBTable("test", "users",
                        List.of(new Column("id", "INT"),
                                new Column("name", "VARCHAR(256)"),
                                new Column("passport", "VARCHAR(256")))

        ));

        ColumnTranslationsLoader columnTranslationsLoader = mock(ColumnTranslationsLoader.class);
        ColumnToAnonymizerLoader columnToAnonymizerLoader = mock(ColumnToAnonymizerLoader.class);

        Map<String, String> columnToAnonymizerMap = Map.of("name", "name",
                "passport", "pid");
        when(columnToAnonymizerLoader.getColumnToAnonymizerTable()).thenReturn(columnToAnonymizerMap);

        autoDetector.setColumnToAnonymizerLoader(columnToAnonymizerLoader);
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(columnTranslationsLoader);

        CommandLineArgs commandLineArgs = new CommandLineArgs();
        commandLineArgs.setSchemaFileName("schema.sql");

        List<Transformation> actualTransformations = autoDetector.autodetectSchema(commandLineArgs);
        List<Transformation> expectedTransformations = List.of(
                new Transformation("test", "users",
                        Map.of("name", "name", "passport", "pid"), List.of("id"))
        );

        assertEquals(expectedTransformations, actualTransformations);
    }

}