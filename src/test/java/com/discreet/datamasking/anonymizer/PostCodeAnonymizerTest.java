package com.discreet.datamasking.anonymizer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostCodeAnonymizerTest extends BaseTest {
    @Test
    public void testAnonymize() {
        PostCodeAnonymizer anonymizer = new PostCodeAnonymizer(List.of(
                "12345-1234",
                "23456-0987",
                "34556-5455",
                "09845-4563",
                "12954-3455"
        ));

        String output = anonymizer.anonymize("12345-1234");
        assertEquals("12345-1234", output);

        String output2 = anonymizer.anonymize("23456-0987");
        assertEquals("34556-5455", output2);

        String output3 = anonymizer.anonymize("34556-5455");
        assertEquals("09845-4563", output3);

        String output4 = anonymizer.anonymize("09845-4563");
        assertEquals("23456-0987", output4);

    }
}