package org.ciat.ita.maps.general;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

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

import com.vividsolutions.jts.geom.Point;

import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

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
			if(args.length>0){
				s2k.executeFromProperties(args[0]);
			}else{				
				//System.out.println("Please provide the path to the iabin.properties file");
				PropertiesGenerator propGen = new PropertiesGenerator("iabin.properties");
				try {
					if(propGen.exists()){
						s2k.executeFromProperties("iabin.properties");
					//System.out.println("the file \"default-iabin.properties\" already exists on same folder as this program is \n nothing done. \n if you want to create a new one, please delete the one already exists");
					}else{
						propGen.write();
						System.out.println("the file \"iabin.properties\" has been created on same folder as this program is");
					}
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			
			}
			
			
	
		} catch (ArrayIndexOutOfBoundsException e1) {
			System.out.println("__Please provide the path to the iabin.properties file or execute the script without arguments");
			e1.getMessage();
		} catch (NullPointerException e3) {
			e3.getMessage();
			System.out.println("some lines missing in "+args[0]+" file, add the line \"language=english\" to the "+args[0]+" file");
		}
	}

	public void executeFromProperties(String propertiesFile)
			throws NullPointerException {
		this.propertiesFile = propertiesFile;
		PropertiesManager.register(propertiesFile);

		String[] shapesID = PropertiesManager.getInstance().getPropertiesAsStringArray("shapes");
		String sourcePath = PropertiesManager.getInstance().getPropertiesAsString("path.source");
		if(!sourcePath.endsWith(File.separator)) sourcePath=sourcePath+File.separator;
		String targetPath = PropertiesManager.getInstance().getPropertiesAsString("path.target");
		if(targetPath.endsWith(File.pathSeparator))	targetPath=targetPath+File.separator;
		String species = PropertiesManager.getInstance().getPropertiesAsString("species.path");
		String language = PropertiesManager.getInstance().getPropertiesAsString("language");

		for (String shapeID : shapesID) {
			String group = PropertiesManager.getInstance().getPropertiesAsString(shapeID + ".group");
			String pathGroup = PropertiesManager.getInstance().getPropertiesAsString(group + ".path");
			String fileName = PropertiesManager.getInstance().getPropertiesAsString(shapeID + ".shapefile");
			String urlServer = PropertiesManager.getInstance().getPropertiesAsString(shapeID + ".server.url");
			String mainKml = PropertiesManager.getInstance().getPropertiesAsString(shapeID + ".kml.main");
			int[] columnIndexes = PropertiesManager.getInstance().getPropertiesAsIntArray(shapeID + ".shape.column.indexes");
			String[] columnName = PropertiesManager.getInstance().getPropertiesAsStringArray(shapeID + ".shape.column.names");

			String sourceFile = sourcePath + pathGroup + fileName;
			String targetFile = targetPath + pathGroup + shapeID;
			System.out.println("targetfile: "+targetFile);
			HashMap<Integer, String> atributos = new HashMap<Integer, String>();
			for (int i = 0; i < columnIndexes.length; i++) {
				atributos.put(columnIndexes[i], columnName[i]);
			}

			s2k.execute(sourceFile, targetFile, urlServer, mainKml, atributos,
					sourcePath, targetPath, species, language);
		}

	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	private void execute(String sourceFile, String targetFile,
			String urlServer, String mainKml,
			HashMap<Integer, String> atributos, String sourcepath,
			String targetpath, String species, String language)
			throws NullPointerException {


		int count = 5;

		KmlPolygonCreator kml = new KmlPolygonCreator(targetFile, atributos);

		if (language.equals("espanol") || language.equals("espa�ol")) {
			System.out.println("bienvenido \n seleccione la opcion que desee");
			System.out.println("1. crear el archivo properties");
			System.out.println("2. convierte archivo .shp a kml, protected areas");
			System.out.println("3. convierte archivo .csv a kml; puntos y pol�gonos - ocurrencias y chull, chull-buff");
			System.out.println("4. convierte archivo .asc a png, variables bioclimaticas");
			System.out.println("5. convierte archivo .asc a png, distribuci�n de especies");
			System.out.println("6. realiza todos los procesos anteriores");
			System.out.println("para mas info consulta en la wiki del proyecto http://code.google.com/p/iabin-threats/wiki/DataConversion");
		}
		if (language.equals("english")) {
			System.out.println("welcome \n select the option");
			System.out.println("1. create default properties file");
			System.out.println("2. convert file .shp to kml, protected areas");
			System.out.println("3. convert file .csv to kml; points and polygons - ocurrences and chull, chull-buff");
			System.out.println("4. convert file .asc to png, variables bioclimaticas");
			System.out.println("5. convert file .asc to png, species distribution");
			System.out.println("6. performs all the previous tasks");
			System.out.println("for more info visit the project's wiki page at http://code.google.com/p/iabin-threats/wiki/DataConversion");
		}
		String option;
		String horaEmpieza = this.getDateTime();
		if (language.equals("english"))	System.out.println("started at : " + horaEmpieza);
		if (language.equals("espanol") || language.equals("espa�ol"))System.out.println("empieza a las : " + horaEmpieza);
		if (language.equals("english"))	System.out.print("please select an option :");
		if (language.equals("espanol") || language.equals("espa�ol"))System.out.print("por favor seleccione una opci�n :");

		Scanner in = new Scanner(System.in);
		option = in.nextLine();

		int opt = Integer.parseInt(option);
		if (opt == 1 || opt == 6)

		{
			if (language.equals("english"))	System.out.println("you selected option :" + option);
			if (language.equals("espanol") || language.equals("espa�ol"))System.out.println("usted escogi� la opci�n :" + option);

			// *****************************************************************************
			// esta secci�n crea el archivo properties con la configuraci�n por
			// defecto
			PropertiesGenerator propGen = new PropertiesGenerator(targetpath+ "default-iabin.properties");
			try {
				propGen.write();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}// cierra el case1
		// *****************************************************************************
		// protected areas shape to kml

		if (opt == 2 || opt == 6)
		{		
			if (language.equals("english"))	System.out.println("you selected option :" + option);
			if (language.equals("espanol") || language.equals("espa�ol"))System.out.println("usted escogi� la opci�n :" + option);

			File file = new File(sourceFile);//loads the shape file to read
			System.out.println("folder of shape file: "+sourceFile);
			System.out.println("file: "+file);
			shp = new Shapefile(file);
			
			SimpleFeature sf = null;
			KmlGroupCreator grupo = new KmlGroupCreator(urlServer);

			FeatureIterator<SimpleFeature> fi = shp.getFeatures();

	/*		while (fi.hasNext()) {// && count-- > 0) {
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
*/
			
			final Kml kml2 = new Kml();
			Folder folder = kml2.createAndSetFolder();	
			
			String ruta = targetFile+File.separator+ "total-info.kml";
			System.out.println("ruta: "+ruta);
			
			
			
			while (fi.hasNext() ) {// && count-- > 0) {
				sf = fi.next();
				Set<Integer> keySet = atributos.keySet();
				String descripcion = "<table border=\"1\" padding=\"3\" width=\"300\"><tr bgcolor= \"#D2D2D2\">";

				for (Integer i : keySet) {//se crean los titulos de la tabla de la informacion del poligono
					descripcion+= "<td>"+atributos.get(i)+"</td>";
				}
				descripcion+="</tr><tr>";
				for (Integer i : keySet) {//se crean los contenidos de la tabla de la informacion del poligono
					descripcion+= "<td>"+sf.getAttribute(i)+"</td>";
				}
				descripcion+="</tr></table>";

				Placemark placemark = KmlFactory.createPlacemark();
				String estilo = PropertiesManager.getInstance().getPropertiesAsString("style.url");
				/* se recorre la lista generando las coordenadas y agregando al folder		*/
				placemark = folder.createAndAddPlacemark();//se crea el placemark y se a�ade al folder	
				placemark.withName(atributos.get(1)).withDescription(descripcion).withStyleUrl(estilo);	
				Point punto=Shapefile.getPointForMarker(sf);	
				//se crean las coordenadas y se registran al placemark
				placemark.createAndSetPoint().addToCoordinates(punto.getX(), punto.getY() );
	
			}//fin while		
	/*			try 
				{	grupo.writeKml(targetFile, mainKml);
				} catch (FileNotFoundException e) {
					e.printStackTrace();				}
*/
			kml2.setFeature(folder);//se registra el folder al kml
			
			File dir = new File(targetFile);
			dir.mkdirs();			
			//kml.marshal();//se imprime kml en consola
			try {
				kml2.marshal(new File(ruta));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//se guarda kml en archivo
		
			
		}// fin case 2
		// **********************************************************************************************************
		// *** esta secci�n se encarga de recorrer la carpeta data y cargar los
		// archivos csv para convertir a kml
		// puntos y pol�gonos - ocurrencias y chull, chull-buff

		if (opt == 3 || opt == 6)

		{
			if (language.equals("english"))	System.out.println("you selected option :" + option);
			if (language.equals("espanol") || language.equals("espa�ol"))System.out.println("usted escogi� la opci�n :" + option);

			// String
			// estilo="http://wikipedia.agilityhoster.com/estilo.kml#estilo";//se
			// debe agregar properties
			String estilo = PropertiesManager.getInstance().getPropertiesAsString("style.url");
			String estilo1 = PropertiesManager.getInstance().getPropertiesAsString("style1.url");

			// String nombreArchivo= "default"; //nombre de archivo
			// File folder = new File("c:/data/species/");//folder que contiene
			// los archivos a leer "d:/csv/"
			File folder = new File(sourcepath + species);
			// *************************************************************************************

			File[] listOfFiles = folder.listFiles();

			int contador=100;
			for (File s : listOfFiles) {

				if (s.isDirectory()) {

					// nombreArchivo= s.getName();
					String ruta = folder.getPath() + File.separator+ s.getName() + File.separator + s.getName();
					if(contador%100==0)	
						{
						System.out.println("File " + ruta);
						System.gc();
						}
					contador++;
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
						point.createKML(targetpath + species + s.getName()+ File.separator, s.getName(), estilo); // cambiar a
						// properties
						// file
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					Csv2Polygon pol = new Csv2Polygon(listaChull,listaChullBuff, estilo, estilo1);
					try {
						//pol.createKML(targetpath + species + s.getName()+ File.separator, s.getName(), style1); // cambiar a properties file
						//System.out.println("estilo :"+ estilo);
						pol.createKMLchull(targetpath+species+s.getName()+File.separator,s.getName(), estilo);  //crea kml chull
						//System.out.println("estilo1 :"+ estilo1);
						pol.createKMLchullbuff(targetpath+species+s.getName()+File.separator,s.getName(), estilo1);  //crea kml chull buff
						
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

				}

			}
			

		} // fin case3

		// *************************************************************************************
		// bioclim variables

		if (opt == 4 || opt == 6)

		{
			if (language.equals("english"))	System.out.println("you selected option :" + option);
			if (language.equals("espanol") || language.equals("espa�ol"))System.out.println("usted escogi� la opci�n :" + option);

			try {
				TileCutter.execute(propertiesFile);
			} catch (IOException e) {
				System.out.println("file not found");
				e.getMessage();
				e.printStackTrace();
			}

		} // fin case 4
		
		//**********************************
		// se crean las imagenes de distribucion de especies a partir de los rasters .asc
		if (opt == 5 || opt == 6)
		{
			
			if (language.equals("english"))	System.out.println("you selected option :" + option);
			if (language.equals("espanol") || language.equals("español"))System.out.println("usted escogió la opción :" + option);
			
			if(opt == 5) {
			
				if (language.equals("english"))	System.out.println("Do you want to run the script for all species? y/n :");
				if (language.equals("espanol") || language.equals("español"))System.out.println("Desea correr el algoritmo para todas las especies? s/n" + option);
				option = in.nextLine();

				if(option.equalsIgnoreCase("n")) {
						if (language.equals("english"))	System.out.println("Please write the minimum and maximum species directory separated by (-), format: 78465-98750543:");
						if (language.equals("espanol") || language.equals("español"))System.out.println("Por favor, indique el código de espécie mínimo y máximo, formato: 78465-98750543");
						option = in.nextLine();					
						String[] minMax = option.split("-");
						if(minMax.length == 2) {
							String min = minMax[0];
							String max = minMax[1];
							try {
								TileCutter.createSpeciesDistributionImages(propertiesFile, min, max);
							} catch (IOException e) {
								System.out.println("file not found");
								e.getMessage();
								e.printStackTrace();
							}
						} else {
							if (language.equals("english"))	System.out.println("Invalid format!");
							if (language.equals("espanol") || language.equals("español"))System.out.println("Formato incorrecto!");
						}
				} else {
					try {
						TileCutter.createSpeciesDistributionImages(propertiesFile, null, null);
					} catch (IOException e) {
						System.out.println("file not found");
						e.getMessage();
						e.printStackTrace();
					}
				}
			} else {			
				try {
					TileCutter.createSpeciesDistributionImages(propertiesFile, null, null);
				} catch (NumberFormatException e) {
					System.out.println("format error");
					e.getMessage();
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("file not found");
					e.getMessage();
					e.printStackTrace();
				}
			}
			
		}//fin case 5

		String horaTermina = this.getDateTime();
		if (language.equals("espanol") || language.equals("espa�ol"))System.out.println("empez� a las : " + horaEmpieza + "\r\n"+ " finaliz� a las : " + horaTermina);
		if (language.equals("english"))System.out.println("started at : " + horaEmpieza + "\r\n"+ " ended at : " + horaTermina);

	}// fin switch

}
