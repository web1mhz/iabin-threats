package tilecutter.tile;

import tilecutter.raster.Raster;
import utils.PropertiesManager;

public class ColorManagerFactory {
	private static final String CONTINUOUS = "continuous";
	private static final String FIXED = "fixedthreshold";
	private static final String QUANTILE = "quantiles";

	/**
	 * Funcion que toma los valores del archivo de configuracion para determinar
	 * el color que se va a aplicar y la escala de color.
	 * 
	 * @param rasterID
	 *            el nombre del archivo raster
	 * @param raster
	 *            valores del archivo raster
	 * @return null sino encuentra la propiedad
	 */
	public static ColorManager createColorManager(String rasterID, Raster raster) {
		String colorType = PropertiesManager.getInstance()
				.getPropertiesAsString(rasterID + ".color.type");
		float[] rgbMin = PropertiesManager.getInstance().getPropertiesAsRGB(colorType + ".color.min");
		float[] rgbMax = PropertiesManager.getInstance().getPropertiesAsRGB(colorType + ".color.max");
		float min, max;

		if (PropertiesManager.getInstance().existProperty(colorType + ".value.max"))
			max = PropertiesManager.getInstance().getPropertiesAsFloat(colorType + ".value.max");
		else
			max = raster.getMax();

		if (PropertiesManager.getInstance().existProperty(colorType + ".value.min"))
			min = PropertiesManager.getInstance().getPropertiesAsFloat(colorType + ".value.min");
		else
			min = raster.getMin();

		String scaleType = PropertiesManager.getInstance().getPropertiesAsString(colorType + ".scale");

		if (scaleType.equals(CONTINUOUS))
			return new ContinousColorManager(rgbMin, rgbMax, min, max, raster.getHeader().getNoData());

		if (scaleType.equals(FIXED)) {
			float umbral = PropertiesManager.getInstance().getPropertiesAsFloat(colorType + ".scale.threshold");
			FixedThresholdColorManager cm = new FixedThresholdColorManager(rgbMin, rgbMax, min, max, raster.getHeader().getNoData(),
					umbral);
			cm.setThresholds(cm.createThresholds());
			return cm;
		}

		if (scaleType.equals(QUANTILE)) {
			float quantiles = PropertiesManager.getInstance()
					.getPropertiesAsFloat(colorType + ".scale.quantiles");
			QuantilesColorManager cm = new QuantilesColorManager(raster,rgbMin, rgbMax, min, max, raster.getHeader().getNoData(),
					quantiles);
			cm.setThresholds(cm.createThresholds());
			return cm;
		}

		return null;
	}
}
