package org.ciat.ita.proxy.rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ciat.ita.client.ClientConfig;
import org.ciat.ita.iclientmanager.IWorkManager;
import org.ciat.ita.iservermanager.rmi.IWorkServerRMI;
import org.ciat.ita.model.CompressorManager;
import org.ciat.ita.model.Record;



@SuppressWarnings("serial")
public class WorkManagerRMI extends UnicastRemoteObject implements IWorkManager {
	/**
	 * @uml.property   name="wsrmi"
	 * @uml.associationEnd   multiplicity="(1 1)" ordering="true" inverse="base:iservermanager.rmi.IWorkServerRMI"
	 */
	private IWorkServerRMI wserver;
//
	public WorkManagerRMI() throws RemoteException {
		super();
		try {
			wserver = (IWorkServerRMI) Naming.lookup("//"
					+ ClientConfig.getInstance().server_ipaddr + "/WorkServerRMI");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, String> getCountriesISO(Set<String> countries)
			throws RemoteException {
		return wserver.getCountriesISO(countries);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Record> getWork(String clientName, int quantity)
			throws RemoteException {
		try {
			/*
			 * decompress the data got by RMI communication object
			 */
			return (List<Record>) CompressorManager.toObject(wserver.getZippedWork(clientName, quantity));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
