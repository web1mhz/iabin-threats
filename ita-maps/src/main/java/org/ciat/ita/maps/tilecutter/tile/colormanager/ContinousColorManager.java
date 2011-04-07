package org.ciat.ita.maps.tilecutter.tile.colormanager;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

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
		int nbStep = 5;
		int line = 15;
		int margin = 30;
		
		int recWidth = 140;
		int recHeight = line;
		
		int width = recWidth+margin;
		int height = recHeight*2;
		
		
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		
		graphics.setColor(new Color(255,255,255,0));
		graphics.fillRect(0, 0, width, height);
		
		// int fontSize = 12;
		//Font font = new Font("Arial", Font.PLAIN, fontSize);
		   
		//graphics.setFont(font);
		//graphics.setColor(Color.BLACK);
		//graphics.drawString(descripcion,5,(line-recHeight)/2+recHeight/2);
		Font font = new Font("serif", Font.BOLD, 10);
	    
		graphics.setFont(font);
		
		Color colorMin = new Color(getRGB(getMin()));
		Color colorMax = new Color(getRGB(getMax()));
		graphics.setPaint(new GradientPaint(margin/2, margin/4, colorMin,recWidth, 0, colorMax ));
		graphics.fillRect(margin/2, 0, recWidth, recHeight);
		
		FontMetrics fm = graphics.getFontMetrics(font);
		
		
		graphics.setColor(Color.BLACK);
		graphics.drawRect(margin/2, 0, recWidth, recHeight);
		int fWidth;
		float diffMinMax = (this.getMax()-this.getMin())/nbStep;
		float min = this.getMin();
		float toWrite;
		
		
		String format = "#";
		int fact = 1;
		if(diffMinMax < 1.5){
			format = format + ".";
			for(int i=1; diffMinMax*i<1.5; i *= 10){
				format = format + "#";
				fact *= 10;
			}
		}
		
		DecimalFormat formater = new DecimalFormat(format);
		for(int i =0; i < nbStep; i++){
			toWrite =  (float)Math.floor((min+i*diffMinMax)*fact)/fact;
			fWidth= fm.stringWidth(formater.format(toWrite));
			graphics.drawString(formater.format(toWrite), margin/2-fWidth/2 + 
					(recWidth/nbStep)*i,recHeight+line-2);
			graphics.drawLine(margin/2+(recWidth/nbStep)*i,recHeight, 
					margin/2+(recWidth/nbStep)*i, recHeight+line/4);
		}
		
		fWidth  = fm.stringWidth(formater.format(Math.floor(this.getMax()*fact)/fact));
		graphics.drawString(formater.format(this.getMax()), margin/2+recWidth-fWidth/2,recHeight+line-2);
		graphics.drawLine(margin/2+recWidth,recHeight, 
				margin/2+recWidth, recHeight+line/4);
		return image;
	}

}
