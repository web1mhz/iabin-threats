package org.ciat.ita.maps.csv2kml;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * @author  supportadmin
 */
public class CsvFile {

	/**
	 * @uml.property  name="archivo"
	 */
	private String archivo;
	/**
	 * @uml.property  name="lista"
	 */
	private LinkedList<String[]> lista;

	public CsvFile(String arch) {
		archivo = arch;

		FileInputStream lee = null;
		String line = null;
		String[] temp;
		int largo = 1;
		lista = new LinkedList<String[]>();
		BufferedReader br;

		try {
			lee = new FileInputStream(archivo);

			DataInputStream datos = new DataInputStream(lee);
			br = new BufferedReader(new InputStreamReader(datos));

			line = br.readLine();

			temp = line.split(",");
			temp = line.split(",");
			int i = 0;
			largo = temp.length - 1;

			String[] coorden = new String[2];

			line = br.readLine();

			while (line != null) {

				temp = line.split(",");
				coorden[0] = temp[largo - 1];
				coorden[1] = temp[largo];
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

	}

	/**
	 * @return
	 * @uml.property  name="lista"
	 */
	public LinkedList<String[]> getLista() {
		return lista;
	}

}
