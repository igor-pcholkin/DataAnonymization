package com.discreet.dataprotection.anonymizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FullNameAnonymizerTest extends BaseTest {
    FullNameAnonymizer anonymizer = new FullNameAnonymizer();

    @Test
    public void testNameOnly() {
        String input = "John";
        String output11 = anonymizer.anonymize(input);

        testMatch(output11, input, "J[a-z]*");

        String output12 = anonymizer.anonymize(input);

        assertEquals(output11, output12);

        String input2 = "Michael";
        String output21 = anonymizer.anonymize(input2);

        testMatch(output21, input2, "M[a-z]*");

        String output22 = anonymizer.anonymize(input2);

        assertEquals(output21, output22);
    }

    @Test
    public void testNamePlusLastName() {
        String input = "John Smith";
        String output = anonymizer.anonymize(input);

        testMatch(output, input, "J[a-z]* S[a-z]*");
    }

    @Test
    public void testNamePlusLastNamePlusMiddleName() {
        String input = "John Paul Smith";
        String output = anonymizer.anonymize(input);

        testMatch(output, input, "J[a-z]* P[a-z]* S[a-z]*");
    }

    @Test
    public void testFullNameWithCaseCorrection() {
        String input = "JOHN paul sMITH";
        String output = anonymizer.anonymize(input);

        testMatch(output, input, "J[a-z]* P[a-z]* S[a-z]*");
    }

    @Test
    public void testInvalidFullName() {
        String input = ",John123Paul Smith";
        String output = anonymizer.anonymize(input);

        testMatch(output, input, "[A-Za-z]* S[a-z]*");
    }

}
