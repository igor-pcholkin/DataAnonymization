package com.discreet.dataprotection.autodetect;

import com.discreet.dataprotection.autodetect.ColumnToAnonymizerLoader;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ColumnToAnonymizerLoaderTest {
    ColumnToAnonymizerLoader loader = new ColumnToAnonymizerLoader();

    @Test
    public void testLoadMappings() {
        Map<String, String> loadedColumnToAnonymizerMap = loader.getColumnToAnonymizerTable();
        Map<String, String> expectedColumnToAnonymizerMap = new HashMap<>();
        expectedColumnToAnonymizerMap.put("name", "name");
        expectedColumnToAnonymizerMap.put("title", "name");
        expectedColumnToAnonymizerMap.put("address", "address");
        expectedColumnToAnonymizerMap.put("ccard", "ccard");
        expectedColumnToAnonymizerMap.put("personal", "pid");
        expectedColumnToAnonymizerMap.put("social_number", "pid");
        expectedColumnToAnonymizerMap.put("taxpayer_number", "pid");
        expectedColumnToAnonymizerMap.put("passport", "pid");
        expectedColumnToAnonymizerMap.put("birthdate", "birthdate");
        expectedColumnToAnonymizerMap.put("postal", "post");
        expectedColumnToAnonymizerMap.put("post", "post");
        expectedColumnToAnonymizerMap.put("zip", "post");
        expectedColumnToAnonymizerMap.put("email", "email");
        expectedColumnToAnonymizerMap.put("ip", "ip");

        assertEquals(expectedColumnToAnonymizerMap, loadedColumnToAnonymizerMap);
    }
}