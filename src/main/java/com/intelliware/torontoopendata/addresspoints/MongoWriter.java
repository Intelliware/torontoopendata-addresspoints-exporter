package com.intelliware.torontoopendata.addresspoints;

import java.io.IOException;

import org.opengis.feature.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.vividsolutions.jts.geom.Point;

/**
 * Write each source address to a mongo database.
 */
public class MongoWriter implements FeatureProcessor {

	private MongoURI mongoUri;
	private DB db;
	private Mongo mongo;
	private DBCollection addressCollection;

	private final Logger logger = LoggerFactory.getLogger(MongoWriter.class);
	
	public MongoWriter(MongoURI mongoUri) {
		this.mongoUri = mongoUri;
	}
	
	public void begin() throws IOException {
		logger.info("Initializing connection to mongo instance: "+mongoUri);
		db = mongoUri.connectDB();
		if (mongoUri.getUsername() != null && mongoUri.getPassword() != null) {
			db.authenticate(mongoUri.getUsername(), mongoUri.getPassword());
		}
		addressCollection = db.getCollection("addresses");
		logger.info("Using mongo collection: "+addressCollection.getName()+" with count: "+addressCollection.count());
	}

	public void write(Feature feature, Point geometry) {
		BasicDBObject document = new BasicDBObject();
		document.put("id", feature.getProperty("GEO_ID").getValue());
		document.put("lat", geometry.getY());
		document.put("lng", geometry.getX());
		document.put("address", feature.getProperty("ADDRESS").getValue());
		document.put("street", feature.getProperty("LF_NAME").getValue());
		document.put("numberLow", feature.getProperty("LO_NUM").getValue());
		document.put("numberHigh", feature.getProperty("HI_NUM").getValue());
		addressCollection.insert(document);
	}

	public void end() {
		mongo.close();
	}

}
