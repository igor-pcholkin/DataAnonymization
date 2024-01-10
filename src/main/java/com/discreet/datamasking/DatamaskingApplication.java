package com.discreet.datamasking;

import com.discreet.datamasking.transformations.Transformations;
import com.discreet.datamasking.transformations.TransformationsLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

import java.util.Map;

@SpringBootApplication
public class DatamaskingApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DatamaskingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		CommandLineArgs commandLineArgs = new CommandLineArgs();
		new CommandLine(commandLineArgs).parseArgs(args);
		if (commandLineArgs.isHelpRequested()) {
			System.out.println("Help was requested.");
		}
		Transformations transformations = new TransformationsLoader().loadDefinitions();
		System.out.println(transformations);
	}
}
