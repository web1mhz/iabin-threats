package tilecutter.projection;

import java.lang.Math;


public class Mercator implements Projection {

	private int zoom, worldX, worldY;
	private double tiles;
	private double circumference;
	private double radius;
	
	private double falseEasting;
	private double falseNorthing;
	
	private int TILE_SIZE;
	
	/**
	 * Constructor de la clase Mercator
	 * @param zoom
	 * @param worldX
	 * @param worldY
	 * @param tileSize
	 */
	
	public Mercator(int zoom, int worldX, int worldY, int tileSize) {
		
		this.zoom = zoom;
		this.worldX = worldX;
		this.worldY = worldY;
		this.TILE_SIZE = tileSize;
		
		tiles = Math.pow(2,this.zoom);
		circumference = TILE_SIZE * tiles;
		radius = circumference / (2 * Math.PI);
		
		falseEasting = -1.0 * circumference / 2.0;
		falseNorthing = circumference / 2.0;
	}
	
	/**
	 * Convierte latitud a valor de Y
	 */

	public int lat2y(double lat) {
		return (int) -((radius / 2.0 * Math.log((1.0 + Math.sin(Math.toRadians(lat))) / 
				(1.0 -Math.sin(Math.toRadians(lat))))) -falseNorthing) -worldY;
	}

	/**
	 * Convierte longitud a valor de X
	 */
	public int lon2x(double lon) {
		return (int)((radius * Math.toRadians(lon)) - falseEasting)-worldX;
	}

	/**
	 * Devuelve la longitud y latitud de la esquina inferior derecha del pixel
	 */
	public double[] pixelInfDer(int x, int y) {
		
		return new double[]{x2lon(x+1),y2lat(y)};
	}

	/**
	 * Devuelve la longitud y latitud de la esquina superior izquierda del pixel.
	 */
	public double[] pixelSupIzq(int x, int y) {
		
		return new double[]{x2lon(x),y2lat(y-1)};
	}

	/**
	 * retorna la latitud de la esquina inferior derecha del raster
	 */
	
	public double rasterInfDerLat() {
		
		return y2lat(TILE_SIZE);
	}

	/**
	 * retorna la longitud de la esquina inferior derecha del raster
	 */
	public double rasterInfDerLon() {
		
		return x2lon(TILE_SIZE);
	}

	/**
	 * retorna la latitud de la esquina superior izquierda del raster
	 */
	public double rasterSupIzqLat() {
		
		return y2lat(0);
	}

	/**
	 * retorna la longitud de la esquina superior izquierda del raster
	 */
	public double rasterSupIzqLon() {
		
		return x2lon(0);
	}

	/**
	 * Convierte valor de X a longitud
	 */
	public double x2lon(int x) {
		return Math.toDegrees(((x+worldX) + falseEasting)/radius);
	}

	/**
	 * Convierte valor de Y a latitud
	 */
	public double y2lat(int y) {
		double u = (2 * (falseNorthing - (y+worldY))) / radius;
		return Math.toDegrees(Math.asin((Math.pow(Math.E, u) - 1.0)
				/ (1 + Math.pow(Math.E, u))));

	}
	
}
