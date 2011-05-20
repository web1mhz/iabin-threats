package org.ciat.ita.maps.tilecutter.tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.ciat.ita.maps.tilecutter.raster.Raster;
import org.ciat.ita.maps.tilecutter.tile.colormanager.ColorManager;

public class ImageManager {

	/**
	 * @uml.property  name="rootPath"
	 */
	private String rootPath = "";
	/**
	 * @uml.property  name="colorManager"
	 * @uml.associationEnd  
	 */
	private ColorManager colorManager;

	public ImageManager(String rootPath) {
		this.rootPath = rootPath;
	}

	/**
	 * @param colorManager
	 * @uml.property  name="colorManager"
	 */
	public void setColorManager(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	/**
	 * Guarda la imagen en la ruta especificada
	 * 
	 * @param raster
	 * @param x
	 * @param y
	 * @param zoom : valor del zoom
	 */
	public void saveTile(Raster raster, int x, int y, int zoom) {

		String path = rootPath + File.separator + zoom + File.separator;
		String nombre = "x" + x + "_y" + y + ".png";

		try {
			File dir = new File(path);
			dir.mkdirs();

			ImageIO.write(raster2Image(raster), "png", new File(path + nombre));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Transforma un grid en un imagen png
	 */
	public BufferedImage raster2Image(Raster raster) {
		BufferedImage bufferedImage = new BufferedImage(raster.getHeader().getWidth(), raster.getHeader()
				.getHeight(), BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < raster.getHeader().getWidth(); x++)
			for (int y = 0; y < raster.getHeader().getHeight(); y++)
				bufferedImage.setRGB(x, y, getRGB(raster.getValue(x, y)));

		return bufferedImage;
	}

	/**
	 * Calcular el color del pixel
	 * 
	 * @param value : El valor del pixel
	 * @return el color codificado 0xAARRGGBB
	 */
	private int getRGB(float value) {
		return colorManager.getRGB(value);
	}
}
