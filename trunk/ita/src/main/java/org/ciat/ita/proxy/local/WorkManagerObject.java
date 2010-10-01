package org.ciat.ita.proxy.local;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import java.util.Map;

import org.ciat.ita.iclient.IWorkManager;
import org.ciat.ita.model.Record;
import org.ciat.ita.server.connection.object.WorkServerObject;



public class WorkManagerObject implements IWorkManager {
	
	private WorkServerObject wserver;
	
	public WorkManagerObject(){
		wserver=new WorkServerObject();
	}

	@Override
	public Map<String, String> getCountriesISO(Set<String> countries)  throws RemoteException {
		return wserver.getCountriesISO(countries);
	}

	@Override
	public List<Record> getWork(String clientName,int quantity) throws RemoteException {
		return wserver.getWork(clientName, quantity);
	}


}
