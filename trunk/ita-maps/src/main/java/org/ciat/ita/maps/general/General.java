package org.ciat.ita.maps.general;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

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

		try {
			s2k.executeFromProperties(args[0]);
		} catch (ArrayIndexOutOfBoundsException e1) {
			System.out.println("Please provide the path to the iabin.properties file");
			PropertiesGenerator hola = new PropertiesGenerator(
					"default-iabin.properties");
			try {
				hola.write();
				System.out
						.println("the file \"default-iabin.properties\" has been created on same folder as this program is ");
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			e1.getMessage();
		} catch (NullPointerException e3) {
			System.out.println("add the line \"language=english\" to the iabin.properties file");
		}
	}

	public void executeFromProperties(String propertiesFile) throws NullPointerException{
		this.propertiesFile = propertiesFile;
		PropertiesManager.register(propertiesFile);

		String[] shapesID = PropertiesManager.getInstance()
				.getPropertiesAsStringArray("shapes");
		String sourcePath = PropertiesManager.getInstance()
				.getPropertiesAsString("path.source");
		String targetPath = PropertiesManager.getInstance()
				.getPropertiesAsString("path.target");
		String species = PropertiesManager.getInstance().getPropertiesAsString(
				"species.path");
		String language = PropertiesManager.getInstance()
				.getPropertiesAsString("language");

		for (String shapeID : shapesID) {
			String group = PropertiesManager.getInstance()
					.getPropertiesAsString(shapeID + ".group");
			String pathGroup = PropertiesManager.getInstance()
					.getPropertiesAsString(group + ".path");
			String fileName = PropertiesManager.getInstance()
					.getPropertiesAsString(shapeID + ".shapefile");
			String urlServer = PropertiesManager.getInstance()
					.getPropertiesAsString(shapeID + ".server.url");
			String mainKml = PropertiesManager.getInstance()
					.getPropertiesAsString(shapeID + ".kml.main");
			int[] columnIndexes = PropertiesManager.getInstance()
					.getPropertiesAsIntArray(shapeID + ".shape.column.indexes");
			String[] columnName = PropertiesManager
					.getInstance()
					.getPropertiesAsStringArray(shapeID + ".shape.column.names");

			String sourceFile = sourcePath + pathGroup + fileName;
			String targetFile = targetPath + pathGroup + shapeID;
			HashMap<Integer, String> atributos = new HashMap<Integer, String>();
			for (int i = 0; i < columnIndexes.length; i++) {
				atributos.put(columnIndexes[i], columnName[i]);
			}

			s2k.execute(sourceFile, targetFile, urlServer, mainKml, atributos,
					sourcePath, targetPath, species, language);
		}

	}

	private void execute(String sourceFile, String targetFile,
			String urlServer, String mainKml,
			HashMap<Integer, String> atributos, String sourcepath,
			String targetpath, String species, String language) throws NullPointerException {

		File file = new File(sourceFile);

		shp = new Shapefile(file);
		SimpleFeature sf = null;
		KmlGroupCreator grupo = new KmlGroupCreator(urlServer);

		FeatureIterator<SimpleFeature> fi = shp.getFeatures();
		int count = 5;

		KmlPolygonCreator kml = new KmlPolygonCreator(targetFile, atributos);

		if (language.equals("espanol")||language.equals("español")) {
			System.out.println("bienvenido \n seleccione la opcion que desee");
			System.out.println("1. crear el archivo properties");
			System.out.println("2. convierte archivo .shp a kml, protected areas");
			System.out.println("3. convierte archivo .csv a kml; puntos y polígonos - ocurrencias y chull, chull-buff");
			System.out.println("4. convierte archivo .asc a png, variables bioclimaticas");
			System.out.println("5. realiza todos los procesos anteriores");
			System.out.println("para mas info consulta en la wiki del proyecto http://code.google.com/p/iabin-threats/wiki/DataConversion");
		}
		if (language.equals("english")) {
			System.out.println("welcome \n select the option");
			System.out.println("1. create default properties file");
			System.out.println("2. convert file .shp to kml, protected areas");
			System.out.println("3. convert file .csv to kml; points and polygons - ocurrences and chull, chull-buff");
			System.out.println("4. convert file .asc to png, variables bioclimaticas");
			System.out.println("5. performs all the previous tasks");
			System.out.println("for more info visit the project's wiki page at http://code.google.com/p/iabin-threats/wiki/DataConversion");
		}
		String option;
		Scanner in = new Scanner(System.in);
		option = in.nextLine();

		int opt = Integer.parseInt(option);
		if (opt == 1 || opt == 5)

		{
			System.out.println("you selected option :" + option);

			// *****************************************************************************
			// esta sección crea el archivo properties con la configuración por
			// defecto
			PropertiesGenerator hola = new PropertiesGenerator(targetpath
					+ "default-iabin.properties");
			try {
				hola.write();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}// cierra el case1
		// *****************************************************************************
		// protected areas shape to kml

		if (opt == 2 || opt == 5)

		{
			System.out.println("you selected option :" + option);

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
				grupo.writeKml(targetFile, mainKml);
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}

		}// fin case 2
		// **********************************************************************************************************
		// *** esta sección se encarga de recorrer la carpeta data y cargar los
		// archivos csv para convertir a kml
		// puntos y polígonos - ocurrencias y chull, chull-buff

		if (opt == 3 || opt == 5)

		{
			System.out.println("you selected option :" + option);

			// String
			// estilo="http://wikipedia.agilityhoster.com/estilo.kml#estilo";//se
			// debe agregar properties
			String estilo = PropertiesManager.getInstance()
					.getPropertiesAsString("style.url");
			String style1 = PropertiesManager.getInstance()
					.getPropertiesAsString("style1.url");

			// String nombreArchivo= "default"; //nombre de archivo
			// File folder = new File("c:/data/species/");//folder que contiene
			// los archivos a leer "d:/csv/"
			File folder = new File(sourcepath + species);
			// *************************************************************************************

			File[] listOfFiles = folder.listFiles();

			for (File s : listOfFiles) {

				if (s.isDirectory()) {

					// nombreArchivo= s.getName();
					String ruta = folder.getPath() + File.separator
							+ s.getName() + File.separator + s.getName();
					System.out.println("File " + ruta);

					// ****************************

					CsvFile file1 = new CsvFile(ruta + ".csv");
					lista = file1.getLista();
					// System.out.println(lista.getFirst());

					CsvFile chull = new CsvFile(ruta + "-chull.csv");
					listaChull = chull.getLista();
					CsvFile chullbuff = new CsvFile(ruta + "-chull-buff.csv");
					listaChullBuff = chullbuff.getLista();

					Csv2Point point = new Csv2Point(lista);
					try {
						point.createKML(targetpath + species + s.getName()
								+ File.separator, s.getName()); // cambiar a
						// properties
						// file
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					Csv2Polygon pol = new Csv2Polygon(listaChull,
							listaChullBuff, estilo);
					try {
						pol.createKML(targetpath + species + s.getName()
								+ File.separator, s.getName(), style1); // cambiar
						// a
						// properties
						// file
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

				}

			}

		} // fin case3

		// *************************************************************************************
		// bioclim variables

		if (opt == 4 || opt == 5)

		{
			System.out.println("you selected option :" + option);

			try {
				TileCutter.execute(propertiesFile);
			} catch (IOException e) {
				System.out.println("file not found");
				e.getMessage();
				e.printStackTrace();
			}

		} // fin case 4

	}// fin switch

}
