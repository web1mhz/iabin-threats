package server.rmi;

import iservermanager.IWorkServerRMI;
import server.DataBaseManager;
import server.PortalManager;
import server.ServerConfig;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.CompressorManager;
import model.Record;

public class WorkServerRMI extends UnicastRemoteObject implements
		IWorkServerRMI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7492065662565511196L;
	/**
	 * @uml.property name="portal"
	 * @uml.associationEnd multiplicity="(1 1)" ordering="true"
	 *                     inverse="base:server.PortalManager"
	 */
	private PortalManager portal;

	public WorkServerRMI() throws RemoteException {
		super();
		portal = new PortalManager(DataBaseManager.openConnection(
				ServerConfig.database_user, ServerConfig.database_password));
	}

	@Override
	public byte[] getZippedWork(String clientName, int quantity) {
		try {
			return CompressorManager.toZipArray(getWork(clientName, quantity));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO retorno
		return null;
	}

	@Override
	public Map<String, String> getCountriesISO(Set<String> countries)
			throws RemoteException {
		Map<String, String> isoMap = new TreeMap<String, String>();
		for (String countryName : countries) {
			isoMap.put(countryName, portal.getCountryISO(countryName));

		}
		return isoMap;
	}

	@Override
	public List<Record> getWork(String clientName, int quantity)
			throws RemoteException {
		List<Record> records = portal.getRecords(quantity);
		if (records.size() > 0) {
			System.out.println("> Sending " + records.size() + " ["
					+ records.get(0).getId() + "-"
					+ records.get(records.size() - 1).getId() + "] to ["
					+ clientName + "] " + new Date());
		} else {
			System.out.println("> No records to send to " + clientName
					+ ". To exit press 'q' and enter anytime");
		}
		/*
		 * return the list of records
		 */
		return records;

	}

}
