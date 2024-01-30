package com.discreet.dataprotection.autodetect;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.parser.ParserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SchemaSqlReader {

    private String defaultSchema;
    private String dbEngine;
    public void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    public void setDBEngine(String dbEngine) {
        this.dbEngine = dbEngine;
    }

    public List<DBTable> readDDL(String schemaFile) {
        List<DBTable> tables = new ArrayList<>();
        String sql = readDDLRaw(schemaFile);
        try {
            List<SQLStatement> statements = SQLUtils.parseStatements(sql, dbEngine);
            for (SQLStatement statement : statements) {
                addStatementAsDBTable(tables, statement);
            }
        } catch (ParserException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException("Error: cannot parse schema, please ensure that correct DB dialect (engine) is set using -dbe option.");
        }
        return tables;
    }

    private void addStatementAsDBTable(List<DBTable> tables, SQLStatement statement) {
        if (statement instanceof SQLCreateTableStatement createTableStatement) {
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

    private String readDDLRaw(String schemaFile) {
        try (InputStream schemaStream = new FileInputStream(schemaFile)) {
            return new String(schemaStream.readAllBytes());
        } catch (IOException ex) {
            throw new RuntimeException("Schema can't be loaded, please use the -sfn (--schemaFileName) option to set the path");
        }
    }

    private Column mapColumn(SQLColumnDefinition columnDefinition) {
        return new Column(columnDefinition.getName().getSimpleName(), columnDefinition.getDataType().getName());
    }

}
