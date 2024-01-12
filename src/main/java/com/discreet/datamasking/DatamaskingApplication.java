package com.discreet.datamasking;

import com.discreet.datamasking.autodetect.DBTable;
import com.discreet.datamasking.autodetect.SchemaSqlReader;
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

	@Autowired
	private SchemaSqlReader schemaSqlReader;

	public static void main(String[] args) {
		SpringApplication.run(DatamaskingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		CommandLineArgs commandLineArgs = new CommandLineArgs();
		new CommandLine(commandLineArgs).parseArgs(args);
		String schemaName = commandLineArgs.getSchema();
		if (commandLineArgs.getSchema() != null) {
			List<DBTable> tables = schemaSqlReader.readDDL(schemaName);
			System.out.println(tables);
		} else {
			List<Transformation> transformations = new TransformationsLoader().loadDefinitions();
			processor.process(transformations);
		}
	}
}
