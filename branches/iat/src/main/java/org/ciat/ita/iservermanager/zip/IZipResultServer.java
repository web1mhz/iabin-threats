package org.ciat.ita.iservermanager.zip;

public interface IZipResultServer {

	/**
	 * 
	 * @param clientName
	 *            to identify from insert the request came
	 * @param zippedData
	 *            of records to insert instance of List<Record>
	 * @return false if there were problems during the insertion, and true otherwise.
	 * @throws Exception
	 */
	public boolean insertZippedResult(String clientName, byte[] zippedData) throws Exception;


}
