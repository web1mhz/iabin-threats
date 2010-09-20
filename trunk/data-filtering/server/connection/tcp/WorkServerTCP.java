package server.connection.tcp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import server.database.PortalInterface;

import model.Record;
import iservermanager.IWorkServer;

public class WorkServerTCP implements IWorkServer {

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
