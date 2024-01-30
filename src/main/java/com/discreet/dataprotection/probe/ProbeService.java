package com.discreet.dataprotection.probe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;

@Service
public class ProbeService {
    @Autowired
    final private JdbcTemplate jdbcTemplate;

    private static final int UNLIMITED_NUMBER_VALUES = Integer.MAX_VALUE;

    public ProbeService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<String> probe(String table, String column, int maxValues) {
        Set<String> values = new TreeSet<>();
        String sql = String.format("select distinct %s from %s limit %d", column, table, maxValues);
        jdbcTemplate.query(sql, rs -> {
            values.add(rs.getNString(column));
        });
        return values;
    }

    public Set<String> probe(String table, String column) {
        return probe(table, column, UNLIMITED_NUMBER_VALUES);
    }
}
