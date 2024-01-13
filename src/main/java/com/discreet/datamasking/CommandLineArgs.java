package com.discreet.datamasking;

import picocli.CommandLine;

public class CommandLineArgs {
    @CommandLine.Option(names = { "-s", "--schema" },
            description = "ddl schema file name to use for auto-detection of DB table columns which can be anonymized")
    private String schema;

    @CommandLine.Option(names = { "-dsn", "--defaultSchemaName" },
            description = "default schema name to use in case if schema name is missing in DDL schema")
    private String defaultSchemaName;

    public String getSchema() {
        return schema;
    }

    public String getDefaultSchemaName() {
        return defaultSchemaName;
    }

}
