package com.discreet.dataprotection.autodetect;

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
                                new Column("passport", "VARCHAR(256"),
                                new Column("post_code", "VARCHAR(256"),
                                new Column("credit_card", "VARCHAR(256"),
                                new Column("ip", "VARCHAR(256")))

        ));

        ColumnTranslationsLoader columnTranslationsLoader = mock(ColumnTranslationsLoader.class);

        autoDetector.setColumnToAnonymizerLoader(new ColumnToAnonymizerLoader());
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(columnTranslationsLoader);

        List<Transformation> actualTransformations = autoDetector.autodetectTransformations(
                "schema.sql", null, false, null, null);
        List<Transformation> expectedTransformations = List.of(
                new Transformation("test", "users",
                        Map.of("name", "name",
                                "passport", "pid",
                                "post_code", "post",
                                    "credit_card", "ccard",
                                "ip", "ip"), List.of("id"))
        );

        assertEquals(expectedTransformations, actualTransformations);
    }

    @Test
    public void testAutodetectSchemaWithoutTranslationsMixedCase() {
        SchemaSqlReader schemaSqlReader = mock(SchemaSqlReader.class);
        when(schemaSqlReader.readDDL("schema.sql")).thenReturn(List.of(
                new DBTable("test", "users",
                        List.of(new Column("Id", "INT"),
                                new Column("Name", "VARCHAR(256)"),
                                new Column("Passport", "VARCHAR(256"),
                                new Column("Post_code", "VARCHAR(256"),
                                new Column("Ip", "VARCHAR(256")))

        ));

        ColumnTranslationsLoader columnTranslationsLoader = mock(ColumnTranslationsLoader.class);

        autoDetector.setColumnToAnonymizerLoader(new ColumnToAnonymizerLoader());
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(columnTranslationsLoader);

        List<Transformation> actualTransformations = autoDetector.autodetectTransformations(
                "schema.sql", null, false, null, null);
        List<Transformation> expectedTransformations = List.of(
                new Transformation("test", "users",
                        Map.of("Name", "name",
                                "Passport", "pid",
                                "Post_code", "post",
                                "Ip", "ip"), List.of("id"))
        );

        assertEquals(expectedTransformations, actualTransformations);
    }

    @Test
    public void testAutodetectSchemaWithoutTranslationsCamelCase() {
        SchemaSqlReader schemaSqlReader = mock(SchemaSqlReader.class);
        when(schemaSqlReader.readDDL("schema.sql")).thenReturn(List.of(
                new DBTable("test", "users",
                        List.of(new Column("mainId", "INT"),
                                new Column("nameOfUser", "VARCHAR(256)"),
                                new Column("userPassport", "VARCHAR(256"),
                                new Column("postCode", "VARCHAR(256"),
                                new Column("ipAddress", "VARCHAR(256")))
        ));

        ColumnTranslationsLoader columnTranslationsLoader = mock(ColumnTranslationsLoader.class);

        autoDetector.setColumnToAnonymizerLoader(new ColumnToAnonymizerLoader());
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(columnTranslationsLoader);

        List<Transformation> actualTransformations = autoDetector.autodetectTransformations(
                "schema.sql", null, false, null, null);
        List<Transformation> expectedTransformations = List.of(
                new Transformation("test", "users",
                        Map.of("nameOfUser", "name",
                                "userPassport", "pid",
                                "postCode", "post",
                                "ipAddress", "ip"), List.of("mainId"))
        );

        assertEquals(expectedTransformations, actualTransformations);
    }

    @Test
    public void testAutodetectSchemaWithoutTranslationsSplitByMinus() {
        SchemaSqlReader schemaSqlReader = mock(SchemaSqlReader.class);
        when(schemaSqlReader.readDDL("schema.sql")).thenReturn(List.of(
                new DBTable("test", "users",
                        List.of(new Column("main-id", "INT"),
                                new Column("name-of-user", "VARCHAR(256)"),
                                new Column("user-passport", "VARCHAR(256"),
                                new Column("post-code", "VARCHAR(256"),
                                new Column("ip-address", "VARCHAR(256")))
        ));

        ColumnTranslationsLoader columnTranslationsLoader = mock(ColumnTranslationsLoader.class);

        autoDetector.setColumnToAnonymizerLoader(new ColumnToAnonymizerLoader());
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(columnTranslationsLoader);

        List<Transformation> actualTransformations = autoDetector.autodetectTransformations(
                "schema.sql", null, false, null, null);
        List<Transformation> expectedTransformations = List.of(
                new Transformation("test", "users",
                        Map.of("name-of-user", "name",
                                "user-passport", "pid",
                                "post-code", "post",
                                "ip-address", "ip"), List.of("main-id"))
        );

        assertEquals(expectedTransformations, actualTransformations);
    }

    @Test
    public void testAutodetectSchemaWithoutTranslationsSplitByUnderscore() {
        SchemaSqlReader schemaSqlReader = mock(SchemaSqlReader.class);
        when(schemaSqlReader.readDDL("schema.sql")).thenReturn(List.of(
                new DBTable("test", "users",
                        List.of(new Column("main_id", "INT"),
                                new Column("name_of_user", "VARCHAR(256)"),
                                new Column("user_passport", "VARCHAR(256"),
                                new Column("post_code", "VARCHAR(256"),
                                new Column("ip_address", "VARCHAR(256")))
        ));

        ColumnTranslationsLoader columnTranslationsLoader = mock(ColumnTranslationsLoader.class);

        autoDetector.setColumnToAnonymizerLoader(new ColumnToAnonymizerLoader());
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(columnTranslationsLoader);

        List<Transformation> actualTransformations = autoDetector.autodetectTransformations(
                "schema.sql", null, false, null, null);
        List<Transformation> expectedTransformations = List.of(
                new Transformation("test", "users",
                        Map.of("name_of_user", "name",
                                "user_passport", "pid",
                                "post_code", "post",
                                "ip_address", "ip"), List.of("main_id"))
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
                                new Column("poster", "VARCHAR(256)"),
                                new Column("rkt_otsikko", "VARCHAR(256)"),
                                new Column("kav_kav", "VARCHAR(256)"),
                                new Column("postali_code", "VARCHAR(256")))

        ));

        autoDetector.setColumnToAnonymizerLoader(new ColumnToAnonymizerLoader());
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(new ColumnTranslationsLoader());

        List<Transformation> actualTransformations = autoDetector.autodetectTransformations("schema.sql",
                null, false, null, null);
        List<Transformation> expectedTransformations = List.of(
                new Transformation("test", "users",
                        Map.of("adiresi", "address",
                                "henkilötunnus", "pid",
                                "nodokļu_maksātāja_numurs", "pid",
                                "kart", "ccard",
                                "titl", "name",
                                "poster", "post",
                                "geboortedatum", "birthdate",
                                "rkt_otsikko", "name",
                                "postali_code", "post"
                ), List.of("user_id"))
        );

        assertEquals(expectedTransformations, actualTransformations);
    }

    @Test
    public void testAutodetectSchemaWithTranslationsMixedCase() {
        SchemaSqlReader schemaSqlReader = mock(SchemaSqlReader.class);
        when(schemaSqlReader.readDDL("schema.sql")).thenReturn(List.of(
                new DBTable("test", "users",
                        List.of(new Column("user_id", "INT"),
                                new Column("Geboortedatum", "DATE"),
                                new Column("Henkilötunnus", "VARCHAR(256)"),
                                new Column("Titl", "VARCHAR(256)"),
                                new Column("Nodokļu_maksātāja_numurs", "VARCHAR(256)"),
                                new Column("Kart", "CHAR(16)"),
                                new Column("Adiresi", "VARCHAR(256)"),
                                new Column("Poster", "VARCHAR(256)"),
                                new Column("Rkt_otsikko", "VARCHAR(256)"),
                                new Column("Kav_kav", "VARCHAR(256)"),
                                new Column("Postali_code", "VARCHAR(256")))

        ));

        autoDetector.setColumnToAnonymizerLoader(new ColumnToAnonymizerLoader());
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(new ColumnTranslationsLoader());

        List<Transformation> actualTransformations = autoDetector.autodetectTransformations("schema.sql",
                null, false, null, null);
        List<Transformation> expectedTransformations = List.of(
                new Transformation("test", "users",
                        Map.of("Adiresi", "address",
                                "Henkilötunnus", "pid",
                                "Nodokļu_maksātāja_numurs", "pid",
                                "Kart", "ccard",
                                "Titl", "name",
                                "Poster", "post",
                                "Geboortedatum", "birthdate",
                                "Rkt_otsikko", "name",
                                "Postali_code", "post"
                        ), List.of("user_id"))
        );

        assertEquals(expectedTransformations, actualTransformations);
    }

    //@Test()
    public void testAutodetectSchemaWithTranslationsCamelCase() {
        SchemaSqlReader schemaSqlReader = mock(SchemaSqlReader.class);
        when(schemaSqlReader.readDDL("schema.sql")).thenReturn(List.of(
                new DBTable("test", "users",
                        List.of(new Column("userId", "INT"),
                                new Column("GeboorteDatum", "DATE"),
                                new Column("HenkilöTunnus", "VARCHAR(256)"),
                                new Column("Titl", "VARCHAR(256)"),
                                new Column("NodokļuMaksātājaNumurs", "VARCHAR(256)"),
                                new Column("KartKart", "CHAR(16)"),
                                new Column("UserAdiresi", "VARCHAR(256)"),
                                new Column("Poster", "VARCHAR(256)"),
                                new Column("RktOtsikko", "VARCHAR(256)"),
                                new Column("KavKav", "VARCHAR(256)"),
                                new Column("PostaliCode", "VARCHAR(256")))

        ));

        autoDetector.setColumnToAnonymizerLoader(new ColumnToAnonymizerLoader());
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(new ColumnTranslationsLoader());

        List<Transformation> actualTransformations = autoDetector.autodetectTransformations("schema.sql",
                null, false, null, null);
        List<Transformation> expectedTransformations = List.of(
                new Transformation("test", "users",
                        Map.of("UserAdiresi", "address",
                                "HenkilöTunnus", "pid",
                                "NodokļuMaksātājaNumurs", "pid",
                                "KartKart", "ccard",
                                "Titl", "name",
                                "Poster", "post",
                                "Geboortedatum", "birthdate",
                                "RktOtsikko", "name",
                                "PostaliCode", "post"
                        ), List.of("user_id"))
        );

        assertEquals(expectedTransformations, actualTransformations);
    }

    @Test
    public void testAutodetectTableWithoutId() {
        SchemaSqlReader schemaSqlReader = mock(SchemaSqlReader.class);
        when(schemaSqlReader.readDDL("schema.sql")).thenReturn(List.of(
                new DBTable("test", "users",
                        List.of(
                                new Column("name", "VARCHAR(256)"),
                                new Column("passport", "VARCHAR(256")))

        ));

        ColumnTranslationsLoader columnTranslationsLoader = mock(ColumnTranslationsLoader.class);

        autoDetector.setColumnToAnonymizerLoader(new ColumnToAnonymizerLoader());
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(columnTranslationsLoader);

        try {
            List<Transformation> actualTransformations = autoDetector.autodetectTransformations("schema.sql",
                    null, false, null, null);
            fail();
        } catch (RuntimeException ex) {
            assertEquals("Error: can't autodetect id column for test.users", ex.getMessage());
        }
    }

    @Test
    public void testAutodetectIgnoreTableWithoutId() {
        SchemaSqlReader schemaSqlReader = mock(SchemaSqlReader.class);
        when(schemaSqlReader.readDDL("schema.sql")).thenReturn(List.of(
                new DBTable("test", "users",
                        List.of(
                                new Column("name", "VARCHAR(256)"),
                                new Column("passport", "VARCHAR(256")))

        ));

        ColumnTranslationsLoader columnTranslationsLoader = mock(ColumnTranslationsLoader.class);

        autoDetector.setColumnToAnonymizerLoader(new ColumnToAnonymizerLoader());
        autoDetector.setSchemaSqlReader(schemaSqlReader);
        autoDetector.setColumnTranslationsLoader(columnTranslationsLoader);

        List<Transformation> actualTransformations = autoDetector.autodetectTransformations("schema.sql",
                null, true, null, null);
        assertEquals(List.of(), actualTransformations);
    }
}