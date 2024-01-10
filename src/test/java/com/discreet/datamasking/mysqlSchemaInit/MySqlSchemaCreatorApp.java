package com.discreet.datamasking.mysqlSchemaInit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@SpringBootApplication
@Import({ MySqlConfiguration.class})
public class MySqlSchemaCreatorApp implements CommandLineRunner  {
    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(MySqlSchemaCreatorApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ResourceDatabasePopulator dbPopulator = new ResourceDatabasePopulator();
        dbPopulator.addScript(new ClassPathResource("/com/discreet/datamasking/create_db.sql"));
        dbPopulator.addScript(new ClassPathResource("/com/discreet/datamasking/schema.sql"));
        dbPopulator.addScript(new ClassPathResource("/com/discreet/datamasking/test-data.sql"));
        dbPopulator.execute(dataSource);
    }
}
