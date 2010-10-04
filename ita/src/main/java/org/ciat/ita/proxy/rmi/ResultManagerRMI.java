package org.ciat.ita.proxy.rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.ciat.ita.client.ClientConfig;
import org.ciat.ita.iclient.rmi.IResultManagerRMI;
import org.ciat.ita.iserver.rmi.IResultServerRMI;
import org.ciat.ita.model.CompressorManager;
import org.ciat.ita.model.Record;



@SuppressWarnings("serial")
public class ResultManagerRMI extends UnicastRemoteObject implements
		IResultManagerRMI {
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
					+ ClientConfig.getInstance().server_ipaddr + "/ResultServerRMI");
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
