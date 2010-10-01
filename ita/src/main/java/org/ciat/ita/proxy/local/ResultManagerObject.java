package org.ciat.ita.proxy.local;

import java.util.List;

import org.ciat.ita.iclient.IResultManager;
import org.ciat.ita.model.Record;
import org.ciat.ita.server.connection.object.ResultServerObject;




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
