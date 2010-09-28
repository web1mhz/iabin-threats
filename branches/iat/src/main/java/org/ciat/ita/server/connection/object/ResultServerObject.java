package org.ciat.ita.server.connection.object;

import java.util.List;

import org.ciat.ita.iservermanager.IResultServer;
import org.ciat.ita.model.Record;
import org.ciat.ita.server.database.PortalInterface;



public class ResultServerObject implements IResultServer {

	@Override
	public boolean insertResult(String clientName, List<Record> data) {
		return PortalInterface.getInstance().insertResult(clientName, data);
	}


}
