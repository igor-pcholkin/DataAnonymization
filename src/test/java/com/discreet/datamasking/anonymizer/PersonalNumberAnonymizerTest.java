package com.discreet.datamasking.anonymizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonalNumberAnonymizerTest {
    PersonalNumberAnonymizer anonymizer = new PersonalNumberAnonymizer();

    @Test
    public void testAnonimizeOnlyDigits() {
        String input = "020976-11322";
        String output = anonymizer.anonymize(input);

        assertNotEquals (output, input);
        assertEquals(input.length(), output.length());
        assertTrue(output.matches("[0-9]{6}-[0-9]{5}"));
    }

    @Test
    public void testAnonimizeAlsoLetters() {
        String input = "020976-113A";
        String output = anonymizer.anonymize(input);

        assertNotEquals (output, input);
        assertEquals(input.length(), output.length());
        assertTrue(output.matches("[0-9]{6}-[0-9]{3}[A-Z]"));
    }

}