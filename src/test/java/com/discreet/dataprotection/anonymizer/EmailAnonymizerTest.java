package com.discreet.dataprotection.anonymizer;

import org.junit.jupiter.api.Test;

class EmailAnonymizerTest extends BaseTest {
    EmailAnonymizer anonymizer = new EmailAnonymizer();

    @Test
    public void testAnonymize() {
        String input = "joe.doe@gmail.com";
        String output = anonymizer.anonymize(input);
        testMatch(output, input, "[a-z]{3}\\.[a-z]{3}@[a-z]{5}\\.com");
    }
}