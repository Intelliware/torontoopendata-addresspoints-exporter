package com.intelliware.torontoopendata.addresspoints;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.opengis.feature.Feature;

import com.vividsolutions.jts.geom.Point;

/**
 * Write each source address to a CSV file.
 */
public class CsvWriter implements FeatureProcessor {

	FileWriter writer;
	private final File file;
	
	public CsvWriter(File file) {
		this.file = file;
		
	}

	public void begin() throws IOException {
		writer = new FileWriter(file);
	}
	
	public void write(Feature feature, Point sourceGeometry) throws IOException {
		
		writer.write(""+ feature.getProperty("GEO_ID").getValue()+","+sourceGeometry.getY()+","+sourceGeometry.getX()+","+feature.getProperty("ADDRESS").getValue()+feature.getProperty("LF_NAME").getValue()+","+feature.getProperty("LO_NUM").getValue()+","+feature.getProperty("HI_NUM").getValue()+"\n");
	}
	
	public void end() throws IOException {
		writer.close();
	}
}
