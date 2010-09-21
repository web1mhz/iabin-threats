package tilecutter.tile;

import java.awt.image.BufferedImage;

public abstract class ColorManager {
	
	private float[] rgbMin = new float[] {255,255,0};
	private float[] rgbMax = new float[] {255,0,0}; 
	private float min,max,noData;
	
	public ColorManager(float[] rgbMin, float[] rgbMax, float min, float max, float NoData) {
		this.rgbMax = rgbMax;
		this.rgbMin = rgbMin;
		this.max = max;
		this.min = min;
		this.noData = NoData;
	}
	
	/**
	 * Retorna el color minimo
	 * @return rgbMin
	 */
	public float[] getRgbMin() {
		return rgbMin;
	}
	
	/**
	 * Retorna el color maximo
	 * @return rgbMax
	 */
	public float[] getRgbMax() {
		return rgbMax;
	}
	/**
	 * Retorna el valor minimo
	 * @return
	 */
	public float getMin() {
		return min;
	}
	
	/**
	 * Retorna el valor maximo
	 * @return
	 */
	public float getMax() {
		return max;
	}
	/**
	 * Retorna el valor de noData
	 * @return NoData
	 */
	public float getNoData() {
		return noData;
	}
	
	/**
	 * Calcular el color del pixel 
	 * @param value : El valor del pixel
	 * @return el color codificado 0xAARRGGBB
	 */
	public abstract int getRGB(float value);
	
	public abstract BufferedImage getScaleImage(String descripcion);
}
