package com.discreet.datamasking.transformations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransformationsProcessor {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void process(List<Transformation> transformations) {
        System.out.println(jdbcTemplate);
    }
}
