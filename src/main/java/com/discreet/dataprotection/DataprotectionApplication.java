package com.discreet.dataprotection;

import com.discreet.dataprotection.autodetect.TransformationsAutoDetector;
import com.discreet.dataprotection.transformations.Transformation;
import com.discreet.dataprotection.transformations.TransformationsLoader;
import com.discreet.dataprotection.transformations.TransformationsProcessor;
import com.discreet.dataprotection.transformations.TransformationsWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import picocli.CommandLine;

import java.io.File;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@SpringBootApplication
@Slf4j
public class DataprotectionApplication implements CommandLineRunner {

	@Autowired
	private TransformationsProcessor processor;

	@Autowired
	private TransformationsAutoDetector autoDetector;

	@Autowired
	private TransformationsWriter writer;

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(DataprotectionApplication.class)
				.properties("spring.config.name:application,myapp",
						"spring.config.location:file:conf/application.properties")
				.build().run(args);
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
				writer.write(transformations, new File("transformations.yaml"));
			} else {
				transformations = new TransformationsLoader()
						.loadDefinitions(commandLineArgs.getTransformationsFileName());
				processor.process(transformations);
			}
		}
		catch (RuntimeException ex) {
			log.error(ex.getMessage());
			System.err.println(ex.getMessage());
		}
	}

}
