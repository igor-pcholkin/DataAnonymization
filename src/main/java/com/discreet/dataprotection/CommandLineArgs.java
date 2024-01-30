package com.discreet.dataprotection;

import picocli.CommandLine;

@CommandLine.Command(name = "dataprotection")
public class CommandLineArgs {
    @CommandLine.Option(names = {"?", "-h", "--help"}, usageHelp = true, description = "display this help message")
    private boolean usageHelpRequested;

    @CommandLine.Option(names = { "-sfn", "--schemaFileName" },
            description = "ddl schema file name to use for auto-detection of DB table columns which can be anonymized")
    private String schemaFileName;

    @CommandLine.Option(names = { "-sn", "--schemaName" },
            description = "schema name to use for reading metadata from DB and auto-detection of DB table columns which can be anonymized")
    private String schemaName;

    @CommandLine.Option(names = { "-tfn", "--transformationsFileName" },
            description = "yaml file describing for each schema/table how each column should be anonymized")
    private String transformationsFileName;

    @CommandLine.Option(names = { "-dsn", "--defaultSchemaName" },
            description = "default schema name to use in case if schema name is missing in DDL schema")
    private String defaultSchemaName;

    @CommandLine.Option(names = { "-dbe", "--dbEngine" },
            description = "DB engine, one of mysql, oracle, postgresql, db2, jtds, sybase, sqlserver, mariadb, derby, hive, h2, informix")
    private String dbEngine;

    public String getSchemaFileName() {
        return schemaFileName;
    }

    public String getSchemaName() {
        return schemaName;
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

    public boolean isUsageHelpRequested() {
        return usageHelpRequested;
    }

    public String getDbEngine() {
        return dbEngine;
    }

    public void setDbEngine(String dbEngine) {
        this.dbEngine = dbEngine;
    }
}
