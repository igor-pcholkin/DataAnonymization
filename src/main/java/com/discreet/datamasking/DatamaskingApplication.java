package com.discreet.datamasking;

import com.discreet.datamasking.commandline.CommandLineArgs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class DatamaskingApplication {

	public static void main(String[] args) {
		CommandLineArgs commandLineArgs = new CommandLineArgs();
		new CommandLine(commandLineArgs).parseArgs(args);
		if (commandLineArgs.isHelpRequested()) {
			System.out.println("Help was requested.");
		}
		SpringApplication.run(DatamaskingApplication.class, args);
	}

}
