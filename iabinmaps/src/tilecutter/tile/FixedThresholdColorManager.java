package tilecutter.tile;

public class FixedThresholdColorManager extends DiscreteColorManager {

	private float umbral;
	
	public FixedThresholdColorManager(float[] rgbMin, float[] rgbMax, float min, float max,
			float NoData, float umbral) {
		super(rgbMin, rgbMax, min, max, NoData);
		
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
