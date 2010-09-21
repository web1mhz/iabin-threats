package proxy.object;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import java.util.Map;

import server.connection.object.WorkServerObject;

import model.Record;
import iclientmanager.IWorkManager;

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
