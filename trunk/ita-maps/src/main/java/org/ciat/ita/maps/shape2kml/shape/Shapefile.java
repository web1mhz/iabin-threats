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
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;

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
	
	/**
	 * Obtain the ideal point to place a marker in a polygon
	 * @param sf is the simple feature to get the marker point
	 * @return
	 */
	public static Point getPointForMarker(SimpleFeature sf){
			
		MultiPolygon mp;
		Point centroid;
		double distance;
		if(!(sf.getDefaultGeometry() instanceof MultiPolygon)){
			return null;
		}
		mp=(MultiPolygon) sf.getDefaultGeometry();
		centroid=mp.getCentroid();
		distance=mp.distance(centroid);
		if(distance==0){
			return centroid;
		}
			
		double difX=sf.getBounds().getMaxX()-sf.getBounds().getMinX();
		double difY=sf.getBounds().getMaxY()-sf.getBounds().getMinY();
		GeometryFactory gf = new GeometryFactory();
		LineString in;
			
		/* get the point from the center of the narrowest part of the polygon */
		boolean byX=difX<difY;
		if(byX){
			in= gf.createLineString(new Coordinate[]{
					new Coordinate(sf.getBounds().getMinX()+difX, 90),
					new Coordinate(sf.getBounds().getMinX()+difX, -90)});
		}else{
			in= gf.createLineString(new Coordinate[]{
					new Coordinate(180, sf.getBounds().getMinY()+difY),
					new Coordinate(-180, sf.getBounds().getMinY()+difY)});
		}
		
		centroid=mp.intersection(in).getCentroid();
		if(centroid!=null && mp.distance(centroid)==0){
				return centroid;
			}
		/* find a random point inside the polygon */
		while(distance>0){
			Point pointPol = gf.createPoint(new Coordinate(sf.getBounds().getMinX()+(Math.random()*difX)
					, sf.getBounds().getMinY()+(Math.random()*difY)));
			distance=mp.distance(pointPol);
			if(distance==0){
				return pointPol;
			}
				
		}
	
		return null;
	}
	
}
