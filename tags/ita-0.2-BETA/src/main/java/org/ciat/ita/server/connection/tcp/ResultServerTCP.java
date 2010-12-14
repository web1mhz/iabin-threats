package org.ciat.ita.server.connection.tcp;

import java.util.List;

import org.ciat.ita.iserver.IResultServer;
import org.ciat.ita.iserver.zip.IZipResultServer;
import org.ciat.ita.model.Record;
import org.ciat.ita.server.database.PortalInterface;



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
