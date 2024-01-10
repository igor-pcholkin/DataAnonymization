package com.discreet.datamasking;

import picocli.CommandLine;

public class CommandLineArgs {
    @CommandLine.Option(names = { "-h", "--help" }, usageHelp = true, description = "display a help message")
    private boolean helpRequested = false;

    public boolean isHelpRequested() {
        return helpRequested;
    }

}
