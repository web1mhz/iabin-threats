package server.tcp;

import java.rmi.RemoteException;
import java.util.List;

import model.Record;
import iservermanager.IResultServer;

public class ResultServerTCP implements IResultServer{

	@Override
	public boolean insertResult(String clientName, List<Record> data)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertZippedResult(String clientName, byte[] zippedData)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

}
