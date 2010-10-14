package general;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import csv2kml.Csv2Kml;
import csv2kml.Csv2Point;
import csv2kml.Csv2Polygon;
import csv2kml.CsvFile;

public class General {


	static LinkedList<String[]> lista;
	static LinkedList<String[]> listaChull;
	static LinkedList<String[]> listaChullBuff;
	public static Csv2Kml c2k;
	
		public static void main(String[] args) {
			
			String estilo="http://wikipedia.agilityhoster.com/estilo.kml#estilo";//se debe agregar properties
		    //String nombreArchivo= "default"; //nombre de archivo
		    File folder = new File("c:/data/species/");//folder que contiene los archivos a leer "d:/csv/"
			
			//*************************************************************************************
			
		    File[] listOfFiles = folder.listFiles();

		    for (File s : listOfFiles) {
		    
		      if (s.isDirectory()) {
		    	  
		    	  //nombreArchivo= s.getName();
		    	  String ruta=folder.getPath()+File.separator+s.getName()+File.separator+s.getName();
		        System.out.println("File " + ruta);
		      
		        //****************************
		        
				CsvFile file=new CsvFile( ruta+".csv");
				lista=file.getLista();
				//System.out.println(lista.getFirst());
				
				CsvFile chull=new CsvFile(ruta+"-chull.csv");
				listaChull=chull.getLista();
				CsvFile chullbuff=new CsvFile(ruta+"-chull-buff.csv");
				listaChullBuff=chullbuff.getLista();
				
				Csv2Point point=new Csv2Point(lista);   
				try {
					point.createKML("d:/"+s.getName()+File.separator,s.getName());												//cambiar a properties file
				} catch (FileNotFoundException e) {		e.printStackTrace();		}
				
				Csv2Polygon pol=new Csv2Polygon(listaChull,listaChullBuff, estilo);
				try {
					pol.createKML("d:/"+s.getName()+File.separator,s.getName());												//cambiar a properties file
				} catch (FileNotFoundException e) {		e.printStackTrace();		}
		       
		        //***************************
		        
		      } /*else if (s.isDirectory()) {
		        System.out.println("Directory " + s.getName());
		      }*/
		    
		    }
			
			//*************************************************************************************
			
		}
	}


