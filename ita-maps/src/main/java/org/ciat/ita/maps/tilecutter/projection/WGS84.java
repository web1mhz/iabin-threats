package org.ciat.ita.maps.tilecutter.projection;

import org.ciat.ita.maps.tilecutter.raster.Header;

public class WGS84 implements Projection {
	
	
	/**
	 * @uml.property  name="xll"
	 */
	private double xll;
	/**
	 * @uml.property  name="yll"
	 */
	private double yll;
	/**
	 * @uml.property  name="cellSizeX"
	 */
	private double cellSizeX;
	/**
	 * @uml.property  name="cellSizeY"
	 */
	private double cellSizeY;
	/**
	 * @uml.property  name="header"
	 * @uml.associationEnd  
	 */
	private Header header;
	
	
	/**
	 * Constructor de la clase WGS84
	 * @param xll
	 * @param yll
	 * @param cellSizeX
	 * @param cellSizeY
	 */
	public WGS84(double xll, double yll, double cellSizeX, double cellSizeY) {
	
		this.xll = xll;
		this.yll = yll;
		this.cellSizeX = cellSizeX;
		this.cellSizeY = cellSizeY;
	}

	/**
	 * Recibe un header y sus valores se usan en la clase WGS84
	 * @param  header
	 * @uml.property  name="header"
	 */
	public void setHeader(Header header) {
		this.header = header;
	}
	
	/**
	 *  Convierte la longitud a valor de X			
	 * @param lon : valor de la longitud
	 * @return valor de X
	 */
	public int lon2x(double lon) {           
		return (int)(Math.round((lon-xll)/cellSizeX));
	}
	
	/**
	 * Convierte la latitud a valor de Y
	 * @param lat : valor de la latitud
	 * @return valor de Y.
	 */
	public int lat2y(double lat) {          
		return header.getHeight() - (int)(Math.round((lat-yll)/cellSizeY));
	}
	
	/**
	 * Convierte el valor de X a longitud
	 * @param x : Valor de X 
	 * @return Valor de la longitud
	 */
	public double x2lon(int x) {            
		return x*cellSizeX+xll;
	}
	
	/**
	 * Convierte el valor de Y a latitud
	 * @param y : Valor de Y 
	 * @return Valor de la latitud
	 */
	public double y2lat(int y) {           //convierte valor de Y a latitud 
		return (header.getHeight()-y)*cellSizeY+yll;
	}
	/**
	 * permite obtener la longitud de la esquina superior izquierda del raster
	 * @return longitud
	 * xll
	 */
	
	public double rasterSupIzqLon() {
		return xll;
	}
	
	/**
	 * permite obtener la latitud de la esquina superior izquierda del raster
	 * @return la latitud
	 */
	public double rasterSupIzqLat() {
		return yll+header.getHeight()*cellSizeY;
	}
	
	/**
	 * permite obtener la longitud de la esquina inferior derecha del raster
	 * @return longitud
	 */
	public double rasterInfDerLon() {
		return xll+header.getWidth()*cellSizeX;
	}
	/**
	 * permite obtener la latitud de la esquina inferior derecha del raster
	 * @return la latitud
	 */
	public double rasterInfDerLat() {
		return yll;
	}
	
	/**
	 * Devuelve la longitud y latitud de la esquina superior izquierda del pixel
	 * @param x
	 * @param y
	 * @return longitud y latitud de la esquina superior izquierda del pixel
	 */
	
	public double[] pixelSupIzq(int x, int y) {
				
		double[] a = {x2lon(x),y2lat(y)+cellSizeY};
		
		return a;
	}
	/**
	 * Devuelve la longitud y latitud de la esquina inferior derecha del pixel
	 * @param x
	 * @param y
	 * @return longitud y latitud de la esquina inferior derecha del pixel
	 */
	
	public double[] pixelInfDer(int x, int y) {
		
		double[] a = {x2lon(x)+cellSizeX,y2lat(y)};
		
		return a;
	}
	
}
