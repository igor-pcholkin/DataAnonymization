package com.discreet.datamasking.autodetect;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.util.JdbcConstants;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SchemaSqlReader {

    private String defaultSchema;
    public void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    public List<DBTable> readDDL(String schemaFile) throws IOException {
        List<DBTable> tables = new ArrayList<>();
        String sql;
        try (InputStream schemaStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(schemaFile)) {
            if (schemaStream != null) {
                sql = new String(schemaStream.readAllBytes());
            } else {
                // TODO add proper logging
                throw new RuntimeException("Cannot load schema!");
            }
        }
        List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        for (SQLStatement statement : statements) {
            if (statement instanceof MySqlCreateTableStatement createTableStatement) {
                SQLExprTableSource tableSource = createTableStatement.getTableSource();
                List<Column> columns = createTableStatement.getTableElementList().stream()
                        .filter(c -> c instanceof SQLColumnDefinition)
                        .map(c -> mapColumn((SQLColumnDefinition) c)).collect(Collectors.toList());
                String schemaName = tableSource.getSchema();
                if (schemaName == null) {
                    if (defaultSchema != null) {
                        schemaName = defaultSchema;
                    } else {
                        throw new RuntimeException(String.format("No schema name is found for table %s, please use " +
                                "-dsn (--defaultSchemaName) command line argument to set a default one.",
                                tableSource.getName().getSimpleName()));
                    }
                }
                DBTable table = new DBTable(schemaName, tableSource.getName().getSimpleName(), columns);
                tables.add(table);
            }
        }
        return tables;
    }

    private Column mapColumn(SQLColumnDefinition columnDefinition) {
        return new Column(columnDefinition.getName().getSimpleName(), columnDefinition.getDataType().getName());
    }
}
