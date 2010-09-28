package org.ciat.ita.iservermanager.rmi;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import org.ciat.ita.iservermanager.IResultServer;
import org.ciat.ita.iservermanager.zip.IZipResultServer;
import org.ciat.ita.model.Record;


public interface IResultServerRMI extends Remote, IResultServer, IZipResultServer{

	/**
	 * 
	 * @param clientName
	 *            to identify from insert the request came
	 * @param List
	 *            of records to insert
	 * @return false if there were problems during the insertion, and true otherwise.
	 * @throws RemoteException
	 */
	public boolean insertResult(String clientName, List<Record> data)
			throws RemoteException;
	
	/**
	 * 
	 * @param clientName
	 *            to identify from insert the request came
	 * @param zippedData
	 *            of records to insert instance of List<Record>
	 * @return false if there were problems during the insertion, and true otherwise.
	 * @throws RemoteException
	 */
	public boolean insertZippedResult(String clientName, byte[] zippedData)
		throws RemoteException;

}
