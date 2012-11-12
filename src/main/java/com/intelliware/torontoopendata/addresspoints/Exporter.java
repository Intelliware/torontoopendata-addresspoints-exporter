package com.intelliware.torontoopendata.addresspoints;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;

import com.vividsolutions.jts.geom.Point;

/**
 * Load the Address Points data from the ESRI shapefile distribution and then allow processors to export each loaded feature.
 * 
 */
public class Exporter {
	private List<FeatureProcessor> processors = new ArrayList<FeatureProcessor>();

	public Exporter() throws IOException {
	}

	public void addProccessor(FeatureProcessor processor) {
		processors.add(processor);
	}

	public void process(File sourceEsri) throws IOException {
		read(sourceEsri);
	}

	private void read(File file) throws IOException {
		Map<String, Object> connect = new HashMap<String, Object>();
		connect.put("url", file.toURL());

		DataStore dataStore = DataStoreFinder.getDataStore(connect);
		String[] typeNames = dataStore.getTypeNames();
		String typeName = typeNames[0];

		System.out.println("Reading content " + typeName);

		FeatureSource featureSource = dataStore.getFeatureSource(typeName);
		FeatureCollection collection = featureSource.getFeatures();
		FeatureIterator iterator = collection.features();

		begin();

		try {
			try {
				while (iterator.hasNext()) {
					Feature feature = iterator.next();
					Point geometry = (Point) feature.getDefaultGeometryProperty().getValue();
					write(feature, geometry);
				}
			} finally {
				iterator.close();
			}
		} finally {
			end();
		}
	}

	private void write(Feature feature, Point geometry) throws IOException {
		for (FeatureProcessor processor : processors) {
			processor.write(feature, geometry);
		}
	}

	private void begin() throws IOException {
		for (FeatureProcessor processor : processors) {
			processor.begin();
		}
	}

	private void end() throws IOException {
		for (FeatureProcessor processor : processors) {
			try {
				processor.end();
			} catch (Exception e) {
				System.err.println("Failed to end: "+processor+" with:"+e);
				// continue
			}
		}
	}
}
