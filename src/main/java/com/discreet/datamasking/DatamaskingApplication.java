package com.discreet.datamasking;

import com.discreet.datamasking.autodetect.TransformationsAutoDetector;
import com.discreet.datamasking.transformations.Transformation;
import com.discreet.datamasking.transformations.TransformationsLoader;
import com.discreet.datamasking.transformations.TransformationsProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@SpringBootApplication
@Slf4j
public class DatamaskingApplication implements CommandLineRunner {

	@Autowired
	private TransformationsProcessor processor;

	@Autowired
	private TransformationsAutoDetector autoDetector;

	public static void main(String[] args) {
		SpringApplication.run(DatamaskingApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			CommandLineArgs commandLineArgs = new CommandLineArgs();
			CommandLine commandLine = new CommandLine(commandLineArgs);
			commandLine.parseArgs(args);
			if (commandLineArgs.isUsageHelpRequested() || isEmpty(args)) {
				commandLine.usage(System.out);
				return;
			}
			List<Transformation> transformations;
			if (commandLineArgs.getSchemaFileName() != null || commandLineArgs.getSchemaName() != null) {
				transformations = autoDetector.autodetectTransformations(commandLineArgs);
			} else {
				transformations = new TransformationsLoader()
						.loadDefinitions(commandLineArgs.getTransformationsFileName());
			}
			processor.process(transformations);
		}
		catch (RuntimeException ex) {
			log.error(ex.getMessage());
			System.err.println(ex.getMessage());
		}
	}

}
