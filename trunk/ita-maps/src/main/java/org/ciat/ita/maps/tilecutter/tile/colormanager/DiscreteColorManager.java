package org.ciat.ita.maps.tilecutter.tile.colormanager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.ciat.ita.maps.tilecutter.raster.Raster;

public abstract class DiscreteColorManager extends ColorManager {

	private float[] umbrales;

	public void setThresholds(float[] thresholds) {
		this.umbrales = thresholds;
	}

	/**
	 * Constructor de la clase DiscreteColorManager
	 * 
	 * @param rgbMin
	 *            color minimo
	 * @param rgbMax
	 *            color maximo
	 * @param min
	 *            valor minimo
	 * @param max
	 *            valor maximo
	 * @param NoData
	 *            valor de noData
	 */
	public DiscreteColorManager(float[] rgbMin, float[] rgbMax, float min,
			float max, float NoData, Raster raster) {
		super(rgbMin, rgbMax, min, max, NoData, raster);

	}

	@Override
	public int getRGB(float value) {

		if (value == getNoData())
			return 0x00000000; // Los bit de la transparencia deben ser 0
		// (transparente)

		float min = 0;
		float max = umbrales.length - 1;

		int classe = 0;

		int rgb = 0;
		float delta;

		for (classe = 0; classe < umbrales.length && value > umbrales[classe]; classe++) {
		}
		/*
		 * Los colores estan en la memoria en un int, cada byte del int
		 * corresponde a una capa A (transparencia) R (rojo) G (verde) B (azul)
		 * 0xAARRGGBB
		 * 
		 * Si quiero un rojo de 3, tengo que poner el byte 00000011 (o 0x03 en
		 * exadecimal) en el byte del rojo. El rojo es en el tercer byte, voy
		 * entonces a mover todos los bit de 2*8
		 * 
		 * 0x03 << 16 = 0x030000
		 */
		for (int i = 0; i < 3; i++) {
			delta = ((((float) classe) - min) / (max - min))
					* (getRgbMax()[i] - getRgbMin()[i]);
			int val = (int) (getRgbMin()[i] + delta);

			rgb = rgb | (val << ((2 - i) * 8));
		}

		return rgb | 0xff000000; // Los bit de la transparencia deben ser 1
		// (opaco)

	}

	@Override
	public BufferedImage getScaleImage(String descripcion) {
		int line = 30;

		int recWidth = 30;
		int recHeight = line - 10;

		int width = 150;
		int height = umbrales.length * line + line;

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics = (Graphics2D) image.getGraphics();

		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);

		int cont = line;

		//graphics.setColor(Color.BLACK);
		//graphics.drawString(descripcion, 5, (line - recHeight) / 2 + recHeight);

		float valorMinimo = this.getMin();
		for (int i = 0; i < umbrales.length; i++) {
			Color color = new Color(getRGB(umbrales[i]
					- (float) Math.pow(10, -8)));
			graphics.setColor(color);
			graphics.fillRect(5, (line - recHeight) / 2 + cont, recWidth,recHeight);
			graphics.setColor(Color.BLACK);
			graphics.drawRect(5, (line - recHeight) / 2 + cont, recWidth,recHeight);
			ColorManager.drawYCenteredString(5, (line -6)+ cont, graphics,"[ " + valorMinimo/10 + " " + Float.toString(umbrales[i]/10)+ " ]");
			cont += line;
			valorMinimo = (int) umbrales[i];

		}

		return image;

	}

	/**
	 * Funcion que crea la lista de umbrales
	 * 
	 * @return lista de umbrales
	 */
	public abstract float[] createThresholds();

}
