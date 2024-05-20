package com.discreet.dataprotection.autodetect;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchemaSqlReaderTest {
    SchemaSqlReader schemaSqlReader = new SchemaSqlReader();

    @Test
    public void testLoadSchema() {
        schemaSqlReader.setDefaultSchema("test2");
        schemaSqlReader.setDBEngine("mysql");
        List<DBTable> schema = schemaSqlReader.readDDL("src/test/resources/com/discreet/dataprotection/itest/schema.sql");
        String actualResult = schema.toString();
        String expectedResult = "[DBTable{schema='test2', table='USERS', columns=[Column{name='id', type='INTEGER'}, " +
                "Column{name='name', type='VARCHAR'}, Column{name='email', type='VARCHAR'}, " +
                "Column{name='address', type='VARCHAR'}, Column{name='birthdate', type='DATE'}, " +
                "Column{name='socialNumber', type='CHAR'}, Column{name='ccard', type='CHAR'}, " +
                "Column{name='post_code', type='VARCHAR'}]}, " +

                "DBTable{schema='test2', table='COMPANIES', columns=[Column{name='company_id', type='INTEGER'}, " +
                "Column{name='name', type='VARCHAR'}, Column{name='address', type='VARCHAR'}]}, " +

                "DBTable{schema='test2', table='LOGS', columns=[Column{name='id', type='INTEGER'}, " +
                "Column{name='user_id', type='INTEGER'}, Column{name='ipAddress', type='VARCHAR'}]}]";
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testExceptionForNoShemaName() {
        schemaSqlReader.setDefaultSchema(null);
        schemaSqlReader.setDBEngine("mysql");
        try {
            schemaSqlReader.readDDL("src/test/resources/com/discreet/dataprotection/itest/schema.sql");
            fail();
        } catch (RuntimeException ex) {
            assertEquals("No schema name is found for table USERS, please use -dsn (--defaultSchemaName) command line argument to set a default one.", ex.getMessage());
        }
    }

    @Test
    public void testDBEngineNotSet() {
        schemaSqlReader.setDefaultSchema("test2");
        assertThrows(RuntimeException.class, () ->
                schemaSqlReader.readDDL("src/test/resources/com/discreet/dataprotection/itest/schema.sql"));
    }

    @Test
    public void testIncorrectDDL() {
        schemaSqlReader.setDefaultSchema("test2");
        schemaSqlReader.setDBEngine("mysql");
        assertThrows(RuntimeException.class, () ->
                schemaSqlReader.readDDL("src/test/resources/transformations.yaml"));
    }
}