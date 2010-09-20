package iservermanager;

import java.util.List;

import model.Record;

public interface IResultServer {

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
	
	/**
	 * 
	 * @param clientName
	 *            to identify from insert the request came
	 * @param List
	 *            of records to insert
	 * @return false if there were problems during the insertion, and true otherwise.
	 * @throws Exception
	 */
	public boolean insertResult(String clientName, List<Record> data) throws Exception;

}
