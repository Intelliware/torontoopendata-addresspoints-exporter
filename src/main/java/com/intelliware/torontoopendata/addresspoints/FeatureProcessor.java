package com.intelliware.torontoopendata.addresspoints;

import java.io.IOException;

import org.opengis.feature.Feature;

import com.vividsolutions.jts.geom.Point;

public interface FeatureProcessor {

	public abstract void write(Feature feature, Point geometry) throws IOException;

	public abstract void end() throws IOException;

	public abstract void begin() throws IOException;

}