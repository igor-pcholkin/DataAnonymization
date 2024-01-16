package com.discreet.datamasking;

import com.discreet.datamasking.autodetect.ColumnTranslationsLoader;
import com.discreet.datamasking.autodetect.DBTable;
import com.discreet.datamasking.autodetect.SchemaSqlReader;
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
import java.util.Map;
import java.util.Set;

@SpringBootApplication
@Slf4j
public class DatamaskingApplication implements CommandLineRunner {

	@Autowired
	private TransformationsProcessor processor;

	@Autowired
	private SchemaSqlReader schemaSqlReader;

	@Autowired
	private ColumnTranslationsLoader columnTranslationsLoader;

	public static void main(String[] args) {
		SpringApplication.run(DatamaskingApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			CommandLineArgs commandLineArgs = new CommandLineArgs();
			new CommandLine(commandLineArgs).parseArgs(args);
			if (commandLineArgs.getSchemaFileName() != null) {
				autodetectSchema(commandLineArgs);
			} else {
				List<Transformation> transformations = new TransformationsLoader().loadDefinitions(commandLineArgs.getTransformationsFileName());
				processor.process(transformations);
			}
		}
		catch (RuntimeException ex) {
			log.error(ex.getMessage());
		}
	}

	private void autodetectSchema(CommandLineArgs commandLineArgs) {
		Map<String, Set<String>> columnTranslations = columnTranslationsLoader.readColumns();
		System.out.println(columnTranslations.keySet());
		String schemaFileName = commandLineArgs.getSchemaFileName();
		if (commandLineArgs.getDefaultSchemaName() != null) {
			schemaSqlReader.setDefaultSchema(commandLineArgs.getDefaultSchemaName());
		}
		List<DBTable> tables = schemaSqlReader.readDDL(schemaFileName);
		System.out.println(tables);
	}
}
