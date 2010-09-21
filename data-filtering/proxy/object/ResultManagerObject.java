package proxy.object;

import java.util.List;

import server.connection.object.ResultServerObject;


import model.Record;
import iclientmanager.IResultManager;
import iservermanager.IResultServer;

public class ResultManagerObject implements IResultManager {

	private ResultServerObject rserver;
	
	public ResultManagerObject(){
		rserver=new ResultServerObject();
	}
	
	@Override
	public boolean insertResult(String clientName,List<Record> data) {
		return rserver.insertResult(clientName, data);
	}

}
