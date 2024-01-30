package com.discreet.dataprotection.itest;

import com.discreet.dataprotection.autodetect.SchemaMetadataReader;
import com.discreet.dataprotection.probe.ProbeService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
// this is a application for integration test, otherwise spring picks up main application
public class CustomApplication {
    @Bean
    public ProbeService getProbeService(JdbcTemplate jdbcTemplate) {
        return new ProbeService(jdbcTemplate);
    }

    @Bean
    public SchemaMetadataReader getMetadataReader(JdbcTemplate jdbcTemplate) { return new SchemaMetadataReader(jdbcTemplate); }
}
