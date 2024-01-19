package com.discreet.datamasking.autodetect;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ColumnToAnonymizerLoaderTest {
    ColumnToAnonymizerLoader loader = new ColumnToAnonymizerLoader();

    @Test
    public void testLoadMappings() {
        Map<String, String> mappings = loader.load();
        assertEquals(Map.of(
                        "name", "name",
                        "title", "name",
                        "address", "address",
                        "ccard", "ccard",
                        "personal", "pid",
                        "social_number", "pid",
                        "taxpayer_number", "pid",
                        "birthdate", "birthdate",
                        "postal", "post",
                        "post", "post"
                        ),
                mappings);
    }
}