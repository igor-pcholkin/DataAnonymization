package com.discreet.datamasking.anonymizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonalNumberAnonymizerTest {
    PersonalNumberAnonymizer anonymizer = new PersonalNumberAnonymizer();

    @Test
    public void testAnonimize() {
        String input = "020976-11322";
        String output = anonymizer.anonymize(input);

        assertNotEquals (output, input);
        assertEquals(input.length(), output.length());
        assertTrue(output.matches("[0-9]{6}-[0-9]{5}"));
    }
}