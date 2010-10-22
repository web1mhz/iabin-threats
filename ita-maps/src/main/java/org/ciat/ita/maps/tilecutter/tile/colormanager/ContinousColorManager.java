package org.ciat.ita.maps.tilecutter.tile.colormanager;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.ciat.ita.maps.tilecutter.raster.Raster;
public class ContinousColorManager extends ColorManager{
	/**
	 * Constructor de la clase ContinousColorManager
	 * @param rgbMin valor de color minimo
	 * @param rgbMax valor de color maximo
	 * @param min valor minimo
	 * @param max valor maximo
	 * @param NoData valor de noData
	 */
	public ContinousColorManager(float[] rgbMin, float[] rgbMax, float min,
			float max, float NoData, Raster raster) {
		super(rgbMin, rgbMax, min, max, NoData, raster);
					
	}

	@Override
	   public int getRGB(float value) {
		int rgb = 0;
		float delta;
		/*
		 * Los colores estan en la memoria en un int, cada byte del int corresponde a una capa
		 * 	 A (transparencia) R (rojo) G (verde) B (azul)
		 *   0xAARRGGBB
		 *   
		 * Si quiero un rojo de 3, tengo que poner el byte 00000011 (o 0x03 en exadecimal) en el byte del rojo.
		 * El rojo es en el tercer byte, voy entonces a mover todos los bit de 2*8 
		 * 
		 * 0x03 << 16 = 0x030000
		 * 
		 */
		
		if(value == getNoData())//se modifica esta linea, se cambia noData por cero 0
			return 0x00000000; //Los bit de la transparencia deben ser 0 (transparente)
		
		for(int i = 0; i<3; i++)
		{
			delta = ((value-getMin())/(getMax()-getMin()))*(getRgbMax()[i]-getRgbMin()[i]);
			int val = (int)(getRgbMin()[i]+delta);
			
			rgb = rgb | (val << ((2-i)*8));
		}
		
		
		return rgb | 0xff000000; //Los bit de la transparencia deben ser 1 (opaco)
	}

	@Override
	public BufferedImage getScaleImage(String descripcion) {
	  
		int line = 35;
		
		int recWidth = 40;
		int recHeight = line*4-recWidth;
		
		int width = recWidth*5;
		int height = line*4+10;
		
		
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
						
		graphics.setColor(Color.BLACK);
		graphics.drawString(descripcion,5,(line-recHeight)/2+recHeight/2);
		
		Color colorMin = new Color(getRGB(getMin()));
		Color colorMax = new Color(getRGB(getMax()));
		graphics.setPaint(new GradientPaint(5, (line-recHeight)/2+line*2, colorMax,0, recHeight+line, colorMin ));
		graphics.fillRect(5,(line-recHeight)/2+line*2, recWidth, recHeight);
		
		
		graphics.setColor(Color.BLACK);
		graphics.drawRect(5,(line-recHeight)/2+line*2, recWidth, recHeight);
		graphics.drawString("High: "+Float.toString(this.getMax()), recWidth+10,(line-recHeight)/2+recHeight/2+30);
		graphics.drawString("Low: "+Float.toString(this.getMin()), recWidth+10,recHeight+40);
		
		
		return image;
	}

}
