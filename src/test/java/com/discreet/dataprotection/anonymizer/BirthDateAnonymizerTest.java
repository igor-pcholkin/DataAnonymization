package com.discreet.dataprotection.anonymizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BirthDateAnonymizerTest extends BaseTest {
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
        for (int i = 0; i < 10000; i++) {
            String input1 = LocalDate.now().toString();
            String output11 = anonymizer.anonymize(input1);
            testMatch(output11, input1, regex);

            String output12 = anonymizer.anonymize(input1);
            assertEquals(output11, output12);

            String input2 = LocalDate.now().plusDays(560).toString();
            String output2 = anonymizer.anonymize(input2);
            testMatch(output2, input2, regex);

            assertNotEquals(output2, output12);
        }
    }
}