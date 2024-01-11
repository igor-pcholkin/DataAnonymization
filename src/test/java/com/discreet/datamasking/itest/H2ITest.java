package com.discreet.datamasking.itest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

@Sql({"schema.sql", "test-data.sql"})
@SpringBootTest(classes = CustomApplication.class)
@TestPropertySource(locations = "/application-h2.properties")
public class H2ITest {

    @Autowired
    private JdbcTemplate template;

    @Test
    public void testConnectionToDB() {
        List<Map<String, Object>> results = template.queryForList("select name from users");
        assertTrue(results.size() > 0);
    }
}
