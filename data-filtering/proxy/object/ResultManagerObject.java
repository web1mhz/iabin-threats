package proxy.object;

import java.util.List;

import server.object.ResultServerObject;

import model.Record;
import iclientmanager.IResultManager;

public class ResultManagerObject implements IResultManager {


	@Override
	public boolean insertResult(String clientName,List<Record> data) {
		
		return false;
	}

}
