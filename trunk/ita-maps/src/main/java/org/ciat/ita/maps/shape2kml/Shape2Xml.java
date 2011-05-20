package org.ciat.ita.maps.shape2kml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

import org.ciat.ita.maps.shape2kml.kml.XmlPolygonCreator;
import org.ciat.ita.maps.shape2kml.shape.Shapefile;
import org.ciat.ita.maps.utils.PropertiesManager;

/**
 * @author  supportadmin
 */
public class Shape2Xml {

	/**
	 * @uml.property  name="shp"
	 * @uml.associationEnd  
	 */
	private Shapefile shp;
	/**
	 * @uml.property  name="s2x"
	 * @uml.associationEnd  
	 */
	public static Shape2Xml s2x;

	public static void main(String[] args) {
		s2x = new Shape2Xml();
		s2x.executeFromProperties(args[0]);
	}

	public void executeFromProperties(String propertiesFile) {
		PropertiesManager.register(propertiesFile);

		String[] shapesID = PropertiesManager.getInstance().getPropertiesAsStringArray("shapes");
		String sourcePath = PropertiesManager.getInstance().getPropertiesAsString("path.source");
		String targetPath = PropertiesManager.getInstance().getPropertiesAsString("path.target");

		for (String shapeID : shapesID) {
			String group = PropertiesManager.getInstance().getPropertiesAsString(shapeID + ".group");
			String pathGroup = PropertiesManager.getInstance().getPropertiesAsString(group + ".path");
			String fileName = PropertiesManager.getInstance().getPropertiesAsString(shapeID + ".shapefile");
			int[] columnIndexes = PropertiesManager.getInstance().getPropertiesAsIntArray(
					shapeID + ".shape.column.indexes");
			String[] columnName = PropertiesManager.getInstance().getPropertiesAsStringArray(
					shapeID + ".shape.column.names");

			String sourceFile = sourcePath + pathGroup + fileName;
			String targetFile = targetPath + pathGroup + shapeID;
			HashMap<Integer, String> atributos = new HashMap<Integer, String>();
			for (int i = 0; i < columnIndexes.length; i++) {
				atributos.put(columnIndexes[i], columnName[i]);
			}

			try {
				s2x.execute(sourceFile, targetFile, atributos);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void execute(String sourceFile, String targetFile, HashMap<Integer, String> atributos)
			throws IOException {

		File file = new File(sourceFile);

		shp = new Shapefile(file);
		FeatureIterator<SimpleFeature> fi = shp.getFeatures();
		XmlPolygonCreator xml = new XmlPolygonCreator(targetFile + "XML", atributos);

		// *****************************************************************************
		// se crea el XML
		System.out.println("creating XML ... ");

		xml.createXML(fi);// writes the XML file

		System.out.println("...");
		System.out.print("XML file done ");

	}

}
