package org.ciat.ita.maps.tilecutter.raster;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.ciat.ita.maps.tilecutter.projection.Projection;

public class Raster {
	
	private float[][] grid;
	private Header header;
	
	private float min = Float.MAX_VALUE;
	private float max = Float.MIN_VALUE;
	
	private boolean empty = true;
	
	
	public Raster() {}
	
	/**
	 * Constructor del raster
	 * @param ncol : numero de filas
	 * @param nfil : numero de columnas
	 * @param noData
	 * @param projection : interfaz que implementan las clases WGS84 o Mercator.
	 */
	
	public Raster(int ncol, int nfil, float noData, Projection projection) {
		
		header = new Header(ncol,nfil,noData,projection);//
		grid = new float[header.getWidth()][header.getHeight()];
	}
	
	/**
	 * Obtiene el valor del dato en la posicion  XY
	 * @param x
	 * @param y
	 * @return Matriz
	 */
	public float getValue(int x, int y) {
		if(x < 0 || x >= grid.length || y < 0 || y >= grid[0].length)
			return header.getNoData();
		
		return grid[x][y];
	}
	
	/**
	 * Permite modificar un valor en la matriz
	 * @param x
	 * @param y
	 * @param value
	 */
	public void setValue(int x, int y, float value, float factor) {
		if(value != header.getNoData()) {
			value *= factor;
			if(value > max)
				max = value;
			if(value < min)
				min = value;
			
			empty = false;
		}
			
		
		grid[x][y] = value;
	}
	
	/**
	 * Obtiene el valor de longitud y latitud
	 * @param lat
	 * @param lon
	 * @return el valor de la longitud y la latitud
	 */

	public float getValuell(float lat, float lon) {
		return getValue(header.lon2x(lon),header.lat2y(lat));
	}
	/**
	 * Carga la matriz de memoria
	 * @param raster
	 * Recibe el raster, lo recorre y le pone un valor
	 */
	
	public void loadRaster(Raster raster) {
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[x].length; y++) {

				setValue(x,y,raster.calculaValorPixel(header.pixelSupIzq(x, y),
						header.pixelInfDer(x, y)),1);
				
			}
		}
	}

	/**
	 * Obtiene la matriz del raster del archivo
	 * @param file 
	 * @throws IOException
	 */
	public void loadRaster(String file, float factor) throws IOException {
			
		BufferedReader reader = new BufferedReader(new FileReader(file));

		header = new Header(reader);

		grid = new float[header.getWidth()][header.getHeight()];
		System.out.println("Start loading raster");
		String line;
		String[] data;
		int y = 0;

		while ((line = reader.readLine()) != null) {
			//System.out.println(line);
			try {
			data = line.split(" ");
			for (int x = 0; x < data.length; x++)
				setValue(x,y,Float.parseFloat(data[x]),factor);
			y++;
			//System.out.println(y);
			}catch (NumberFormatException e){
				e.printStackTrace();
				System.out.println("raster line number: "+y);
				System.out.println(line);
			}
		}
		System.out.println("Raster loaded");
	}

	/**
	 * Calcula el valor del pixel de la esquina superior izquierda y de la esquina inferior derecha del raster
	 * @param supIzq
	 * @param infDer
	 * @return promedio de pixel del raster
	 */
	public float calculaValorPixel(double[] supIzq, double[] infDer) {

		int xStart = header.lon2x(supIzq[0]);
		int yStart = header.lat2y(supIzq[1]);
		int xEnd = header.lon2x(infDer[0]);
		int yEnd = header.lat2y(infDer[1]);
		
		//System.out.println(xStart+" "+xEnd+" "+yStart+" "+yEnd);
		float promedioPixel = 0;
		float val;
		int countBueno = 0;
		int countMalo = 0;

		for (int x = xStart; x <= xEnd; x++) {
			for (int y = yStart; y <= yEnd; y++) {
				val = getValue(x, y);
				if (val == header.getNoData())
					countMalo++;
				else {
					promedioPixel += val;
					countBueno++;
				}
			}
		}

		float t = ((float) countBueno)
				/ ((float) countBueno + (float) countMalo);

		if (t < 0.4f)
			return header.getNoData();

		return promedioPixel / countBueno;
	}

	/**
	 * Permite obtener el objeto header
	 * @return objeto header
	 */
	public Header getHeader() {
		return header;
	}
	/**
	 * Permite obtener el valor minimo del raster
	 * @return El valor minimo del raster
	 */
	public float getMin() {
		return min;
	}
	/**
	 * Permite obtener el valor maximo del raster
	 * @return El valor maximo del raster
	 */
	public float getMax() {
		return max;
	}
	/**
	 * Permite saber si el raster esta vacio 
	 * @return true o false
	 */
	public boolean isEmpty() {
		return empty;
	}

}
