package proxy.object;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;
import java.util.Map;

import model.Record;
import iclientmanager.IWorkManager;

public class WorkManagerObject implements IWorkManager {

	@Override
	public Map<String, String> getCountriesISO(Set<String> countries)  throws RemoteException {
		// TCP obtener pais 
		return null;
	}

	@Override
	public List<Record> getWork(String clientName,int quantity) throws RemoteException {
		//  TCP obtener trabajo
		return null;
	}


}
