package org.ciat.ita.maps.shape2kml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.geotools.feature.FeatureIterator;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.opengis.feature.simple.SimpleFeature;

import org.ciat.ita.maps.shape2kml.kml.KmlGroupCreator;
import org.ciat.ita.maps.shape2kml.kml.KmlPolygonCreator;
import org.ciat.ita.maps.shape2kml.kml.XmlPolygonCreator;
import org.ciat.ita.maps.shape2kml.shape.Shapefile;
import org.ciat.ita.maps.utils.PropertiesGenerator;
import org.ciat.ita.maps.utils.PropertiesManager;

import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.Document;

public class Shape2kml {

	private Shapefile shp;
	public static Shape2kml s2k;

	public static void main(String[] args) {
		s2k = new Shape2kml();
		s2k.executeFromProperties(args[0]);
	}
	
	public void executeFromProperties(String propertiesFile) {
		PropertiesManager.register(propertiesFile);
		
		
		
		String[] shapesID = PropertiesManager.getInstance().getPropertiesAsStringArray("shapes");
		String sourcePath = PropertiesManager.getInstance().getPropertiesAsString("path.source");
		String targetPath = PropertiesManager.getInstance().getPropertiesAsString("path.target");
		
		
		for (String shapeID : shapesID) {	
			String group = PropertiesManager.getInstance().getPropertiesAsString(shapeID+".group");
			String pathGroup = PropertiesManager.getInstance().getPropertiesAsString(group+".path");
			String fileName = PropertiesManager.getInstance().getPropertiesAsString(shapeID+".shapefile");
			String urlServer= PropertiesManager.getInstance().getPropertiesAsString(shapeID+".server.url");
			String mainKml = PropertiesManager.getInstance().getPropertiesAsString(shapeID+".kml.main");
			int []columnIndexes = PropertiesManager.getInstance().getPropertiesAsIntArray(shapeID+".shape.column.indexes");
			String[] columnName = PropertiesManager.getInstance().getPropertiesAsStringArray(shapeID+".shape.column.names");
			
			String sourceFile =sourcePath+pathGroup+fileName;
			String targetFile= targetPath+pathGroup+shapeID;
			HashMap<Integer, String> atributos = new HashMap<Integer, String>();
			for (int i = 0; i < columnIndexes.length; i++) {
				atributos.put(columnIndexes[i], columnName[i]);
			}
			
			try {
				s2k.execute(sourceFile, targetFile,urlServer,mainKml,atributos);
			} catch (IOException e) { e.printStackTrace();}
		}
		
		
	}
	

	public void execute(String sourceFile,String targetFile,String urlServer,String mainKml,HashMap<Integer,String> atributos) throws IOException {
		
		
		
		File file = new File(sourceFile);
		
		shp = new Shapefile(file);
		SimpleFeature sf = null;
		KmlGroupCreator grupo = new KmlGroupCreator(urlServer);

		FeatureIterator<SimpleFeature> fi = shp.getFeatures();
		int count = 5;

		KmlPolygonCreator kml = new KmlPolygonCreator(targetFile,atributos);
		XmlPolygonCreator xml = new XmlPolygonCreator(targetFile+"XML", atributos);
		
		
		//*****************************************************************************
		PropertiesGenerator hola=new PropertiesGenerator("d:/prueba.properties");
		try {
			hola.write();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//*****************************************************************************
		
		while (fi.hasNext() && count-- > 0) {
			sf = fi.next();
			try {
				kml.createKML(sf);//writes the KML file
				grupo.addElement(sf.getAttribute(1) + ".kml");
			} catch (FileNotFoundException e) {	e.printStackTrace();
			}

			System.out.print(sf.getAttribute(4) + " ");
			System.out.println(sf.getAttribute(5));

		}

		try {
			grupo.writeKml(targetFile,mainKml);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	//*****************************************************************************	
		//se crea el XML
		
		Document doc = null;
		
		
		
		while (fi.hasNext() && count-- > 0) {
			sf = fi.next();
			try {
				doc=xml.createXML(sf);//writes the XML file
				grupo.addElement(sf.getAttribute(1) + ".kml");
			} catch (FileNotFoundException e) {	e.printStackTrace();
			}

			System.out.print(sf.getAttribute(4) + " ");
			System.out.println(sf.getAttribute(5));

		}
				
		XMLOutputter outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());
		FileOutputStream file2;
		
		file2 = new FileOutputStream("file");
		
		outp.output(doc, file2);

		file2.flush();
		file2.close();
		
		System.out.println("...");
		System.out.print("XML file done ");
		
	}

}
