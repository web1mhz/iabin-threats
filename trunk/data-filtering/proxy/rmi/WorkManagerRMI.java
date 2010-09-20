package proxy.rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

import client.ClientConfig;

import model.CompressorManager;
import model.Record;
import iclientmanager.IWorkManager;
import iservermanager.rmi.IWorkServerRMI;

@SuppressWarnings("serial")
public class WorkManagerRMI extends UnicastRemoteObject implements IWorkManager {
	/**
	 * @uml.property   name="wsrmi"
	 * @uml.associationEnd   multiplicity="(1 1)" ordering="true" inverse="base:iservermanager.rmi.IWorkServerRMI"
	 */
	IWorkServerRMI wsrmi;
//
	public WorkManagerRMI() throws RemoteException {
		super();
		try {
			wsrmi = (IWorkServerRMI) Naming.lookup("//"
					+ ClientConfig.server_ipaddr + "/WorkServerRMI");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, String> getCountriesISO(Set<String> countries)
			throws RemoteException {
		return wsrmi.getCountriesISO(countries);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Record> getWork(String clientName, int quantity)
			throws RemoteException {
		try {
			/**
			 * decompress the data got by RMI communication object
			 */
			return (List<Record>) CompressorManager.toObject(wsrmi.getZippedWork(clientName, quantity));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}
