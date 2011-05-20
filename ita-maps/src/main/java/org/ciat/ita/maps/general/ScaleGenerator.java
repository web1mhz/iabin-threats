package org.ciat.ita.maps.general;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.ciat.ita.maps.tilecutter.raster.Raster;
import org.ciat.ita.maps.tilecutter.tile.ImageManager;
import org.ciat.ita.maps.tilecutter.tile.colormanager.ColorManager;
import org.ciat.ita.maps.tilecutter.tile.colormanager.ColorManagerFactory;
import org.ciat.ita.maps.utils.PropertiesManager;

public class ScaleGenerator {

	public static void main(String arg[]) throws IOException {
		PropertiesManager.register(arg[0]);

		String[] rastersID = PropertiesManager.getInstance().getPropertiesAsStringArray("rasters");
		Raster raster;
		ImageManager iManager;

		String targetPath = PropertiesManager.getInstance().getPropertiesAsString("path.target");
		String sourcePath = PropertiesManager.getInstance().getPropertiesAsString("path.source");

		for (String rasterID : rastersID) {
			raster = new org.ciat.ita.maps.tilecutter.raster.Raster();

			String descripcion = PropertiesManager.getInstance().getPropertiesAsString(
					rasterID + ".descripcion");
			String group = PropertiesManager.getInstance().getPropertiesAsString(rasterID + ".group");
			String pathGroup = PropertiesManager.getInstance().getPropertiesAsString(group + ".path");
			String fileName = PropertiesManager.getInstance().getPropertiesAsString(rasterID + ".filename");

			iManager = new ImageManager(targetPath + pathGroup + rasterID);
			float factor = PropertiesManager.getInstance().getPropertiesAsFloat(group + ".factor");
			raster.loadRaster(sourcePath + pathGroup + fileName, factor);
			ColorManager cManager = ColorManagerFactory.createColorManager(rasterID, raster);
			iManager.setColorManager(cManager);

			BufferedImage scaleImage = cManager.getScaleImage(descripcion);

			File dir = new File(targetPath + pathGroup + rasterID + File.separator);
			dir.mkdirs();
			try {
				ImageIO.write(scaleImage, "png", new File(targetPath + pathGroup + rasterID + File.separator
						+ rasterID + "scaleImage.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}// fin for
	}
}
