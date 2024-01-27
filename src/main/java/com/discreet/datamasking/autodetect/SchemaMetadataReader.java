package com.discreet.datamasking.autodetect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SchemaMetadataReader {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SchemaMetadataReader(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DBTable> read(String schema) {
        List<DBTable> tables = new LinkedList<>();
        jdbcTemplate.query("SHOW TABLES FROM " + schema, rs -> {
            String table = rs.getNString(1);
            List<Column> columns = new LinkedList<>();
            DBTable dbTable = new DBTable(schema, table, columns);
            addColumns(dbTable);
            tables.add(dbTable);
        });
        return tables;
    }

    private void addColumns(DBTable dbTable) {
        String sql = String.format("SHOW COLUMNS FROM %s.%s", dbTable.getSchema(), dbTable.getTable());
        List<Column> columns = dbTable.getColumns();
        jdbcTemplate.query(sql, rs -> {
            Column column = new Column(rs.getNString(1), rs.getNString(2));
            columns.add(column);
        });
    }
}
