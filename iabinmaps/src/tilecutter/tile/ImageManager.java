package tilecutter.tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import tilecutter.raster.Raster;

public class ImageManager {
	//private float[] rgbMin = new float[] {0,0,255};
	//private float[] rgbMax = new float[] {255,255,0}; 
	private float[] rgbMin = new float[] {255,255,0};
	private float[] rgbMax = new float[] {255,0,0}; 
	
	private String rootPath = "";
	private Raster colorReference;
	
	//rgbMin = new float[] {255,255,0}; amarillo
	//rgbMax = new float[] {255,0,0};   rojo
	public ImageManager(String rootPath) {
		this.rootPath = rootPath;
	}
	
	public void setColorReference(Raster colorReference) {
		this.colorReference = colorReference;
	}
	
	/**
	 * Guarda la imagen en la ruta especificada
	 * @param raster
	 * @param x
	 * @param y
	 * @param zoom : valor del zoom
	 */
	public void saveTile(Raster raster, int x, int y, int zoom) {
		
		String path = rootPath+File.separator+zoom+File.separator;
		String nombre = "x"+x+"_y"+y+".png";
		
		try
		{
			File dir = new File(path);
			dir.mkdirs();
			
			
			ImageIO.write(raster2Image(raster), "png", new File(path+nombre));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Transforma un grid en un imagen png
	 */
	public BufferedImage raster2Image(Raster raster) {
		BufferedImage bufferedImage = new BufferedImage(raster.getHeader().getWidth(),
				raster.getHeader().getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		//rgbMin = new float[] {255,255,0}; amarillo
		//rgbMax = new float[] {255,0,0};   rojo
		
		
		for(int x = 0; x<raster.getHeader().getWidth(); x++)
			for(int y = 0; y < raster.getHeader().getHeight(); y++)
				bufferedImage.setRGB(x,y,getRGB(raster,raster.getValue(x, y)));
		
		return bufferedImage;
	}
	
	/**
	 * Calcular el color del pixel 
	 * @param value : El valor del pixel
	 * @return el color codificado 0xAARRGGBB
	 */
	private int getRGB(Raster raster, float value) {
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
		
		if(value == raster.getHeader().getNoData())//se modifica esta linea, se cambia noData por cero 0
			return 0x00000000; //Los bit de la transparencia deben ser 0 (transparente)
		
		for(int i = 0; i<3; i++)
		{
			delta = ((value-colorReference.getMin())/(colorReference.getMax()-colorReference.getMin()))*(rgbMax[i]-rgbMin[i]);
			int val = (int)(rgbMin[i]+delta);
			
			rgb = rgb | (val << ((2-i)*8));
		}
		
		
		return rgb | 0xff000000; //Los bit de la transparencia deben ser 1 (opaco)
	}
}
