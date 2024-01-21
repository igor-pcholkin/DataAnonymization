package com.discreet.datamasking;

import picocli.CommandLine;

public class CommandLineArgs {
    @CommandLine.Option(names = { "-sfn", "--schemaFileName" },
            description = "ddl schema file name to use for auto-detection of DB table columns which can be anonymized")
    private String schemaFileName;

    @CommandLine.Option(names = { "-tfn", "--transformationsFileName" },
            description = "ddl schema file name to use for auto-detection of DB table columns which can be anonymized")
    private String transformationsFileName;

    @CommandLine.Option(names = { "-dsn", "--defaultSchemaName" },
            description = "default schema name to use in case if schema name is missing in DDL schema")
    private String defaultSchemaName;

    public String getSchemaFileName() {
        return schemaFileName;
    }

    public String getDefaultSchemaName() {
        return defaultSchemaName;
    }

    public String getTransformationsFileName() {
        return transformationsFileName;
    }

    public void setSchemaFileName(String schemaFileName) {
        this.schemaFileName = schemaFileName;
    }
}
