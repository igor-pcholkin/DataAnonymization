package com.discreet.dataprotection.itest;

import com.discreet.dataprotection.probe.ProbeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProbeServiceITest extends BaseITest {

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
