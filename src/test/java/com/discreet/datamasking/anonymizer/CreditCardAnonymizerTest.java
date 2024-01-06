package com.discreet.datamasking.anonymizer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CreditCardAnonymizerTest extends BaseTest {
    CreditCardAnonymizer anonymizer = new CreditCardAnonymizer();

    @Test
    public void testAnonimize() {
        String input = "5209761132208795";
        String output11 = anonymizer.anonymize(input);

        testMatch (output11, input, "[0-9]{16}");

        String output12 = anonymizer.anonymize(input);

        assertEquals(output12, output11);

        String input2 = "4909761132208793";
        String output2 = anonymizer.anonymize(input2);

        testMatch (output2, input2, "[0-9]{16}");
        assertNotEquals(output11, output2);
    }
}