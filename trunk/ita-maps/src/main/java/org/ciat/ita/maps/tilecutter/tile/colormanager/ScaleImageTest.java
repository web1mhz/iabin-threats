package org.ciat.ita.maps.tilecutter.tile.colormanager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScaleImageTest {
	public static void main(String args[]) {
		
				
		float[] umbrales = new float[]{100,500,2000,9600,10500};
		
		int line = 35;
		
		int recWidth = 40;
		int recHeight = 30;
		
		int width = 200;
		int height = umbrales.length*line+line;
		
		
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);
		
				
		int cont= line;
		graphics.setColor(Color.BLACK);
		graphics.drawString("DKDKF",5,(line-recHeight)/2+recHeight/2);
		for (int i=0;i<umbrales.length;i++){
			Color color = new Color( aleatorio(255),aleatorio(255),

	                aleatorio(255) );
		graphics.setColor(color);
		graphics.fillRect(5,(line-recHeight)/2+cont, recWidth, recHeight);
		
		
		graphics.setColor(Color.BLACK);
		graphics.drawRect(5,(line-recHeight)/2+cont, recWidth, recHeight);
		graphics.drawString(Float.toString(umbrales[i]), recWidth+10,(line-recHeight)/2+recHeight/2+cont);
		cont+=line;
		
		}
		
		try {
			ImageIO.write(image, "png", new File("scaleTestImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	 private static int aleatorio( int rango ) {

         double retornoMath;

  

         retornoMath = Math.random();

         return( (int)( retornoMath * rango ) );

         }
	
	
}
