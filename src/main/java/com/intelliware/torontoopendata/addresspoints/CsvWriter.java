package com.intelliware.torontoopendata.addresspoints;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.opengis.feature.Feature;
import org.opengis.feature.Property;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.vividsolutions.jts.geom.Point;

/**
 * Write each source address to a CSV file.
 */
public class CsvWriter implements FeatureProcessor {

	private FileWriter writer;
	private final File file;
	private boolean wroteHeader = false;

	public CsvWriter(File file) {
		this.file = file;

	}

	public void begin() throws IOException {
		writer = new FileWriter(file);
		wroteHeader = false;
	}

	public void write(Feature feature, Point sourceGeometry) throws IOException {
		writeHeaderIfNecessary(feature);
		writer.write(Joiner.on(",").skipNulls().join(
				Iterables.transform(feature.getProperties(),
						new Function<Property, Object>() {

							public Object apply(Property property) {
								if (property.getValue() instanceof Point) {
									return null;
								} else {
									return property.getValue();
								}
							}

						})));
		writer.write("," + sourceGeometry.getY() + "," + sourceGeometry.getX());
		writer.write('\n');
	}

	private void writeHeaderIfNecessary(Feature feature) throws IOException {
		if (!wroteHeader) {
			writeHeader(feature);
			wroteHeader = true;
		}
	}

	private void writeHeader(Feature feature) throws IOException {
		writer.write(Joiner.on(",").skipNulls().join(
				Iterables.transform(feature.getProperties(),
						new Function<Property, String>() {

							public String apply(Property property) {
								if (property.getValue() instanceof Point) {
									return null;
								} else {
									return property.getName().toString();
								}
							}

						})));
		writer.write(",lat,lon");
		writer.write('\n');
	}

	public void end() throws IOException {
		writer.close();
	}
}
