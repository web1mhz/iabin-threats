package tilecutter.projection;

/**
 * Interfaz projection
 * @author supportadmin
 *
 */
public interface Projection {
	
	public int lon2x(double lon);
	public int lat2y(double lat);
	public double x2lon(int x);
	public double y2lat(int y);
	public double rasterSupIzqLon();
	public double rasterSupIzqLat();
	public double rasterInfDerLon();
	public double rasterInfDerLat();
	public double[] pixelSupIzq(int x, int y);
	public double[] pixelInfDer(int x, int y);
	
}
