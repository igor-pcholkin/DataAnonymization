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
    public List<DBTable> readDDL(String schemaFile) throws IOException {
        List<DBTable> tables = new ArrayList<>();
        String sql = null;
        try (InputStream schemaStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(schemaFile)) {
            if (schemaStream != null) {
                sql = new String(schemaStream.readAllBytes());
            } else {
                // TODO add proper logging
                System.out.println("Cannot load schema!");
                return null;
            }
        }
        List<SQLStatement> statements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        for (SQLStatement statement : statements) {
            if (statement instanceof MySqlCreateTableStatement createTableStatement) {
                SQLExprTableSource tableSource = createTableStatement.getTableSource();
                List<String> columns = createTableStatement.getTableElementList().stream()
                        .filter(c -> c instanceof SQLColumnDefinition)
                        .map(c -> ((SQLColumnDefinition) c).getName().getSimpleName()).collect(Collectors.toList());
                DBTable table = new DBTable(tableSource.getSchema(), tableSource.getName().getSimpleName(), columns);
                tables.add(table);
            }
        }
        return tables;
    }
}
