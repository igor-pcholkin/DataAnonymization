package com.discreet.datamasking.mysqlSchemaInit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application-mysql.properties")
public class MySqlConfiguration {

    @Value("${spring.mysql.datasource.url}")
    private String url;
    @Value("${spring.mysql.datasource.username}")
    private String username;
    @Value("${spring.mysql.datasource.password}")
    private String password;
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

}
