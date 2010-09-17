package tilecutter.tile;

import java.util.ArrayList;
import java.util.Collections;

import tilecutter.raster.Raster;

public class QuantilesColorManager extends DiscreteColorManager {

	private float quantil;
	Raster raster;
/**
 * Constructor de la clase QuantilesColorManager
 * @param raster los valores del archivo raster
 * @param rgbMin valor de color minimo
 * @param rgbMax valor de color maximo
 * @param min  valor minimo
 * @param max valor maximo
 * @param NoData valor de noData
 * @param quantil valor del quantil
 */
	public QuantilesColorManager(Raster raster, float[] rgbMin, float[] rgbMax,
			float min, float max, float NoData, float quantil) {
		super(rgbMin, rgbMax, min, max, NoData);
		this.quantil = quantil;
		this.raster = raster;

	}

	@Override
	public float[] createThresholds() {
		ArrayList<Float> values = new ArrayList<Float>();
		for (int x = 0; x < raster.getHeader().getWidth(); x++) {
			for (int y = 0; y < raster.getHeader().getHeight(); y++) {
				if(raster.getValue(x, y) != raster.getHeader().getNoData())
					values.add(raster.getValue(x, y));

			}
		}

		Collections.sort(values);

		float[] umbrales = new float[(int) quantil];
		int index;
		System.out.println("umbrales size : "+umbrales.length);
		for (int i = 0; i < umbrales.length; i++) {
			double fIndex = (values.size() * ((i + 1) / quantil));
			double rIndex = Math.floor(fIndex);

			index = (int) rIndex;

			if (fIndex != rIndex)
				index++;
			umbrales[i] = values.get(index-1);
			System.out.println("Quantil "+i+" "+umbrales[i]);
		}
		
		
		
		return umbrales;

	}

}
