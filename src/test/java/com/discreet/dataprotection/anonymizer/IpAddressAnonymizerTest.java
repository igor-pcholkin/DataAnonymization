package com.discreet.dataprotection.anonymizer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IpAddressAnonymizerTest extends BaseTest {
    IpAddressAnonymizer anonymizer = new IpAddressAnonymizer();

    @Test
    public void testIp4Anonymize() {
        String inputIPAddress = "192.168.0.1";
        String outputIPAddress = anonymizer.anonymize(inputIPAddress);
        String[] tokens = outputIPAddress.split("\\.");
        assertEquals(4, tokens.length);
        Arrays.stream(tokens).forEach(token ->
                assertTrue(Integer.parseInt(token) < 256));
    }

    @Test
    public void testIp6Anonymize() {
        String inputIPAddress = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String outputIPAddress = anonymizer.anonymize(inputIPAddress);
        String[] tokens = outputIPAddress.split(":");
        assertEquals(8, tokens.length);
        Arrays.stream(tokens).forEach(token -> {
            assertTrue(Integer.parseInt(token, 16) <= 65535);
            assertEquals(4, token.length());
        });
    }
}
