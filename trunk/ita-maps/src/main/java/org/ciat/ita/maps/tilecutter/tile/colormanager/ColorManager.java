package org.ciat.ita.maps.tilecutter.tile.colormanager;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import org.ciat.ita.maps.tilecutter.raster.Raster;

public abstract class ColorManager {

	/**
	 * @uml.property  name="rgbMin"
	 */
	private float[] rgbMin = new float[] { 255, 255, 0 };
	/**
	 * @uml.property  name="rgbMax"
	 */
	private float[] rgbMax = new float[] { 255, 0, 0 };
	/**
	 * @uml.property  name="min"
	 */
	private float min;
	/**
	 * @uml.property  name="max"
	 */
	private float max;
	/**
	 * @uml.property  name="noData"
	 */
	private float noData;
	/**
	 * @uml.property  name="raster"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Raster raster;

	public ColorManager(float[] rgbMin, float[] rgbMax, float min, float max, float NoData, Raster raster) {
		this.rgbMax = rgbMax;
		this.rgbMin = rgbMin;
		this.max = max;
		this.min = min;
		this.noData = NoData;
		this.raster = raster;
	}

	/**
	 * @return
	 * @uml.property  name="raster"
	 */
	public Raster getRaster() {
		return raster;
	}

	/**
	 * Escribe un String centrado en Y
	 * 
	 * @param x
	 *            valor de la posicion en X
	 * @param y
	 *            valor de la posicion en Y
	 * @param g
	 *            grafico
	 * @param s
	 *            Texto que se va a escribir
	 */
	protected static void drawYCenteredString(int x, int y, Graphics2D g, String s) {
		Font font = g.getFont();
		GlyphVector gv = font.createGlyphVector(g.getFontRenderContext(), s);
		Rectangle bounds = gv.getPixelBounds(g.getFontRenderContext(), x, y);

		g.drawGlyphVector(gv, x + 35, y - bounds.height / 2);
	}

	/**
	 * Retorna el color minimo
	 * @return  rgbMin
	 * @uml.property  name="rgbMin"
	 */
	public float[] getRgbMin() {
		return rgbMin;
	}

	/**
	 * Retorna el color maximo
	 * @return  rgbMax
	 * @uml.property  name="rgbMax"
	 */
	public float[] getRgbMax() {
		return rgbMax;
	}

	/**
	 * Retorna el valor minimo
	 * @return  valor mínimo
	 * @uml.property  name="min"
	 */
	public float getMin() {
		return min;
	}

	/**
	 * Retorna el valor maximo
	 * @return  valor máximo
	 * @uml.property  name="max"
	 */
	public float getMax() {
		return max;
	}

	/**
	 * Retorna el valor de noData
	 * @return  NoData
	 * @uml.property  name="noData"
	 */
	public float getNoData() {
		return noData;
	}

	/**
	 * Calcular el color del pixel
	 * 
	 * @param value
	 *            : El valor del pixel
	 * @return el color codificado 0xAARRGGBB
	 */
	public abstract int getRGB(float value);

	/**
	 * Obtiene la imagen de escala del raster
	 * 
	 * @param descripcion
	 *            de cada raster
	 * @return imagen de escala con la descripcion y rangos discretos o continuos del raster
	 */
	public abstract BufferedImage getScaleImage(String descripcion);

}
