package server.object;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Record;
import iservermanager.IWorkServer;

public class ResultServerObject implements IWorkServer{

	@Override
	public Map<String, String> getCountriesISO(Set<String> countries)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Record> getWork(String clientName, int quantity)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getZippedWork(String clientName, int quantity)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
