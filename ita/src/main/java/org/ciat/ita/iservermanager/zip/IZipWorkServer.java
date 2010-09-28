package org.ciat.ita.iservermanager.zip;


public interface IZipWorkServer {
	/**
	 * 
	 * @param clientName
	 *            to identify from where the request came
	 * @param quantity
	 *            of requesting records
	 * @return zippedData of records to work instance of List<Record>
	 * @throws Exception
	 */
	public byte[] getZippedWork(String clientName, int quantity) throws Exception;
	
}
