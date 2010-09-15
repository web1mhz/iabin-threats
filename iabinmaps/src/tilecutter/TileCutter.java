package tilecutter;

import java.io.IOException;

import tilecutter.raster.Raster;
import tilecutter.tile.ImageManager;
import tilecutter.tile.TileManager;

/**
 * Clase principal (Main)
 * @author supportadmin
 *
 */
public class TileCutter {

	public static void main(String arg[]) throws IOException {
		int zoom = Integer.parseInt(arg[0]);
		Raster raster = new Raster();
		raster.loadRaster(arg[1]);
		ImageManager iManager = new ImageManager(arg[2]);
		TileManager tManager = new TileManager(iManager);
		
		tManager.cutRaster(raster, zoom);
		
		
	}
}
