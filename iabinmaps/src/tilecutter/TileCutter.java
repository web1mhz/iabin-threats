package tilecutter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import tilecutter.raster.Raster;
import tilecutter.tile.ImageManager;
import tilecutter.tile.TileManager;
import tilecutter.tile.colormanager.ColorManager;
import tilecutter.tile.colormanager.ColorManagerFactory;
import utils.PropertiesManager;

/**
 * Clase principal (Main)
 * 
 * @author supportadmin
 * 
 */
public class TileCutter {
	static boolean control=false;

	public static void main(String arg[]) {
		
		control=true;
		try {
			execute(arg[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void execute(String arg) throws IOException{
		

		if(control==true) PropertiesManager.register(arg);

		String[] rastersID = PropertiesManager.getInstance()
				.getPropertiesAsStringArray("rasters");
		Raster raster;
		ImageManager iManager;

		String targetPath = PropertiesManager.getInstance()
				.getPropertiesAsString("path.target");
		String sourcePath = PropertiesManager.getInstance()
				.getPropertiesAsString("path.source");

		for (String rasterID : rastersID) {

			 raster = new tilecutter.raster.Raster();
			String descripcion = PropertiesManager.getInstance()
					.getPropertiesAsString(rasterID + ".descripcion");
			String group = PropertiesManager.getInstance()
					.getPropertiesAsString(rasterID + ".group");
			String pathGroup = PropertiesManager.getInstance()
					.getPropertiesAsString(group + ".path");
			String fileName = PropertiesManager.getInstance()
					.getPropertiesAsString(rasterID + ".filename");

			iManager = new ImageManager(targetPath + pathGroup + rasterID);

			TileManager tManager = new TileManager(iManager);

			raster.loadRaster(sourcePath + pathGroup + fileName);

			ColorManager cManager = ColorManagerFactory.createColorManager(
					rasterID, raster);

			iManager.setColorManager(cManager);

			BufferedImage scaleImage = cManager.getScaleImage(descripcion);

			File dir = new File(targetPath+ pathGroup + rasterID + File.separator);
			dir.mkdirs();
			try {
				ImageIO.write(scaleImage, "png", new File(targetPath
						+ pathGroup + rasterID + File.separator + rasterID
						+ "scaleTestImage.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			int zoomMin = PropertiesManager.getInstance().getPropertiesAsInt(
					"zoom.min");
			int zoomMax = PropertiesManager.getInstance().getPropertiesAsInt(
					"zoom.max");
			while (zoomMin <= zoomMax) {
				tManager.cutRaster(raster, zoomMin);
				zoomMin++;
			}
		}

	
	}
	
}
