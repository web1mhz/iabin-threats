package org.ciat.ita.iclientmanager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.List;
import java.util.Map;

import org.ciat.ita.model.Record;

/**
 * 
 * A communication interface for requesting work 
 *
 */
public interface IWorkManager extends Remote {

	/**
	 * @param clientName
	 *            to identify from where the request came
	 * @param quantity
	 *            of requesting records
	 * @return records to work
	 * @throws RemoteException
	 */
	public List<Record> getWork(String clientName, int quantity)
			throws RemoteException;

	/**
	 * 
	 * @param countries names
	 * @return a dictionary of countries names as key an the respective ISO as value
	 * @throws RemoteException
	 */
	public Map<String, String> getCountriesISO(Set<String> countries)
			throws RemoteException;
}
