package com.intelliware.torontoopendata.addresspoints;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.opengis.feature.Feature;
import org.opengis.feature.Property;

import com.google.common.base.Objects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vividsolutions.jts.geom.Point;

/**
 * Dump out stats based on the loaded addresses in a JSON format.
 */
public class StatsLogger implements FeatureProcessor{

	private int totalAddresses = 0;
	private Set<Object> streets;
	private Map<Object,Object> categories;
	private Map<Object,Integer> categoryTotals;
	private Map<Object,Integer> streetNumberTotals;
	
	public void begin() throws IOException {
		totalAddresses = 0;
		streets = new TreeSet<Object>();
		categories = new TreeMap<Object, Object>();
		categoryTotals = new TreeMap<Object, Integer>();
		streetNumberTotals = new TreeMap<Object, Integer>();
	}
	
	public void write(Feature feature, Point geometry) throws IOException {
		totalAddresses += 1;
		includeMaping(feature, SourceSchema.FCODE, SourceSchema.FCODE_DESC, categories);
		increment(feature, SourceSchema.FCODE, categoryTotals);
		increment(feature, SourceSchema.LO_NUM, streetNumberTotals);
		include(feature, SourceSchema.LF_NAME, streets);
	}

	private void include(Feature feature, String fcodeDesc, Set<Object> set) {
		Object value = getPropertyValueOrNull(feature, fcodeDesc);
		if (value != null) {
			set.add(value);
		}
	}

	private Object getPropertyValueOrNull(Feature feature, String propertyName) {
		Property property = feature.getProperty(propertyName);
		if (property != null) {
			return property.getValue();
		} else {
			return null;
		}
		
	}
	
	private void includeMaping(Feature feature, String fcode, String fcodeDesc, Map<Object, Object> map) {
		Object fcodeValue = getPropertyValueOrNull(feature, fcode);
		Object descriptionValue = getPropertyValueOrNull(feature, fcodeDesc);
		if (fcodeValue != null && descriptionValue != null && !categories.containsKey(fcodeValue)) {
			categories.put(fcodeValue, descriptionValue);
		}
	}

	private void increment(Feature feature, String fcode, Map<Object, Integer> totals) {
		Object key = getPropertyValueOrNull(feature, fcode);
		if (key != null) {
			Integer count = Objects.firstNonNull(totals.get(key), Integer.valueOf(0));
			totals.put(key, count + 1);
		}
	}

	public void end() throws IOException {
		String jsonStats = createGson().toJson(createJson());
		System.out.println(jsonStats);
	}

	private Gson createGson() {
		return new GsonBuilder().setPrettyPrinting().create();
	}

	private JsonObject createJson() {
		JsonObject root = new JsonObject();
		root.addProperty("totalAddresses", totalAddresses);
		root.addProperty("totalStreets", streets.size());
		root.add("categories", createJsonCategoriesHierarchy()); 
		return root;
	}

	private JsonElement createJsonCategoriesHierarchy() {
		JsonObject root = new JsonObject();
		root.addProperty("category", "all");
		JsonArray allCategories = new JsonArray();
		root.add("children", allCategories);
		int lastGroup = -1;
		JsonArray groupArray = null;
		for (Object key : categoryTotals.keySet()) {
			int group = categoryGroup((Integer) key);
			System.out.println("Key:"+key+" group:"+group);
			if (group != lastGroup) {
				JsonObject groupParent = new JsonObject();
				groupParent.addProperty("category", Integer.toString(group));
				groupArray = new JsonArray();
				groupParent.add("children", groupArray);
				allCategories.add(groupParent);
				lastGroup = group;
			}
			JsonObject categoryElement = new JsonObject();
			String categoryTitle = (String) categories.get(key);
			categoryElement.addProperty("category", categoryTitle);
			categoryElement.addProperty("total", categoryTotals.get(key));
			groupArray.add(categoryElement);
			
		}
		return root;
	}
	
	private int categoryGroup(int value) {
		return value / 1000 * 1000;
	}


}
