package csv2kml;

import java.io.FileNotFoundException;
import java.util.LinkedList;

public class Csv2Kml {


	static LinkedList<String[]> lista;
	
	public static void main(String[] args) {
		
		
		CsvFile file=new CsvFile("d:/csv/101616.csv");

		lista=file.getLista();
		System.out.println(lista.getFirst());
		
		Csv2Point point=new Csv2Point(lista);
		try {
			point.createKML("d:");
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		Csv2Polygon pol=new Csv2Polygon();
		

	}


	}


