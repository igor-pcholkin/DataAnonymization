package com.discreet.datamasking.itest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@Sql({"schema.sql", "test-data.sql"})
@SpringBootTest(classes = CustomApplication.class)
@TestPropertySource(locations = "/application-h2.properties")
public class BaseITest {
}
