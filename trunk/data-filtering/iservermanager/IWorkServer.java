package iservermanager;

import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Record;

public interface IWorkServer {
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
	
	/**
	 * 
	 * @param clientName
	 *            to identify from where the request came
	 * @param quantity
	 *            of requesting records
	 * @return Records of records to work
	 * @throws Exception
	 */
	public List<Record> getWork(String clientName, int quantity) throws Exception;

	/**
	 * 
	 * @param countries
	 *            names
	 * @return a dictionary of countries names as key an the respective ISO as
	 *         value
	 * @throws Exception
	 */
	public Map<String, String> getCountriesISO(Set<String> countries) throws Exception;
}
