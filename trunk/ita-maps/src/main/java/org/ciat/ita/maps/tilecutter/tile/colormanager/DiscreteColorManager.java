package org.ciat.ita.maps.tilecutter.tile.colormanager;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import org.ciat.ita.maps.tilecutter.raster.Raster;

public abstract class DiscreteColorManager extends ColorManager {

	/**
	 * @uml.property  name="umbrales" multiplicity="(0 -1)" dimension="1"
	 */
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
			return 0x00000000; // Los bit de la transparencia deben ser 0 (transparente)

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

		return rgb | 0xff000000; // Los bit de la transparencia deben ser 1 (opaco)

	}

	@Override
	public BufferedImage getScaleImage(String descripcion) {
		Font font = new Font("serif", Font.BOLD, 10);
		
		
		float diffMinMax = (this.getMax()-this.getMin())/umbrales.length;
		String format = "#";
		int fact = 1;
		if(diffMinMax < 1.5){
			format = format + ".";
			for(int i=1; diffMinMax*i<1.5; i *= 10){
				format = format + "#";
				fact *= 10;
			}
		}
		final DecimalFormat formater = new DecimalFormat(format);
		
		int margin = 30;
		int boxMargin = 0;
		int colorRecWidth = (170-margin)/umbrales.length;
		int line = 15;
		
		
		int recWidth = umbrales.length*(colorRecWidth+boxMargin);
		int recHeight = line;
		
		int width = recWidth+margin;
		int height = recHeight*2;
		

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setFont(font);
		final FontMetrics fm = graphics.getFontMetrics(font);
		
		graphics.setColor(new Color(0,0,0,0));
		graphics.fillRect(0, 0, width, height);


		String toWrite;
		int tWidth;
		float valorMinimo = this.getMin();
		
		for (int i = 0; i < umbrales.length; i++) {
			Color color = new Color(getRGB(umbrales[i]
					- (float) Math.pow(10, -8)));
			graphics.setColor(color);
			toWrite = formater.format(Math.floor(valorMinimo*fact)/fact);
			tWidth= fm.stringWidth(toWrite);
			graphics.fillRect(margin/2+i*colorRecWidth+i*boxMargin, 0, colorRecWidth,recHeight);
			graphics.setColor(Color.BLACK);
			graphics.drawRect(margin/2+i*colorRecWidth+i*boxMargin, 0, colorRecWidth,recHeight);
			graphics.drawString(toWrite,
					margin/2+(i)*colorRecWidth+i*boxMargin-tWidth/2, 
					recHeight*2-2);			
			valorMinimo =  umbrales[i];
		}
		
		toWrite = formater.format(Math.floor(getMax()*fact)/fact);
		tWidth= fm.stringWidth(toWrite);
		graphics.drawString(toWrite,
				margin/2+(umbrales.length)*colorRecWidth+umbrales.length*boxMargin-tWidth/2, 
				recHeight*2-2);

		return image;

	}

	/**
	 * Funcion que crea la lista de umbrales
	 * 
	 * @return lista de umbrales
	 */
	public abstract float[] createThresholds();

}
