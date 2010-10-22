package org.ciat.ita.maps.csv2kml;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class CsvFile {

	private String archivo;
	private LinkedList<String[]> lista;

	
	public CsvFile(String arch) {
		archivo=arch;
		
		FileInputStream lee = null;
		String line = null;
		String [] temp;
		int largo=1;
		lista=new LinkedList<String[]>();
		BufferedReader br;
		
			try {
				lee = new FileInputStream(archivo);
			
				DataInputStream datos= new DataInputStream(lee);
				br= new BufferedReader(new InputStreamReader(datos));
		
				line=br.readLine();
				
			temp=line.split(",");
			temp=line.split(",");
			int i=0;
			largo=temp.length-1;

			String[] coorden=new String[2];
			
			line=br.readLine();
			
			while(line!=null){
			//System.out.println(i);		
						
			temp=line.split(",");
			
			//System.out.println("lon: "+temp[largo-1]);
			coorden[0]=temp[largo-1];
			//System.out.println("lat :"+temp[largo]);
			coorden[1]=temp[largo];

			lista.add(coorden.clone());

			i++;
			line = br.readLine();
			
			}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		/*	for (String[] s : lista) {
				
				System.out.println(s[0]+" "+s[1]);
			}*/
			  
		
	}

	public LinkedList<String[]> getLista() {
		return lista;
	}
	
	
	
	
}
			
		
	
	
