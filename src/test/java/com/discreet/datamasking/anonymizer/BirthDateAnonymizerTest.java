package com.discreet.datamasking.anonymizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

class BirthDateAnonymizerTest {
    @Test
    public void testWithDefaultRange() {
        BirthDateAnonymizer anonymizer = new BirthDateAnonymizer();
        testNIterations(anonymizer, "[1-2][0-9]{3}-[0-1][0-9]-[0-3][0-9]");
    }

    @Test
    public void testWithCustomRange() {
        BirthDateAnonymizer anonymizer = new BirthDateAnonymizer(
                LocalDate.of(1950, 1, 1),
                LocalDate.of(1960, 1, 1)
        );
        testNIterations(anonymizer, "195[0-9]-[0-1][0-9]-[0-3][0-9]");
    }

    private void testNIterations(BirthDateAnonymizer anonymizer, String regex) {
        String input = LocalDate.now().toString();

        for (int i = 0; i < 10000; i++) {
            String output = anonymizer.anonymize(input);

            assertNotEquals(output, input);
            assertEquals(input.length(), output.length());
            if (!output.matches(regex)) {
                fail("Assertion failed: " + output);
            }
        }
    }
}