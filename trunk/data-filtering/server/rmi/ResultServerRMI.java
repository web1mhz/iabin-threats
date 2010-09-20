package server.rmi;

import iservermanager.IResultServerRMI;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import server.DataBaseManager;
import server.PortalManager;
import server.ServerConfig;

import model.CompressorManager;
import model.Record;

public class ResultServerRMI extends UnicastRemoteObject implements
		IResultServerRMI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3293128733665173250L;
	/**
	 * @uml.property name="portal"
	 * @uml.associationEnd multiplicity="(1 1)" ordering="true"
	 *                     inverse="base:server.PortalManager"
	 */
	private PortalManager portal;

	public ResultServerRMI() throws RemoteException {
		super();
		portal = new PortalManager(DataBaseManager.openConnection(
				ServerConfig.database_user, ServerConfig.database_password));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean insertZippedResult(String clientName, byte[] zippedData)
			throws RemoteException {
		List<Record> data=new ArrayList<Record>();
		try {
			/*
			 * decompress the data
			 */
			data = (List<Record>) CompressorManager
					.toObject(zippedData);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.gc();
		return insertResult(clientName, data);
	}

	@Override
	public boolean insertResult(String clientName, List<Record> data)
			throws RemoteException{
		for (Record r : data) {
			/*
			 * if record note is null then insert it as good otherwise
			 * insert it as unreliable
			 */
			if (r.getNote() == null) {
				portal.insertGoodRecord(r);
			} else {
				portal.insertUnreliableRecord(r);
			}
		}
		System.out.println("< Inserted " + data.size() + " ["
				+ data.get(0).getId() + "-"
				+ data.get(data.size() - 1).getId() + "]from ["
				+ clientName + "] " + new Date());

		data.clear();
		data = null;
		return true;
	}
}
