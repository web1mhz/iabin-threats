package org.ciat.ita.maps.general;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

import org.ciat.ita.maps.shape2kml.kml.KmlGroupCreator;
import org.ciat.ita.maps.shape2kml.kml.KmlPolygonCreator;
import org.ciat.ita.maps.shape2kml.shape.Shapefile;
import org.ciat.ita.maps.tilecutter.TileCutter;
import org.ciat.ita.maps.utils.PropertiesGenerator;
import org.ciat.ita.maps.utils.PropertiesManager;

import org.ciat.ita.maps.csv2kml.Csv2Kml;
import org.ciat.ita.maps.csv2kml.Csv2Point;
import org.ciat.ita.maps.csv2kml.Csv2Polygon;
import org.ciat.ita.maps.csv2kml.CsvFile;

public class General {


	static LinkedList<String[]> lista;
	static LinkedList<String[]> listaChull;
	static LinkedList<String[]> listaChullBuff;
	public static Csv2Kml c2k;
	
	
	private Shapefile shp;
	private String propertiesFile;
	public static General s2k;

	public static void main(String[] args) {
		s2k = new General();
		
		try{
		s2k.executeFromProperties(args[0]);
		}catch(ArrayIndexOutOfBoundsException e1){
			System.out.println("Please provide the path to the iabin.properties file");
			e1.getMessage();
		}
	}
	
		public void executeFromProperties(String propertiesFile) {
			this.propertiesFile=propertiesFile;
			PropertiesManager.register(propertiesFile);
			
			
			
			String[] shapesID = PropertiesManager.getInstance().getPropertiesAsStringArray("shapes");
			String sourcePath = PropertiesManager.getInstance().getPropertiesAsString("path.source");
			String targetPath = PropertiesManager.getInstance().getPropertiesAsString("path.target");
			String species=PropertiesManager.getInstance().getPropertiesAsString("species.path");
			
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
				
				s2k.execute(sourceFile, targetFile,urlServer,mainKml,atributos, sourcePath, targetPath, species);
			}
			
			
		}
	private void execute(String sourceFile, String targetFile,String urlServer, String mainKml,
				HashMap<Integer, String> atributos, String sourcepath, String targetpath, String species) {
			// TODO Auto-generated method stub

		
		File file = new File(sourceFile);
		
		shp = new Shapefile(file);
		SimpleFeature sf = null;
		KmlGroupCreator grupo = new KmlGroupCreator(urlServer);

		FeatureIterator<SimpleFeature> fi = shp.getFeatures();
		int count = 5;

		KmlPolygonCreator kml = new KmlPolygonCreator(targetFile,atributos);
		
		//*****************************************************************************
		// esta sección crea el archivo properties con la configuración por defecto
		PropertiesGenerator hola=new PropertiesGenerator(targetpath+"default-iabin.properties");
		try {
			hola.write();
		} catch (IOException e1) {
			// TODO Auto-generated catch block			
			e1.printStackTrace();
		}
		//*****************************************************************************
		//protected areas shape to kml
		while (fi.hasNext() && count-- > 0) {
			sf = fi.next();
			try {
				kml.createKML(sf);
				grupo.addElement(sf.getAttribute(1) + ".kml");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			System.out.print(sf.getAttribute(4) + " ");
			System.out.println(sf.getAttribute(5));

		}

		try {
			grupo.writeKml(targetFile,mainKml);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	//**********************************************************************************************************
		//***  esta sección se encarga de recorrer la carpeta data y cargar los archivos csv para convertir a kml
		// puntos y polígonos - ocurrencias y chull, chull-buff
			
			//String estilo="http://wikipedia.agilityhoster.com/estilo.kml#estilo";//se debe agregar properties
			String estilo=PropertiesManager.getInstance().getPropertiesAsString("style.url");
			String style1=PropertiesManager.getInstance().getPropertiesAsString("style1.url");
			
		    //String nombreArchivo= "default"; //nombre de archivo
		    //File folder = new File("c:/data/species/");//folder que contiene los archivos a leer "d:/csv/"
			File folder = new File(sourcepath+species);
			//*************************************************************************************
			
		    File[] listOfFiles = folder.listFiles();

		    for (File s : listOfFiles) {
		    
		      if (s.isDirectory()) {
		    	  
		    	  //nombreArchivo= s.getName();
		    	  String ruta=folder.getPath()+File.separator+s.getName()+File.separator+s.getName();
		        System.out.println("File " + ruta);
		      
		        //****************************
		        
				CsvFile file1=new CsvFile( ruta+".csv");
				lista=file1.getLista();
				//System.out.println(lista.getFirst());
			 	
				CsvFile chull=new CsvFile(ruta+"-chull.csv");
				listaChull=chull.getLista();
				CsvFile chullbuff=new CsvFile(ruta+"-chull-buff.csv");
				listaChullBuff=chullbuff.getLista();
				
				Csv2Point point=new Csv2Point(lista);   
				try {
					point.createKML(targetpath+species+s.getName()+File.separator,s.getName());												//cambiar a properties file
				} catch (FileNotFoundException e) {		e.printStackTrace();		}
				
				Csv2Polygon pol=new Csv2Polygon(listaChull,listaChullBuff, estilo);
				try {
					pol.createKML(targetpath+species+s.getName()+File.separator,s.getName(), style1);												//cambiar a properties file
				} catch (FileNotFoundException e) {		e.printStackTrace();		}
		       
		        
		      } 
		    
		    }
			
			//*************************************************************************************
			//bioclim variables
		    
		    try {
				TileCutter.execute(propertiesFile);
			} catch (IOException e) {
				System.out.println("file not found" );
				e.getMessage();
				e.printStackTrace();
			}
		    
		    
		    
		    
		    
		    
		    
		}
	}


