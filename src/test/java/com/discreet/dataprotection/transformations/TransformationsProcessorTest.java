package com.discreet.dataprotection.transformations;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransformationsProcessorTest {
    TransformationsProcessor transformationProcessor = new TransformationsProcessor();

    @Test
    public void testDbIsNotAffectedOnEmptyColumns() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        transformationProcessor.setJdbcTemplate(jdbcTemplate);

        Transformation transformation = new Transformation("schema", "table", Map.of(),
                List.of("id"));

        transformationProcessor.processTransformation(transformation);
        verify(jdbcTemplate, times(0)).query(anyString(), any(RowCallbackHandler.class));
    }

    @Test
    public void testDbAffectedOnNonEmptyColumns() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        transformationProcessor.setJdbcTemplate(jdbcTemplate);

        Transformation transformation = new Transformation("schema", "table", Map.of("name", "name"),
                List.of("id"));

        transformationProcessor.processTransformation(transformation);
        verify(jdbcTemplate, times(1)).query(eq("select id,name from schema.table"), any(RowCallbackHandler.class));
    }
}
