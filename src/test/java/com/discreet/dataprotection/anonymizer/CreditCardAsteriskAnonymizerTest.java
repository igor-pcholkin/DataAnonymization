package com.discreet.dataprotection.anonymizer;

import com.discreet.dataprotection.anonymizer.CreditCardAsteriskAnonymizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardAsteriskAnonymizerTest {
    @Test
    public void testAnonymizeWithDefaultNumberOfTrailingCharsToMask() {
        CreditCardAsteriskAnonymizer anonymizer = new CreditCardAsteriskAnonymizer();

        String input = "5209761132208795";
        String output = anonymizer.anonymize(input);

        assertEquals("520976113220****", output);
    }

    @Test
    public void testAnonymizeWith10TrailingCharsToMask() {
        CreditCardAsteriskAnonymizer anonymizer = new CreditCardAsteriskAnonymizer(10);

        String input = "5209761132208795";
        String output = anonymizer.anonymize(input);

        assertEquals("520976**********", output);
    }

}