package server.connection.tcp;

import java.util.List;

import server.portal.PortalInterface;

import model.Record;
import iservermanager.IResultServer;

public class ResultServerTCP implements IResultServer {

	@Override
	public boolean insertResult(String clientName, List<Record> data) {
		return PortalInterface.getInstance().insertResult(clientName, data);
	}

	@Override
	public boolean insertZippedResult(String clientName, byte[] zippedData) {
		return PortalInterface.getInstance().insertZippedResult(clientName,
				zippedData);
	}

}
