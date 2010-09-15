package tilecutter;

import java.io.IOException;

import tilecutter.raster.Raster;
import tilecutter.tile.ImageManager;
import tilecutter.tile.TileManager;
import utils.PropertiesManager;

/**
 * Clase principal (Main)
 * 
 * @author supportadmin
 * 
 */
public class TileCutter {

	public static void main(String arg[]) throws IOException {
		// int zoom = Integer.parseInt(arg[0]);
		PropertiesManager.register(arg[0]);
		
		String[] rastersID = PropertiesManager.getInstance().getPropertiesAsStringArray("rasters");
		Raster raster;
		ImageManager iManager;
		
		String targetPath = PropertiesManager.getInstance().getPropertiesAsString("path.target");
		String sourcePath = PropertiesManager.getInstance().getPropertiesAsString("path.source");
		
		for (String rasterID : rastersID) {
			// raster.loadRaster(arg[1]);
			// ImageManager iManager = new ImageManager(arg[2]);
			raster = new Raster();
			
			String group = PropertiesManager.getInstance().getPropertiesAsString(rasterID+".group");
			String pathGroup = PropertiesManager.getInstance().getPropertiesAsString(group+".path");
			String fileName = PropertiesManager.getInstance().getPropertiesAsString(rasterID+".filename");
			
			
			iManager = new ImageManager(targetPath+pathGroup+rasterID);
			
			TileManager tManager = new TileManager(iManager);

			raster.loadRaster(sourcePath+pathGroup+fileName);

			int zoomMin = PropertiesManager.getInstance().getPropertiesAsInt("zoom.min");
			int zoomMax = PropertiesManager.getInstance().getPropertiesAsInt("zoom.max");
			while (zoomMin <= zoomMax) {
				tManager.cutRaster(raster, zoomMin);
				zoomMin++;
			}
		}

	}
}
