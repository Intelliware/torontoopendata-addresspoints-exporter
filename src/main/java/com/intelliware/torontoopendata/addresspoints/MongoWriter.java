package com.intelliware.torontoopendata.addresspoints;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.opengis.feature.Feature;
import org.opengis.feature.Property;
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
	
	// NOTE: Dependency on mongo help comment in CmdLineOptions
	public final static String DB_COLLECTION = "addresses";

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
		addressCollection = db.getCollection(DB_COLLECTION);
		logger.info("Using mongo collection: "+addressCollection.getName());
		removeExistingAddresses();
		esureIndexesPresent();
		logger.info("Loading data...");
	}

	private void esureIndexesPresent() {
		logger.info("Ensure indexes setup...");
		Map<String, Object> indexes = new HashMap<String,Object>();
		indexes.put(SourceSchema.ADDRESS, 1);
		indexes.put(SourceSchema.LF_NAME, 1);
		addressCollection.ensureIndex(new BasicDBObject(indexes));
	}

	private void removeExistingAddresses() {
		logger.info("Clearing out initial items:" +addressCollection.count()+"...");
		addressCollection.remove(new BasicDBObject());
	}

	public void write(Feature feature, Point geometry) {
		BasicDBObject document = new BasicDBObject();
		for (Property property : feature.getProperties()) {
			if (property.getValue() instanceof Point) {
				document.put("lat", geometry.getY());
				document.put("lng", geometry.getX());
			} else {
				document.put(property.getName().toString(), property.getValue());
			}
		}
		addressCollection.insert(document);
	}

	public void end() {
		mongo.close();
	}

}
