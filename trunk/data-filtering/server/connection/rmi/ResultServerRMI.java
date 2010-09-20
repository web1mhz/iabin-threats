package server.connection.rmi;

import iservermanager.rmi.IResultServerRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;


import server.database.PortalInterface;

import model.Record;

public class ResultServerRMI extends UnicastRemoteObject implements
		IResultServerRMI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3293128733665173250L;


	public ResultServerRMI() throws RemoteException {
		super();
	}

	@Override
	public boolean insertZippedResult(String clientName, byte[] zippedData)
			throws RemoteException {
		return PortalInterface.getInstance().insertZippedResult(clientName, zippedData);
	}

	@Override
	public boolean insertResult(String clientName, List<Record> data)
			throws RemoteException{
		return PortalInterface.getInstance().insertResult(clientName, data);
	}
}
