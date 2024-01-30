package com.discreet.dataprotection.autodetect;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ColumnTranslationsLoader {
    public Map<String, Set<String>> readColumns() {
        Map<String, Set<String>> columnTranslationsMap = new HashMap<>();
        try (InputStream columnsFolderStream = this.getClass().getClassLoader()
                .getResourceAsStream("column_translations")) {
            if (columnsFolderStream != null) {
                String[] columnFileNames = new String(columnsFolderStream.readAllBytes()).split("\\n");
                for (String columnFileName: columnFileNames) {
                    addColumnTranslationsToMap(columnTranslationsMap, columnFileName);
                }
                return columnTranslationsMap;
            } else {
                throw new RuntimeException("Cannot load contents of column translations folder");
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load column translations");
        }
    }

    private void addColumnTranslationsToMap(Map<String, Set<String>> columnTranslationsMap, String columnFileName) throws IOException {
        String columnName = parseColumnName(columnFileName);
        Set<String> columnTranslations = parseColumnTraslationsFile(columnFileName);
        columnTranslationsMap.put(columnName, columnTranslations);
    }

    private String parseColumnName(String columnFileName) {
        return columnFileName.split("\\.")[0];
    }

    private Set<String> parseColumnTraslationsFile(String fileName) throws IOException {
        try (InputStream columnResourceStream = this.getClass().getClassLoader()
                     .getResourceAsStream("column_translations/" + fileName)) {
            if (columnResourceStream != null) {
                return parseColumnTraslationsContent(new String(columnResourceStream.readAllBytes()));
            } else {
                throw new RuntimeException("Cannot load column translations resource " + fileName);
            }
        }
    }

    Set<String> parseColumnTraslationsContent(String columnTraslationsContent) {
        return Arrays.stream(columnTraslationsContent.split("\\n"))
                .flatMap(this::getTranslationsFromLine)
                .collect(Collectors.toSet());
    }

    private Stream<String> getTranslationsFromLine(String line) {
        String[] lineTokens = line.split(":");
        String rawValue = lineTokens[1];
        String[] values = rawValue.split("[()]");
        return Arrays.stream(values).map(value -> value.strip().replace(' ', '_'));
    }
}
