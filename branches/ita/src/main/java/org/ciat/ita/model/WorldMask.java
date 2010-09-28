package org.ciat.ita.model;

public class WorldMask  {
	
	/*Atributos de la mascara mundial*/
	private int ncols;
	private int nrows;
	private double xllcorner;
	private double yllcorner;
	private double cellsize;
	private int NODATA_value;
	private byte[] infoMask;
	private byte[] ascMask;
	public WorldMask(int ncols, int nrows, double xllcorner, double yllcorner,
			double cellsize, int nodata_value, byte[] infoMask, byte[] ascMask) {		
		this.ncols = ncols;
		this.nrows = nrows;
		this.xllcorner = xllcorner;
		this.yllcorner = yllcorner;
		this.cellsize = cellsize;
		NODATA_value = nodata_value;
		this.infoMask = infoMask;
		this.ascMask = ascMask;
		
	}
	
	/**
	 * Do not use this method. Is only for intern use..
	 * @param bitPosition
	 * @return
	 */
	public int findBitASCMask(long bitPosition) {
		return (((ascMask[(int) (bitPosition / 8)] - Byte.MIN_VALUE) >> (bitPosition % 8)) & 1);
	}
	
	/**
	 * Do not use this method. Is only for intern use..
	 * @param bitPosition
	 * @return
	 */
	public int findBitInfoMask(long bitPosition) {
		return (((infoMask[(int) (bitPosition / 8)] - Byte.MIN_VALUE) >> (bitPosition % 8)) & 1);
	}
	
	@Deprecated
	public WorldMask(int ncols, int nrows, double xllcorner, double yllcorner,
			double cellsize, int nodata_value) {		
		this.ncols = ncols;
		this.nrows = nrows;
		this.xllcorner = xllcorner;
		this.yllcorner = yllcorner;
		this.cellsize = cellsize;
		NODATA_value = nodata_value;
	}
	
	/**
	 * Do not use this method. Is only for intern use..
	 * @param longitud
	 * @return
	 */
	public int num_column(double longitud) {
		double distanciax = (longitud - xllcorner);
		double num_columna = (distanciax / cellsize);

		return (int) num_columna;
	}

	/**
	 * Do not use this method. Is only for intern use..
	 * @param latitud
	 * @return
	 */
	public int num_raw(double latitud) {
		double distanciay = (latitud - yllcorner);
		double num_fila = (distanciay / cellsize);
		num_fila = nrows - num_fila - 1;

		return (int) num_fila;
	}
	
	public byte[] getAscMask() {
		return ascMask;
	}
	
	public byte[] getInfoMask() {
		return infoMask;
	}
	
	public double getCellsize() {
		return cellsize;
	}
	public int getNcols() {
		return ncols;
	}
	public int getNrows() {
		return nrows;
	}
	public double getXllcorner() {
		return xllcorner;
	}
	public double getYllcorner() {
		return yllcorner;
	}
	public int getNODATA_value() {
		return NODATA_value;
	}
	
	
}
