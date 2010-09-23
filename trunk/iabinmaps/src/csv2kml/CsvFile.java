package csv2kml;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class CsvFile {

	private String archivo;
	public String coorden[];
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

			coorden=new String[2];
			
			line=br.readLine();
			
			while(line!=null){
			System.out.println(i);		
						
			temp=line.split(",");
			
			System.out.println("largo:"+largo);
			
			System.out.println(temp[largo-1]);
			coorden[0]=temp[largo-1];
			System.out.println(temp[largo]);
			coorden[1]=temp[largo];
			
			System.out.println(coorden[1]);	
			lista.add(coorden);

			System.out.println(lista.getLast());
			System.out.println(lista.getLast());
			
			i++;
			System.out.println("antes");
			line = br.readLine();
			System.out.println("despues");
			
			}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	public LinkedList<String[]> getLista() {
		return lista;
	}
	
	
	
	
}
			
		
	
	
