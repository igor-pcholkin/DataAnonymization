package com.discreet.datamasking.itest;

import com.discreet.datamasking.probe.ProbeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({"schema.sql", "test-data.sql"})
@SpringBootTest(classes = CustomApplication.class)
@TestPropertySource(locations = "/application-h2.properties")
public class ProbeServiceITest {

    @Autowired
    private ProbeService probeService;

    @Test
    public void testGetAllPostCodeValuesFromDB() {
        Set<String> results = probeService.probe("USERS", "post_code");
        assertEquals(Set.of("10022-SHOE", "33701-4313", "04536-6547"),
                results);
    }

    @Test
    public void testGetNCodeValuesFromDB() {
        Set<String> results = probeService.probe("USERS", "post_code", 2);
        assertEquals(Set.of("10022-SHOE", "33701-4313"),
                results);
    }
}
