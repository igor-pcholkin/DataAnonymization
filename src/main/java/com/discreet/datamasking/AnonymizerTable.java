package com.discreet.datamasking;

import com.discreet.datamasking.anonymizer.AddressAnonymizer;
import com.discreet.datamasking.anonymizer.Anonymizer;
import com.discreet.datamasking.anonymizer.BirthDateAnonymizer;
import com.discreet.datamasking.anonymizer.CreditCardAnonymizer;
import com.discreet.datamasking.anonymizer.EmailAnonymizer;
import com.discreet.datamasking.anonymizer.FullNameAnonymizer;
import com.discreet.datamasking.anonymizer.PersonalNumberAnonymizer;
import com.discreet.datamasking.anonymizer.PostCodeAnonymizer;
import com.discreet.datamasking.probe.ProbeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AnonymizerTable {
    @Autowired
    ProbeService probeService;

    final private Map<String, Anonymizer> anonymizersMap = Map.of("name", new FullNameAnonymizer(),
            "email", new EmailAnonymizer(),
            "birthdate", new BirthDateAnonymizer(),
            "ccard", new CreditCardAnonymizer(),
            "address", new AddressAnonymizer(),
            "pid", new PersonalNumberAnonymizer()
            );

    final private Map<String, Anonymizer> postCodeAnonymizers = new ConcurrentHashMap<>();

    public Anonymizer getAnonymizer(String name, String schema, String table, String column) {
        if (name.equals("post")) {
            return postCodeAnonymizers.get(schema + "." + table + "." + column);
        } else {
            return anonymizersMap.get(name);
        }
    }

    public void addPostCodeAnonymizer(String schema, String table, String column) {
        String schemaWithTable = schema + "." + table;
        Set<String> values = probeService.probe(schemaWithTable, column);
        String anonymizerKey = schemaWithTable + "." + column;
        postCodeAnonymizers.put(anonymizerKey, new PostCodeAnonymizer(values));
    }
}
