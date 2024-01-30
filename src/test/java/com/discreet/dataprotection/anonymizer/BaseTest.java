package com.discreet.dataprotection.anonymizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTest {
    protected void testMatch(String output, String input, String regex) {
        assertNotEquals (output, input);
        assertEquals(input.length(), output.length());
        assertTrue(output.matches(regex));
    }

}
