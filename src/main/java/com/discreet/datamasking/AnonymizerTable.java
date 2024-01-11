package com.discreet.datamasking;

import com.discreet.datamasking.anonymizer.AddressAnonymizer;
import com.discreet.datamasking.anonymizer.Anonymizer;
import com.discreet.datamasking.anonymizer.BirthDateAnonymizer;
import com.discreet.datamasking.anonymizer.CreditCardAnonymizer;
import com.discreet.datamasking.anonymizer.EmailAnonymizer;
import com.discreet.datamasking.anonymizer.FullNameAnonymizer;
import com.discreet.datamasking.anonymizer.PersonalNumberAnonymizer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AnonymizerTable {
    private Map<String, Anonymizer> table = Map.of("name", new FullNameAnonymizer(),
            "email", new EmailAnonymizer(),
            "birthdate", new BirthDateAnonymizer(),
            "ccard", new CreditCardAnonymizer(),
            "address", new AddressAnonymizer(),
            "pid", new PersonalNumberAnonymizer());

    public Anonymizer getAnonymizer(String name) {
        return table.get(name);
    }
}
