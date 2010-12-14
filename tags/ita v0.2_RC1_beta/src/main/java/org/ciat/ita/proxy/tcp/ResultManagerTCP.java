package org.ciat.ita.proxy.tcp;

import java.util.List;

import org.ciat.ita.iclient.IResultManager;
import org.ciat.ita.model.Record;


public class ResultManagerTCP implements IResultManager {

	@Override
	public boolean insertResult(String clientName,List<Record> data) {
		
		return false;
	}

}
