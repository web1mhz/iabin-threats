package org.ciat.ita.maps.shape2kml.shape;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


public class Shapefile {

	private String typeName = "";
	private FeatureCollection<SimpleFeatureType, SimpleFeature> collection;
	private DataStore dataStore;
	private FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;
	private Map<String, Serializable> connectParameters;

	/**
	 * 
	 * @param shapefile
	 *            the file with .SHP format of the Shape File
	 */
	public Shapefile(File shapefile) {
		init(shapefile);
	}

	private boolean init(File file) {
		connectParameters = new HashMap<String, Serializable>();
		try {
			connectParameters.put("url", file.toURI().toURL());
			connectParameters.put("create spatial index", true);
			dataStore = DataStoreFinder.getDataStore(connectParameters);
			String[] typeNames = dataStore.getTypeNames();
			typeName = typeNames[0];
			featureSource = dataStore.getFeatureSource(typeName);
			collection = featureSource.getFeatures();

			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public FeatureIterator<SimpleFeature> getFeatures() {
		try {
			return featureSource.getFeatures().features();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get the collection limits of the features in the shape file
	 * 
	 * @return
	 */
	public ReferencedEnvelope collectionLimits() {
		return collection.getBounds();
	}
}
