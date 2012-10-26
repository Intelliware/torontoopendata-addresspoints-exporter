package com.intelliware.torontoopendata.addresspoints;

import java.io.File;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

/**
 * Holder and configuration of command line options.
 */
public class CmdLineOptions {

	@Argument(required = false, index = 0, metaVar="SHP-FILE", usage="read addresses from Toronto open data address points ESRI .shp file")
	public File source = new File(".download/address_point_wgs84.shp");
	
	@Option(name="-v", aliases= {"--verbose"}, usage="log each address point read")
	public boolean verbose;
	
	@Option(name="-m", aliases= {"--mongo"}, metaVar="MONGO-URI", usage="write addresses to the specified mongo database URI")
	public String mongoUrl;
	
	@Option(name="-c", aliases= {"--csv"}, metaVar="CSV-FILE", usage="write addresses to the specified CSV file")
	public File csv;
}
