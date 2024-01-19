package com.discreet.datamasking.anonymizer;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class PostCodeAnonymizerTest extends BaseTest {
    @Test
    public void testAnonymize() {
        Set<String> postCodes = new TreeSet<>();
        postCodes.add("12345-1234");
        postCodes.add("23456-0987");
        postCodes.add("34556-5455");
        postCodes.add("09845-4563");
        postCodes.add("12954-3455");
        PostCodeAnonymizer anonymizer = new PostCodeAnonymizer(postCodes);

        String output = anonymizer.anonymize("12345-1234");
        assertEquals("09845-4563", output);

        String output2 = anonymizer.anonymize("23456-0987");
        assertEquals("12954-3455", output2);

        String output3 = anonymizer.anonymize("34556-5455");
        assertEquals("23456-0987", output3);

        String output4 = anonymizer.anonymize("09845-4563");
        assertEquals("12345-1234", output4);

    }
}