package org.ciat.ita.maps.tilecutter.raster;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Hashtable;

import org.ciat.ita.maps.tilecutter.projection.Projection;
import org.ciat.ita.maps.tilecutter.projection.WGS84;

public class Header {
	
	private float noData;
	private int ncol, nfil;
	private Projection projection;
	
	/**
	 * constructor que crea el header leyendo los datos del archivo
	 * @param reader
	 */

	public Header(BufferedReader reader) {

		try {
			loadFile(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor que recibe un objeto projection
	 * @param projection
	 */
	
	public Header(Projection projection) {
		this.projection = projection;
	}
	
	/**
	 * constructor del header , recibe filas, columnas, valor de noData, y la projection
	 * @param ncol
	 * @param nfil
	 * @param noData
	 * @param projection
	 */
	
	public Header(int ncol, int nfil, float noData, Projection projection) {
		this.ncol = ncol;
		this.nfil = nfil;
		this.noData = noData;
		this.projection = projection;
	}
  
	/**
	 * Toma un archivo raster y lee el header
	 * @param reader
	 * @throws IOException
	 */
	
	public void loadFile(BufferedReader reader) throws IOException {
		Hashtable<String, String> head = new Hashtable<String, String>();

		System.out.println("Reading header");
		// read all headers
		for (int i = 0; i < 6; i++) {
			String line = reader.readLine();
			line = line.replaceAll("\\s+", " ");
			final String[] tmp = line.split(" ");
			System.out.println("\t- " + tmp[0] + " : " + tmp[1]);
			head.put(tmp[0], tmp[1]);
		}
		System.out.println("Header read...");

		ncol = Integer.parseInt(head.get("ncols"));
		nfil = Integer.parseInt(head.get("nrows"));

		noData = Short.parseShort(head.get("NODATA_value"));
		
		projection = new WGS84 (Double.parseDouble(head.get("xllcorner")),  
				Double.parseDouble(head.get("yllcorner")),  
				Double.parseDouble(head.get("cellsize")),  
				Double.parseDouble(head.get("cellsize")));
		
		((WGS84)projection).setHeader(this);
	}
	/**
	 *  Convierte la longitud a valor de X			
	 * @param lon : valor de la longitud
	 * @return valor de X
	 */
	public int lon2x(double lon) {           
		return projection.lon2x(lon); 
	}
	/**
	 * Convierte la latitud a valor de Y
	 * @param lat : valor de la latitud
	 * @return valor de Y.
	 */
	
	public int lat2y(double lat) {          
		return projection.lat2y(lat); 
	}
	
	/**
	 * Convierte el valor de X a longitud
	 * @param x : Valor de X 
	 * @return Valor de la longitud
	 */
	public double x2lon(int x) {           
		return projection.x2lon(x); 
	}
	
	/**
	 * Convierte el valor de Y a latitud
	 * @param y : Valor de Y 
	 * @return Valor de la latitud
	 */
	public double y2lat(int y) {          
		return projection.y2lat(y); 
	}
	
	/**
	 * permite obtener la longitud de la esquina superior izquierda del raster
	 * @return longitud
	 * xll
	 */
	public double rasterSupIzqLon() {
		return projection.rasterSupIzqLon();
	}
	
	/**
	 * permite obtener la latitud de la esquina superior izquierda del raster
	 * @return la latitud
	 */
	public double rasterSupIzqLat() {
		return projection.rasterSupIzqLat(); //yll+getHeight()*cellSizeY;
	}
	
	/**
	 * permite obtener la longitud de la esquina inferior derecha del raster
	 * @return longitud
	 */
	public double rasterInfDerLon() {
		return projection.rasterInfDerLon(); //xll+getWidth()*cellSizeX;
	}
	
	/**
	 * permite obtener la latitud de la esquina inferior derecha del raster
	 * @return la latitud
	 */
	public double rasterInfDerLat() {
		return projection.rasterInfDerLat(); //yll;
	}
	
	/**
	 * Devuelve la longitud y latitud de la esquina superior izquierda del pixel
	 * @param x
	 * @param y
	 * @return longitud y latitud de la esquina superior izquierda del pixel
	 */
	public double[] pixelSupIzq(int x, int y) {
				
		//double[] a = {x2lon(x),y2lat(y)+cellSizeY};
		
		return projection.pixelSupIzq(x, y);	//a;
	}
	
	/**
	 * Devuelve la longitud y latitud de la esquina inferior derecha del pixel
	 * @param x
	 * @param y
	 * @return longitud y latitud de la esquina inferior derecha del pixel
	 */
	public double[] pixelInfDer(int x, int y) {
		
		//double[] a = {x2lon(x)+cellSizeX,y2lat(y)};
		
		return projection.pixelInfDer(x, y); //a;
	}

	/**
	 * permite obtener el numero de filas
	 * @return el numero de filas del raster
	 */
	public int getHeight() {
		return nfil;
	}

	/**
	 * permite obtener el numero de columnas
	 * @return el numero de columnas del raster
	 */
	 
	public int getWidth() {
		return ncol;
	}
/**
 * permite obtener el valor de noData
 * @return valor de noData
 */
	public float getNoData() {
		return noData;
	}
}
