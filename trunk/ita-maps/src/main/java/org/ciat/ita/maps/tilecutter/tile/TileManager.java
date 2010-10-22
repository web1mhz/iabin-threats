package org.ciat.ita.maps.tilecutter.tile;

import org.ciat.ita.maps.tilecutter.projection.Mercator;
import org.ciat.ita.maps.tilecutter.raster.Header;
import org.ciat.ita.maps.tilecutter.raster.Raster;

public class TileManager {
	
	public final static int TILE_SIZE = 256;
	public final static double TOTAL_DEGREE_X = 360;
	public final static double TOTAL_DEGREE_Y = 180;
	public final static float NO_DATA = -9999;
	
	private ImageManager imageManager;
	
	public TileManager(ImageManager imageManager) {
		this.imageManager = imageManager;
	}
	/**
	 * Devuelve el numero de tiles en X
	 * @param zoom : El numero de zoom
	 * @return Numero de tiles en X
	 */
	
	public int getNumTileX(int zoom) {
		return (int) Math.pow(2, zoom);
	}
	
	/**
	 * Devuelve el cellSize de los tiles  en X.
	 * @param zoom
	 * @return El tamaño del cellSize en X
	 */
	
	public double getCellSizeX(int zoom) {
		return TOTAL_DEGREE_X/(getNumTileX(zoom)*TILE_SIZE);
	}
	
	/**
	 * Devuelve el numero de tiles en Y
	 * @param zoom : El numero de zoom 
	 * @return Numero de tiles en Y
	 */
	
	public int getNumTileY(int zoom) {
		return (int) Math.pow(2, zoom);
	}
	
	/**
	 * Devuelve el cellSize de los tiles en Y
	 * @param zoom
	 * @return El tamaño del cellSize en Y.
	 */
	public double getCellSizeY(int zoom) {
		return TOTAL_DEGREE_Y/(getNumTileY(zoom)*TILE_SIZE);
	}
	
	/**
	 * Funcion principal que corta el raster, crea el tile, dibuja la imagen y la guarda
	 * 
	 * @param raster : El archivo Raster
	 * @param zoom : el numero del zoom
	 */
	
	public void cutRaster(Raster raster, int zoom) {
		Header h = new Header(getNumTileX(zoom),getNumTileY(zoom),NO_DATA,new Mercator(zoom,0,0,1));
		
		int xStart = h.lon2x(raster.getHeader().rasterSupIzqLon());
		int xEnd = h.lon2x(raster.getHeader().rasterInfDerLon());
		int yStart = h.lat2y(raster.getHeader().rasterSupIzqLat()-0.5);
		int yEnd = h.lat2y(raster.getHeader().rasterInfDerLat());
		
		//System.out.println("From tile ("+xStart+" "+yStart+") to tile ("+xEnd+" "+yEnd+")");
		Raster tile;
		
		for (int x = xStart; x <= xEnd; x++) {
			for (int y = yStart; y <= yEnd; y++) {
				//System.out.println("Start tile "+x+" "+y);
				tile = createRaster(x,y,zoom);
				tile.loadRaster(raster);
				if(!tile.isEmpty())
					imageManager.saveTile(tile, x, y, zoom);
				//System.out.println("Finished tile "+x+" "+y);
			}
		}
		
	}
	
	/**
	 * Crea el raster
	 * @param x
	 * @param y
	 * @param zoom
	 * @return el raster
	 */
	public Raster createRaster(int x, int y, int zoom) {
	/*double xll,yll;
	xll= x*(360/getNumTileX(zoom))-180;
	yll= 90-( (180/getNumTileY(zoom))*(y+1) );*/
				
		return new Raster(TILE_SIZE,TILE_SIZE,NO_DATA,new Mercator(zoom,x*TILE_SIZE,y*TILE_SIZE,TILE_SIZE));
		
	}
	
}
