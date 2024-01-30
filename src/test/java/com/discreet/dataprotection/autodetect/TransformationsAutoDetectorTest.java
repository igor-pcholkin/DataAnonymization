package com.discreet.dataprotection.autodetect;

import com.discreet.dataprotection.CommandLineArgs;
import com.discreet.dataprotection.autodetect.Column;
import com.discreet.dataprotection.autodetect.ColumnToAnonymizerLoader;
import com.discreet.dataprotection.autodetect.ColumnTranslationsLoader;
import com.discreet.dataprotection.autodetect.DBTable;
import com.discreet.dataprotection.autodetect.SchemaSqlReader;
import com.discreet.dataprotection.autodetect.TransformationsAutoDetector;
import com.discreet.dataprotection.transformations.Transformation;
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

        autoDetector.setColumnToAnonymizerLoader(new ColumnToAnonymizerLoader());
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(columnTranslationsLoader);

        CommandLineArgs commandLineArgs = new CommandLineArgs();
        commandLineArgs.setSchemaFileName("schema.sql");

        List<Transformation> actualTransformations = autoDetector.autodetectTransformations(commandLineArgs);
        List<Transformation> expectedTransformations = List.of(
                new Transformation("test", "users",
                        Map.of("name", "name", "passport", "pid"), List.of("id"))
        );

        assertEquals(expectedTransformations, actualTransformations);
    }

    @Test
    public void testAutodetectSchemaWithTranslations() {
        SchemaSqlReader schemaSqlReader = mock(SchemaSqlReader.class);
        when(schemaSqlReader.readDDL("schema.sql")).thenReturn(List.of(
                new DBTable("test", "users",
                        List.of(new Column("user_id", "INT"),
                                new Column("geboortedatum", "DATE"),
                                new Column("henkilötunnus", "VARCHAR(256)"),
                                new Column("titl", "VARCHAR(256)"),
                                new Column("nodokļu_maksātāja_numurs", "VARCHAR(256)"),
                                new Column("kart", "CHAR(16)"),
                                new Column("adiresi", "VARCHAR(256)"),
                                new Column("poster", "VARCHAR(256)")))

        ));

        autoDetector.setColumnToAnonymizerLoader(new ColumnToAnonymizerLoader());
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(new ColumnTranslationsLoader());

        CommandLineArgs commandLineArgs = new CommandLineArgs();
        commandLineArgs.setSchemaFileName("schema.sql");

        List<Transformation> actualTransformations = autoDetector.autodetectTransformations(commandLineArgs);
        List<Transformation> expectedTransformations = List.of(
                new Transformation("test", "users",
                        Map.of("adiresi", "address",
                                "henkilötunnus", "pid",
                                "nodokļu_maksātāja_numurs", "pid",
                                "kart", "ccard",
                                "titl", "name",
                                "poster", "post",
                                "geboortedatum", "birthdate"
                ), List.of("user_id"))
        );

        assertEquals(expectedTransformations, actualTransformations);
    }

}