package com.discreet.dataprotection.autodetect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
@Slf4j
public class ColumnToAnonymizerLoader {
    private final static Map<String, String> INSTANCE = load();

    private static Map<String, String> load() {
        log.debug("Loading columnToAnonymizer table...");
        try (InputStream resourceAsStream = ColumnToAnonymizerLoader.class.getClassLoader()
                .getResourceAsStream("columnToAnonymizer.properties")) {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            return new HashMap(properties);
        } catch (IOException ex) {
            throw new RuntimeException("Can't load columnToAnonymizer.properties");
        }
    }

    public Map<String, String> getColumnToAnonymizerTable() {
        return INSTANCE;
    }
}
