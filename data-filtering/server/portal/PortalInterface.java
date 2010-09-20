package server.portal;

import iservermanager.IResultServer;
import iservermanager.IWorkServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import server.ServerConfig;

import model.CompressorManager;
import model.Record;

public class PortalInterface implements IWorkServer, IResultServer {

	private static PortalInterface instance;

	/**
	 * @uml.property   name="portal"
	 * @uml.associationEnd   multiplicity="(1 1)" ordering="true" inverse="base:server.portal.PortalManager"
	 */
	private PortalManager portal;

	private PortalInterface() {
		portal = new PortalManager(DataBaseManager.openConnection(
				ServerConfig.database_user, ServerConfig.database_password));
	}

	public static PortalInterface getInstance() {
		if (instance == null) {
			instance = new PortalInterface();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean insertZippedResult(String clientName, byte[] zippedData) {
		List<Record> data = new ArrayList<Record>();
		try {
			/*
			 * decompress the data
			 */
			data = (List<Record>) CompressorManager.toObject(zippedData);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.gc();
		return insertResult(clientName, data);
	}

	@Override
	public boolean insertResult(String clientName, List<Record> data) {
		for (Record r : data) {
			/*
			 * if record note is null then insert it as good otherwise insert it
			 * as unreliable
			 */
			if (r.getNote() == null) {
				portal.insertGoodRecord(r);
			} else {
				portal.insertUnreliableRecord(r);
			}
		}
		System.out.println("< Inserted " + data.size() + " ["
				+ data.get(0).getId() + "-" + data.get(data.size() - 1).getId()
				+ "]from [" + clientName + "] " + new Date());

		data.clear();
		data = null;
		return true;
	}

	@Override
	public Map<String, String> getCountriesISO(Set<String> countries) {
		Map<String, String> isoMap = new TreeMap<String, String>();
		for (String countryName : countries) {
			isoMap.put(countryName, portal.getCountryISO(countryName));

		}
		return isoMap;
	}

	@Override
	public List<Record> getWork(String clientName, int quantity) {
		List<Record> records = portal.getRecords(quantity);
		if (records.size() > 0) {
			System.out.println("> Sending " + records.size() + " ["
					+ records.get(0).getId() + "-"
					+ records.get(records.size() - 1).getId() + "] to ["
					+ clientName + "] " + new Date());
		} else {
			System.out.println("> No records to send to " + clientName
					+ ". To exit press 'q' and enter anytime");
		}
		/*
		 * return the list of records
		 */
		return records;
	}

	@Override
	public byte[] getZippedWork(String clientName, int quantity) {
		try {
			return CompressorManager.toZipArray(getWork(clientName, quantity));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO retorno
		return null;
	}
}
