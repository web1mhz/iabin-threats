package proxy.rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import client.ClientConfig;

import model.CompressorManager;
import model.Record;
import iclientmanager.IResultManager;
import iservermanager.rmi.IResultServerRMI;

@SuppressWarnings("serial")
public class ResultManagerRMI extends UnicastRemoteObject implements
		IResultManager {
	/**
	 * @uml.property   name="rsrmi"
	 * @uml.associationEnd   multiplicity="(1 1)" ordering="true" inverse="base:iservermanager.rmi.IResultServerRMI"
	 */
	private IResultServerRMI rserver;
//
	public ResultManagerRMI() throws RemoteException {
		super();
		try {
			rserver = (IResultServerRMI) Naming.lookup("//"
					+ ClientConfig.server_ipaddr + "/ResultServerRMI");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean insertResult(String clientName, List<Record> data)
			throws RemoteException {
		
		try {
			/*
			 * compress the data and insert it by RMI communication object
			 */
			return rserver.insertZippedResult(clientName,CompressorManager
					.toZipArray(data) );
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

}
