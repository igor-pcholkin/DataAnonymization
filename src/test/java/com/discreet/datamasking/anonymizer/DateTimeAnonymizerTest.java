package com.discreet.datamasking.anonymizer;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DateTimeAnonymizerTest extends BaseTest {
    @Test
    public void testWithDefaultRange() {
        DateTimeAnonymizer anonymizer = new DateTimeAnonymizer();
        testNIterations(anonymizer, "[1-2][0-9]{3}-[0-1][0-9]-[0-3][0-9]T[0-9]{2}:[0-9]{2}:[0-9]{2}");
    }

    @Test
    public void testWithCustomRange() {
        DateTimeAnonymizer anonymizer = new DateTimeAnonymizer(
                LocalDateTime.of(1950, 1, 1, 0, 0, 0),
                LocalDateTime.of(1960, 1, 1, 0, 0, 0)
        );
        testNIterations(anonymizer, "195[0-9]-[0-1][0-9]-[0-3][0-9]T[0-9]{2}:[0-9]{2}:[0-9]{2}");
    }

    private void testNIterations(DateTimeAnonymizer anonymizer, String regex) {
        for (int i = 0; i < 10000; i++) {
            String input1 = createCurrentDateTime().toString();
            String output11 = anonymizer.anonymize(input1);
            testMatch(output11, input1, regex);

            String output12 = anonymizer.anonymize(input1);
            assertEquals(output11, output12);

            String input2 = createCurrentDateTime()
                    .plusSeconds(560 * 24 * 3600)
                    .toString();
            String output2 = anonymizer.anonymize(input2);
            testMatch(output2, input2, regex);

            assertNotEquals(output2, output12);
        }
    }

    @Override
    protected void testMatch(String output, String input, String regex) {
        assertNotEquals (output, input);
        assertTrue(input.length() == 16 || input.length() == 19);
        assertTrue(output.length() == 16 || output.length() == 19);
        assertTrue(output.matches(regex));
    }

    private LocalDateTime createCurrentDateTime() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}