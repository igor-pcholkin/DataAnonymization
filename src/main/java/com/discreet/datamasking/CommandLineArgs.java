package com.discreet.datamasking;

import picocli.CommandLine;

public class CommandLineArgs {
    @CommandLine.Option(names = { "-s", "--schema" },
            description = "ddl schema to use for auto-detection of DB table columns which can be anonymized")
    private String schema;

    public String getSchema() {
        return schema;
    }

}
