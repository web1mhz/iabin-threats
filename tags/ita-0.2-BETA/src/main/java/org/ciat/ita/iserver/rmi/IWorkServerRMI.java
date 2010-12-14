package org.ciat.ita.iserver.rmi;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ciat.ita.iserver.IWorkServer;
import org.ciat.ita.iserver.zip.IZipWorkServer;
import org.ciat.ita.model.Record;


public interface IWorkServerRMI extends Remote, IWorkServer, IZipWorkServer {
	
	/**
	 * 
	 * @param clientName
	 *            to identify from where the request came
	 * @param quantity
	 *            of requesting records
	 * @return Records of records to work
	 * @throws RemoteException
	 */
	public List<Record> getWork(String clientName, int quantity)
			throws RemoteException;

	/**
	 * 
	 * @param countries
	 *            names
	 * @return a dictionary of countries names as key an the respective ISO as
	 *         value
	 * @throws RemoteException
	 */
	public Map<String, String> getCountriesISO(Set<String> countries)
			throws RemoteException;
	
	/**
	 * 
	 * @param clientName
	 *            to identify from where the request came
	 * @param quantity
	 *            of requesting records
	 * @return zippedData of records to work instance of List<Record>
	 * @throws Exception
	 */
	public byte[] getZippedWork(String clientName, int quantity)
			throws RemoteException;
}
