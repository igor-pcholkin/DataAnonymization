package com.discreet.datamasking.anonymizer;

import com.discreet.datamasking.anonymizer.name.FullNameMildAnonymizer;
import com.discreet.datamasking.anonymizer.name.FullNameRandomAnonymizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FullNameRandomAnonymizerTest {
    FullNameRandomAnonymizer anonymizer = new FullNameRandomAnonymizer();

    @Test
    public void testNameOnly() {
        String input = "John";
        String output = anonymizer.anonymize(input);

        assertNotEquals (output, input);
        assertEquals(input.length(), output.length());
        assertTrue(output.matches("[A-Z][a-z]*"));
    }

    @Test
    public void testNamePlusLastName() {
        String input = "John Smith";
        String output = anonymizer.anonymize(input);

        assertNotEquals (output, input);
        assertEquals(input.length(), output.length());
        assertTrue(output.matches("[A-Z][a-z]* [A-Z][a-z]*"));
        assertFalse(output.matches("J[a-z]* S[a-z]*"));
    }

    @Test
    public void testNamePlusLastNamePlusMiddleName() {
        String input = "John Paul Smith";
        String output = anonymizer.anonymize(input);

        assertNotEquals (output, input);
        assertEquals(input.length(), output.length());
        assertTrue(output.matches("[A-Z][a-z]* [A-Z][a-z]* [A-Z][a-z]*"));
        assertFalse(output.matches("J[a-z]* P[a-z]* S[a-z]*"));
    }

    @Test
    public void testFullNameWithCaseCorrection() {
        String input = "JOHN paul sMITH";
        String output = anonymizer.anonymize(input);

        assertNotEquals (output, input);
        assertEquals(input.length(), output.length());
        assertTrue(output.matches("[A-Z][a-z]* [A-Z][a-z]* [A-Z][a-z]*"));
        assertFalse(output.matches("J[a-z]* P[a-z]* S[a-z]*"));
    }

    @Test
    public void testInvalidFullName() {
        String input = ",John123Paul Smith";
        String output = anonymizer.anonymize(input);

        assertNotEquals (output, input);
        assertEquals(input.length(), output.length());
        assertTrue(output.matches("[A-Za-z]* [A-Z][a-z]*"));
    }
}
