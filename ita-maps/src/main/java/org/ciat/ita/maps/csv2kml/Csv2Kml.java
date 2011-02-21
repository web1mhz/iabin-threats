package org.ciat.ita.maps.csv2kml;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import org.ciat.ita.maps.utils.PropertiesManager;

public class Csv2Kml {


	static LinkedList<String[]> lista;
	static LinkedList<String[]> listaChull;
	static LinkedList<String[]> listaChullBuff;
	public static Csv2Kml c2k;
	
	
	public static void main(String[] args) {
		
		String estilo="http://wikipedia.agilityhoster.com/estilo.kml#estilo";//se debe agregar properties
		String estilo1="http://wikipedia.agilityhoster.com/estilo.kml#estilo1";//se debe agregar properties
		
		CsvFile file=new CsvFile("d:/csv/101616.csv");
		lista=file.getLista();
		//System.out.println(lista.getFirst());
		
		CsvFile chull=new CsvFile("d:/csv/101616-chull.csv");
		listaChull=chull.getLista();
		CsvFile chullbuff=new CsvFile("d:/csv/101616-chull-buff.csv");
		listaChullBuff=chullbuff.getLista();
		
		
		
		Csv2Point point=new Csv2Point(lista);   
		try {
			point.createKML("d:","point", estilo);
		} catch (FileNotFoundException e) {		e.printStackTrace();		}
		
		
		
		Csv2Polygon pol=new Csv2Polygon(listaChull,listaChullBuff, estilo, estilo1);
		try {
			pol.createKML("d:", "polig", estilo);
		} catch (FileNotFoundException e) {		e.printStackTrace();		}

	}


	}


