package com.discreet.datamasking;

import com.discreet.datamasking.transformations.Transformation;
import com.discreet.datamasking.transformations.TransformationsLoader;
import com.discreet.datamasking.transformations.TransformationsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

import java.util.List;

@SpringBootApplication
public class DatamaskingApplication implements CommandLineRunner {

	@Autowired
	private TransformationsProcessor processor;

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
		List<Transformation> transformations = new TransformationsLoader().loadDefinitions();
		processor.process(transformations);
	}
}
