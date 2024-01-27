package com.discreet.datamasking.itest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

public class H2ITest extends BaseITest {

    @Autowired
    private JdbcTemplate template;

    @Test
    public void testConnectionToDB() {
        List<Map<String, Object>> results = template.queryForList("select name from users");
        assertTrue(results.size() > 0);
    }
}
