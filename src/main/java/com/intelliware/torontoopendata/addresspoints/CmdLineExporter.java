package com.intelliware.torontoopendata.addresspoints;

import java.io.IOException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.mongodb.MongoURI;

/**
 * Command line for configuring and running the exporter.
 * 
 */
public class CmdLineExporter {

	public static void main(String[] args) throws Exception {
		new CmdLineExporter().run(args);
	}
	
	public CmdLineExporter() throws IOException {
	}

	private void run(String[] args) throws IOException {
		CmdLineOptions options = new CmdLineOptions();
		CmdLineParser parser = new CmdLineParser(options);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			printParseMessage(parser, e);
			return;
		}
		
		Exporter exporter = createExporter(options);
		exporter.process(options.source);
	}

	private Exporter createExporter(CmdLineOptions options) throws IOException {
		Exporter exporter = new Exporter();
		
		if (options.verbose) {
			exporter.addProccessor(new VerboseLogger());
		}
		if (options.csv != null) {
			exporter.addProccessor(new CsvWriter(options.csv));
		}
		if (options.mongoUrl != null) {
			exporter.addProccessor(new MongoWriter(new MongoURI(options.mongoUrl)));
		}
		return exporter;
	}

	private void printParseMessage(CmdLineParser parser, CmdLineException e) {
		System.err.println(e.getMessage());
		System.err.println("java -jar addresspoints-importer.jar [options...] SHP-FILE");
		parser.printUsage(System.err);
	}
}
