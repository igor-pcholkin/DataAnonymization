package com.discreet.datamasking.transformations;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TransformationsLoaderTest {
    TransformationsLoader loader = new TransformationsLoader();

    @Test
    public void testReadTransformations() {
        Transformations transformations = loader.loadDefinitions();

        List<Schema> schemas = transformations.getSchemas();
        assertEquals(1, schemas.size());

        assertEquals("test", schemas.get(0).getName());

        List<Table> tables = schemas.get(0).getTables();
        List<String> tableNames = tables.stream().map(table -> table.getName()).collect(Collectors.toList());
        assertEquals(List.of("users", "companies"), tableNames);

        List<Column> columnsUsers = tables.get(0).getColumns();
        assertEquals(List.of(
                new Column("name", "name"),
                new Column("email", "email"),
                new Column("address", "address"),
                new Column("birthdate", "birthdate"),
                new Column("socialNumber", "pid"),
                new Column("ccard", "ccard")
                ), columnsUsers);

        List<Column> columnsCompanies = tables.get(1).getColumns();
        assertEquals(List.of(
                new Column("name", "name"),
                new Column("address", "address")
        ), columnsCompanies);
    }

}