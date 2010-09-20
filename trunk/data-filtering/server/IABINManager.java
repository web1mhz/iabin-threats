package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import server.database.DataBaseManager;
import server.database.PortalManager;

import model.Record;
import model.ShapeFile;

import client.ClientConfig;
import client.correctormanager.EnvironmentMaskManager;
import client.correctormanager.ShapeFileManager;
import client.correctormanager.WorldMaskManager;

public class IABINManager {

	private List<Record> records;
	private ShapeFileManager shapeFileManager;
	private WorldMaskManager worldmask;
	private EnvironmentMaskManager environmentMask;
	private PortalManager portal;

	public static void main(String[] args) {
		IABINManager manager = new IABINManager();
		manager.init();
	}

	public void init() {

		ClientConfig.init();
		ServerConfig.init();
		DataBaseManager.registerDriver();
		portal = new PortalManager(DataBaseManager.openConnection(
				ServerConfig.database_user, ServerConfig.database_password));

		records = portal
				.getRecordsWithCountyLevelInfo(ClientConfig.quantityRecords);
		shapeFileManager = new ShapeFileManager(2);
		worldmask = new WorldMaskManager(ClientConfig.maskFile);
		try {
			environmentMask = new EnvironmentMaskManager(
					ClientConfig.dirBioclimaticFiles, worldmask.getWorldMask());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// until there is no more records
		while (records.size() > 0) {
			work();
			records = portal
					.getRecordsWithCountyLevelInfo(ClientConfig.quantityRecords);
		}
		System.out.println("Work finished");
	}

	private void work() {
		int rangeSize = records.get(records.size() - 1).getId()
				- records.get(0).getId();
		int first = records.get(0).getId();
		System.out.print("# Working " + records.size() + " [" + first + "-"
				+ records.get(records.size() - 1).getId() + "]");
		String shapeComparison = null;
		String maskComparison = null;
		long tiempoCountries = 0;
		long tiempoEMM = 0;
		long tiempoMascara = 0;
		long tiempoTotal = System.currentTimeMillis();
		long antes = System.currentTimeMillis();
		int n = 0;
		try {

			/*
			 * For all the records in round
			 */
			for (Record rec : records) {
				n = rec.getId() - first;
				if ((rec.getId() - first) % 1000 == 0) {
					System.out.print(" "
							+ (int) (((double) n / (double) rangeSize) * 100)
							+ "%");
				}
				antes = System.currentTimeMillis();

				/*
				 * Make the comparison with the shape file data
				 */
				shapeComparison = shapeFileManager.compareCountries(rec
						.getLatitude(), rec.getLongitude(), rec.getState());
				tiempoCountries += System.currentTimeMillis() - antes;
				if (shapeComparison == null
						|| shapeComparison
								.equals(ShapeFile.E_NOT_FOUND_COUNTRY)) {

					antes = System.currentTimeMillis();
					/*
					 * Make the comparison with the world mask data
					 */
					maskComparison = worldmask.isEarth(rec.getLatitude(), rec
							.getLongitude());
					tiempoMascara += System.currentTimeMillis() - antes;
					rec.setNote(maskComparison);
					/*
					 * if record is OK add the environmental variables values
					 */
					if (rec.getNote() == null) {
						antes = System.currentTimeMillis();
						rec.setVariables(environmentMask
								.readVariablesFromEmmAIOFile(
										rec.getLongitude(), rec.getLatitude()));
						tiempoEMM += System.currentTimeMillis() - antes;
					}

				} else {
					/*
					 * Add note to the record when is NULL_COUNTRY or
					 * WRONG_COUNTRY
					 */
					rec.setNote(shapeComparison);
				}
			}
			System.out.println(" 100%");
			System.out.println("Total time :"
					+ (System.currentTimeMillis() - tiempoTotal)
					+ "ms FreeMem:" + Runtime.getRuntime().freeMemory());

			insertResults();
		} catch (RemoteException e) {
			System.out
					.println("There was a problem trying to get the countries codes (ISOs)");
			System.out.println("ERROR: ");
			System.out.println(e.getMessage());
		}

	}

	private void insertResults() {
		for (Record r : records) {
			/*
			 * if record note is null then insert it as good otherwise insert it
			 * as unreliable
			 */
			if (r.getNote() == null) {
				portal.insertGoodRecordWithCanonical(r);
			} else {
				portal.insertUnreliableRecordWithCanonical(r);
			}
		}

	}

}
