package org.ciat.ita.maps.tilecutter.tile.colormanager;

import org.ciat.ita.maps.tilecutter.raster.Raster;

public class FixedThresholdColorManager extends DiscreteColorManager {

	private float umbral;
	/**
	 * Constructor de la clase FixedThresholdColorManager
	 * @param rgbMin valor de color minimo
	 * @param rgbMax valor de color maximo
	 * @param min valor minimo
	 * @param max valor maximo
	 * @param NoData valor de noData
	 * @param umbral para el color discreto fijo
	 */
	public FixedThresholdColorManager(float[] rgbMin, float[] rgbMax, float min, float max,
			float NoData, float umbral,Raster raster) {
		super(rgbMin, rgbMax, min, max, NoData, raster);
		
		this.umbral = umbral;
	}

	@Override
	public float[] createThresholds() {
		
		double fSize = (getMax()-getMin())/umbral;
		double rSize = Math.floor(fSize);
		
		int size = (int) rSize;
		
		if(fSize != rSize)
			size++;
		
		float[] umbrales = new float[size];
		
		for(int i = 0; i<umbrales.length;i++)
			umbrales[i] = getMin()+umbral*(i+1);
		
		return umbrales;
	}

}
