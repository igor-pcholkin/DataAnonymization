package com.discreet.datamasking.itest;

import com.discreet.datamasking.autodetect.Column;
import com.discreet.datamasking.autodetect.DBTable;
import com.discreet.datamasking.autodetect.SchemaMetadataReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchemaMetadataReaderITest extends BaseITest {
    @Autowired
    SchemaMetadataReader schemaMetadataReader;

    @Test
    public void testReadMetadata() {
        List<DBTable> actualMetadata = schemaMetadataReader.read("test");
        List<DBTable> expectedMetadata = List.of(
                new DBTable("test", "COMPANIES",
                        List.of(new Column("COMPANY_ID", "INTEGER"),
                                new Column("NAME", "CHARACTER VARYING(256)"),
                                new Column("ADDRESS", "CHARACTER VARYING(256)"))),
                new DBTable("test", "USERS",
                        List.of(new Column("ID", "INTEGER"),
                                new Column("NAME", "CHARACTER VARYING(256)"),
                                new Column("EMAIL", "CHARACTER VARYING(256)"),
                                new Column("ADDRESS", "CHARACTER VARYING(256)"),
                                new Column("BIRTHDATE", "DATE"),
                                new Column("SOCIALNUMBER", "CHARACTER(12)"),
                                new Column("CCARD", "CHARACTER(16)"),
                                new Column("POST_CODE", "CHARACTER VARYING(20)"))));
        assertEquals(expectedMetadata, actualMetadata);
    }

    @Test
    public void testReadUnknownSchema() {
        List<DBTable> actualMetadata = schemaMetadataReader.read("test2");
        List<DBTable> expectedMetadata = List.of();
        assertEquals(expectedMetadata, actualMetadata);
    }
}