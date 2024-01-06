package com.discreet.datamasking.anonymizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PersonalNumberAnonymizerTest extends BaseTest {
    PersonalNumberAnonymizer anonymizer = new PersonalNumberAnonymizer();

    @Test
    public void testAnonimizeOnlyDigits() {
        String input = "020976-11322";
        String output11 = anonymizer.anonymize(input);

        testMatch (output11, input, "[0-9]{6}-[0-9]{5}");

        String output12 = anonymizer.anonymize(input);

        assertEquals(output12, output11);

        String input2 = "080323-12325";
        String output2 = anonymizer.anonymize(input2);

        testMatch (output2, input2, "[0-9]{6}-[0-9]{5}");
        assertNotEquals(output11, output2);
    }

    @Test
    public void testAnonimizeAlsoLetters() {
        String input = "020976-113A";
        String output11 = anonymizer.anonymize(input);

        testMatch (output11, input, "[0-9]{6}-[0-9]{3}[A-Z]");

        String output12 = anonymizer.anonymize(input);

        assertEquals(output12, output11);

        String input2 = "080323-123B";
        String output2 = anonymizer.anonymize(input2);

        testMatch (output2, input2, "[0-9]{6}-[0-9]{3}[A-Z]");
        assertNotEquals(output11, output2);
    }

}