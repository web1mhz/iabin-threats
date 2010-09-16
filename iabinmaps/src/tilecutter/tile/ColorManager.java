package tilecutter.tile;

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
	
	public float[] getRgbMin() {
		return rgbMin;
	}

	public float[] getRgbMax() {
		return rgbMax;
	}

	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}

	public float getNoData() {
		return noData;
	}
	
	public abstract int getRGB(float value);
}
