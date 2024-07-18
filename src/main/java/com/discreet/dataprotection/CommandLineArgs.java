package com.discreet.dataprotection;

import picocli.CommandLine;

@CommandLine.Command(name = "dp", subcommands = {Detect.class, Transform.class })
public class CommandLineArgs {
    DataprotectionApplication application;

    public CommandLineArgs(DataprotectionApplication application) {
        this.application = application;
    }

    @CommandLine.Option(names = { "-h", "--h", "--help", "help" },
            description = "show help for this command", help = true, usageHelp = true, hidden = true)
    private boolean isHelpNeeded;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
}

@CommandLine.Command(name = "detect", description = "(Auto)-detect data for anonymization")
class Detect implements Runnable {
    @CommandLine.ParentCommand
    private CommandLineArgs parent;

    @CommandLine.Option(names = { "-h", "--h", "--help", "help" },
            description = "show help for this command", help = true, usageHelp = true, hidden = true)
    private boolean isHelpNeeded;
    @CommandLine.Option(names = { "-sfn", "--schemaFileName" },
            description = "ddl schema file name to use for auto-detection of DB table columns which can be anonymized")
    private String schemaFileName;

    @CommandLine.Option(names = { "-sn", "--schemaName" },
            description = "schema name to use for reading metadata from DB and auto-detection of DB table columns which can be anonymized")
    private String schemaName;

    @CommandLine.Option(names = { "-dsn", "--defaultSchemaName" },
            description = "default schema name to use in case if schema name is missing in DDL schema")
    private String defaultSchemaName;

    @CommandLine.Option(names = { "-dbe", "--dbEngine" },
            description = "DB engine, one of mysql, oracle, postgresql, db2, jtds, sybase, sqlserver, mariadb, derby, hive, h2, informix")
    private String dbEngine;

    @CommandLine.Option(names = { "-iid", "--ignoreMissingIds" },
            description = "Ignore db tables with missing or not detected id columns")
    private boolean ignoreMissingIds;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    @Override
    public void run() {
        if (schemaFileName != null || schemaName != null) {
            parent.application.executeAutodetectTransformationsCommand(schemaFileName, schemaName, ignoreMissingIds,
                    defaultSchemaName, dbEngine);
        }
    }
}

@CommandLine.Command(name = "transform", description = "Anonimize data using supplied transformations file")
class Transform implements Runnable {
    @CommandLine.ParentCommand
    private CommandLineArgs parent;

    @CommandLine.Option(names = { "-h", "--h", "--help", "help" },
            description = "show help for this command", help = true, usageHelp = true, hidden = true)
    private boolean isHelpNeeded;

    @CommandLine.Option(names = { "-tfn", "--transformationsFileName" },
            description = "yaml file describing for each schema/table how each column should be anonymized")
    private String transformationsFileName;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    @Override
    public void run() {
        parent.application.executeTransformCommand(transformationsFileName);
    }
}
