package com.discreet.datamasking.anonymizer;

import org.junit.jupiter.api.Test;

class AddressAnonymizerTest extends BaseTest {
    AddressAnonymizer anonymizer = new AddressAnonymizer();

    @Test
    public void testAnonnymize() {
        String input = "1 Arcadia Lane\n" +
                "Annandale, VA 22003";
        String output = anonymizer.anonymize(input);
        testMatch(output, input, "[0-9] [A-Z][a-z]{6} [A-Z][a-z]{3}\n[A-Z][a-z]{8}, [A-Z]{2} [0-9]{5}");
    }
}