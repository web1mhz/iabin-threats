package server.connection.tcp;

import java.util.List;

import server.database.PortalInterface;

import model.Record;
import iservermanager.IResultServer;
import iservermanager.zip.IZipResultServer;

public class ResultServerTCP implements IResultServer, IZipResultServer {
	
	/*
	 * It must be a method to gather the data from the TCP connection
	 * an then use the inserts methods to insert the data. 
	 */

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
