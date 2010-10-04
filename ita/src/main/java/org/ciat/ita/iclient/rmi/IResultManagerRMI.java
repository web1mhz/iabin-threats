package org.ciat.ita.iclient.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import org.ciat.ita.iclient.IResultManager;
import org.ciat.ita.model.Record;


/**
 * 
 * A communication interface for deliver data worked
 *
 */
public interface IResultManagerRMI extends IResultManager, Remote{
	
	
	/**
	 * 
	 * @param clientName
	 *            to identify from where the results came
	 * @param data
	 *            worked to insert
	 * @return false if there were problems during the insertion
	 * @throws RemoteException
	 */
	@Override
	public boolean insertResult(String clientName,List<Record> data) throws RemoteException;
	
}
