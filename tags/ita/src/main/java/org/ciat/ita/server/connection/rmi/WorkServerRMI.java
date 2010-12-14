package org.ciat.ita.server.connection.rmi;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ciat.ita.iserver.rmi.IWorkServerRMI;
import org.ciat.ita.model.Record;
import org.ciat.ita.server.database.PortalInterface;


public class WorkServerRMI extends UnicastRemoteObject implements
		IWorkServerRMI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7492065662565511196L;

	public WorkServerRMI() throws RemoteException {
		super();
	}

	@Override
	public byte[] getZippedWork(String clientName, int quantity) {
		return PortalInterface.getInstance().getZippedWork(clientName, quantity);
	}

	@Override
	public Map<String, String> getCountriesISO(Set<String> countries)
			throws RemoteException {
		return PortalInterface.getInstance().getCountriesISO(countries);
	}

	@Override
	public List<Record> getWork(String clientName, int quantity)
			throws RemoteException {
		return PortalInterface.getInstance().getWork(clientName, quantity);
	}

}
