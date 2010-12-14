package org.ciat.ita.server.connection.tcp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ciat.ita.iserver.IWorkServer;
import org.ciat.ita.iserver.zip.IZipWorkServer;
import org.ciat.ita.model.Record;
import org.ciat.ita.server.database.PortalInterface;



public class WorkServerTCP implements IWorkServer, IZipWorkServer {
	
	/*
	 * It must be a method to send the records to work through the TCP connection
	 */

	@Override
	public Map<String, String> getCountriesISO(Set<String> countries) {
		return PortalInterface.getInstance().getCountriesISO(countries);
	}

	@Override
	public List<Record> getWork(String clientName, int quantity) {
		return PortalInterface.getInstance().getWork(clientName, quantity);
	}

	@Override
	public byte[] getZippedWork(String clientName, int quantity) {
		return PortalInterface.getInstance()
				.getZippedWork(clientName, quantity);
	}

}
