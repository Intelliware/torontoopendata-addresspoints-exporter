package com.intelliware.torontoopendata.addresspoints;

import java.io.IOException;

import org.opengis.feature.Feature;
import org.opengis.feature.Property;

import com.vividsolutions.jts.geom.Point;

public class SchemaLogger implements FeatureProcessor{

	private boolean wroteSchema = false;
	
	public void begin() throws IOException {
		wroteSchema = false;
	}
	
	public void write(Feature feature, Point geometry) throws IOException {
		if (!wroteSchema) {
			writeSchema(feature);
			wroteSchema = true;
		}
	}

	private void writeSchema(Feature feature) {
		for (Property property : feature.getProperties()) {
			System.out.println("SCHEMA: "+property.getName()+" - "+property.getType());
		}
	}

	public void end() throws IOException {
		// nothing
	}


}
