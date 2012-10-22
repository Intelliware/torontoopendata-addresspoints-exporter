package com.intelliware.torontoopendata.addresspoints;

import java.io.IOException;

import org.opengis.feature.Feature;

import com.vividsolutions.jts.geom.Point;

/**
 * Print to the logger each feature/address that is read in from the source data.
 */
public class VerboseLogger implements FeatureProcessor {

	public void begin() throws IOException {
		// nothing
	}
	
	public void write(Feature feature, Point geometry) throws IOException {
		
		System.out.println(feature.getProperty("ADDRESS").getValue()+" "+feature.getProperty("LF_NAME").getValue()+" ("+geometry.getY()+","+geometry.getX()+")");		
	}

	public void end() throws IOException {
		// nothing
	}

}
