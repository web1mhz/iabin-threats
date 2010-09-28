package org.ciat.ita.server.connection.object;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ciat.ita.iservermanager.IWorkServer;
import org.ciat.ita.model.Record;
import org.ciat.ita.server.database.PortalInterface;



public class WorkServerObject implements IWorkServer {

	@Override
	public Map<String, String> getCountriesISO(Set<String> countries) {
		return PortalInterface.getInstance().getCountriesISO(countries);
	}

	@Override
	public List<Record> getWork(String clientName, int quantity) {
		return PortalInterface.getInstance().getWork(clientName, quantity);
	}

}
