package com.discreet.datamasking.autodetect;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class ColumnToAnonymizerLoader {
    public Map<String, String> load() {
        try (InputStream resourceAsStream = this.getClass().getClassLoader()
                .getResourceAsStream("columnToAnonymizer.properties")) {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            return new HashMap(properties);
        } catch (IOException ex) {
            throw new RuntimeException("Can't load columnToAnonymizer.properties");
        }
    }
}
