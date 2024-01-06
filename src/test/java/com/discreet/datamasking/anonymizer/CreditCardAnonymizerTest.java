package com.discreet.datamasking.anonymizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreditCardAnonymizerTest {
    CreditCardAnonymizer anonymizer = new CreditCardAnonymizer();

    @Test
    public void testAnonimize() {
        String input = "5209761132208795";
        String output = anonymizer.anonymize(input);

        assertNotEquals (output, input);
        assertEquals(input.length(), output.length());
        assertTrue(output.matches("[0-9]{16}"));
    }
}