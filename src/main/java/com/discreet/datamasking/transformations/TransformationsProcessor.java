package com.discreet.datamasking.transformations;

import com.discreet.datamasking.AnonymizerTable;
import com.discreet.datamasking.anonymizer.Anonymizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TransformationsProcessor {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    AnonymizerTable anonymizerTable;

    @Transactional
    public void process(List<Transformation> transformations) {
        for (Transformation transformation: transformations) {
            processTransformation(transformation);
        }
    }

    private void processTransformation(Transformation transformation) {
        String columns = String.join(",", transformation.getColumns().keySet());
        String sql = String.format("select id,%s from %s.%s", columns, transformation.getSchema(),
                transformation.getTable());
        addPostCodeAnonymizerIfNeeded(transformation);
        jdbcTemplate.query(sql, rs -> {
            anonymizeRow(rs, transformation);
        });
    }

    private void addPostCodeAnonymizerIfNeeded(Transformation transformation) {
        for (Map.Entry<String, String> entry: transformation.getColumns().entrySet()) {
            String column = entry.getKey();
            String anonymizer = entry.getValue();
            if (anonymizer.equals("post")) {
                anonymizerTable.addPostCodeAnonymizer(transformation.getSchema(), transformation.getTable(),
                        column);
            }
        }
    }

    private void anonymizeRow(ResultSet rs, Transformation transformation) throws SQLException {
       String modifiedColumns = transformation.getColumns().keySet().stream().map(column ->
               String.format("%s='%s'", column, anonymizeColumn(transformation, column, rs))).collect(Collectors.joining(","));

        String update = String.format("update %s.%s set %s where id=?", transformation.getSchema(),
                transformation.getTable(), modifiedColumns);

        jdbcTemplate.update(update, rs.getInt("id"));
    }

    private String anonymizeColumn(Transformation transformation, String column, ResultSet rs) {
        String anonymizerName = transformation.getColumns().get(column);
        Anonymizer anonymizer = anonymizerTable.getAnonymizer(anonymizerName, transformation.getSchema(),
                transformation.getTable(), column);
        String input;
        try {
            if (anonymizerName.equals("birthdate")) {
                input = String.valueOf(rs.getDate(column));
            } else {
                input = rs.getNString(column);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return anonymizer.anonymize(input);
    }
}
