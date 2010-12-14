package org.ciat.ita.iclient.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.List;
import java.util.Map;

import org.ciat.ita.iclient.IWorkManager;
import org.ciat.ita.model.Record;

/**
 * 
 * A communication interface for requesting work 
 *
 */
public interface IWorkManagerRMI extends IWorkManager, Remote {

	/**
	 * @param clientName
	 *            to identify from where the request came
	 * @param quantity
	 *            of requesting records
	 * @return records to work
	 * @throws RemoteException
	 */
	@Override
	public List<Record> getWork(String clientName, int quantity)
			throws RemoteException;

	/**
	 * 
	 * @param countries names
	 * @return a dictionary of countries names as key an the respective ISO as value
	 * @throws RemoteException
	 */
	@Override
	public Map<String, String> getCountriesISO(Set<String> countries)
			throws RemoteException;
}
