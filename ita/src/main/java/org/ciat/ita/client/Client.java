package org.ciat.ita.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.ciat.ita.client.manage.EnvironmentMaskManager;
import org.ciat.ita.client.manage.ShapeFileManager;
import org.ciat.ita.client.manage.WorldMaskManager;
import org.ciat.ita.iclient.ClientFactory;
import org.ciat.ita.iclient.IResultManager;
import org.ciat.ita.iclient.IWorkManager;
import org.ciat.ita.model.Record;
import org.ciat.ita.model.ShapeFile;
import org.ciat.ita.proxy.local.ClientFactoryLocal;
import org.ciat.ita.proxy.rmi.ClientFactoryRMI;
import org.ciat.ita.proxy.tcp.ClientFactoryTCP;

public class Client {

	/**
	 * @uml.property name="worker"
	 * @uml.associationEnd multiplicity="(1 1)" ordering="true"
	 *                     inverse="base:iclientmanager.IWorkManager"
	 */
	private IWorkManager worker;

	/**
	 * @uml.property name="resulter"
	 * @uml.associationEnd multiplicity="(1 1)" ordering="true"
	 *                     inverse="base:iclientmanager.IResultManager"
	 */
	private IResultManager resulter;

	/**
	 * @uml.property name="factory"
	 * @uml.associationEnd multiplicity="(1 1)" ordering="true"
	 *                     inverse="base:iclientmanager.ClientFactory"
	 */
	private ClientFactory factory;
	private Long initTime;
	private boolean running;

	/**
	 * @uml.property name="records"
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="true"
	 *                     inverse="base:model.Record"
	 */
	private List<Record> records;

	/**
	 * @uml.property name="shapeFileManager"
	 * @uml.associationEnd multiplicity="(1 1)" ordering="true"
	 *                     inverse="base:client.correctormanager.ShapeFileManager"
	 */
	private ShapeFileManager shapeFileManager;

	/**
	 * @uml.property name="worldmask"
	 * @uml.associationEnd multiplicity="(1 1)" ordering="true"
	 *                     inverse="base:client.correctormanager.WorldMaskManager"
	 */
	private WorldMaskManager worldmask;
	private String clientName;
	/**
	 * @uml.property name="enviromentMask"
	 * @uml.associationEnd multiplicity="(1 1)" ordering="true"
	 *                     inverse="base:client.correctormanager.EnvironmentMaskManager"
	 */
	private EnvironmentMaskManager environmentMask;
	private int worked;

	public static void main(String[] args) {
		Client client = new Client();
		client.start();
	}

	/**
	 * Creates the client whit the properties set
	 */
	public Client() {
		try {
			worked = 0;
			running = false;

			shapeFileManager = new ShapeFileManager();
			// shapefile = new Shape(new File(ClientConfig.shapeFile));
			worldmask = new WorldMaskManager(
					ClientConfig.getInstance().maskFile);
			environmentMask = new EnvironmentMaskManager(ClientConfig
					.getInstance().dirBioclimaticFiles, worldmask
					.getWorldMask());
			clientName = InetAddress.getLocalHost().getHostName() + " "
					+ System.getProperty("user.name") + " "
					+ InetAddress.getLocalHost().getHostAddress();
			initTime = System.currentTimeMillis();

			if (ClientConfig.getInstance().communication_type
					.equalsIgnoreCase(ClientConfig.RMI_COMMUNICATION)) {
				factory = new ClientFactoryRMI();
			} else {
				if (ClientConfig.getInstance().communication_type
						.equalsIgnoreCase(ClientConfig.TCP_COMMUNICATION)) {
					factory = new ClientFactoryTCP();
				} else {
					if (ClientConfig.getInstance().communication_type
							.equalsIgnoreCase(ClientConfig.LOCAL_COMMUNICATION)) {
						factory = new ClientFactoryLocal();
					} else {
						System.out
								.println("Not implemented communication type: "
										+ ClientConfig.getInstance().communication_type);

					}
				}
			}

		} catch (UnknownHostException e) {
			clientName = "unknown";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * starts the client
	 */
	public void start() {

		try {
			initComunication();
			running = true;
			initTime = System.currentTimeMillis();
			System.out.println("< Asking work to the server ["
					+ ClientConfig.getInstance().server_ipaddr + "] "
					+ new Date());
			while (running) {
				records = worker.getWork(clientName,
						ClientConfig.getInstance().quantityRecords);
				if (running = records.size() > 0) {
					work();
				}
			}
			System.out.println("# Work finished " + worked + " in "
					+ (System.currentTimeMillis() - initTime) + "ms "
					+ new Date());
			System.exit(0);

		} catch (RemoteException e) {
			System.out.println("There was a problem with the RMI connection.");
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println("There was a problem asking for work.");
			System.out.println(e.getMessage());
		}

	}

	/**
	 * creates the worker and the resulter from the client factory
	 * 
	 * @throws RemoteException
	 */
	private void initComunication() throws RemoteException {
		worker = factory.createWorkManager();
		resulter = factory.createResultManager();

	}

	/**
	 * 
	 * inserts the results
	 * 
	 * @param parts
	 *            the number of parts in which the results will be send
	 * @throws RemoteException
	 */
	private void insertResults(int parts) throws RemoteException {
		if (parts > 1) {
			System.out.println("Reconnecting to the server....");
			initComunication();
		}
		System.out.println("< Sending worked records to server.");
		try {
			int antes = 0;
			int cant = (records.size() / parts);
			for (int n = 0; n < parts; n++, antes = cant * n) {
				if (n + 1 == parts) {
					if (resulter.insertResult(clientName,
							new LinkedList<Record>(records.subList(antes,
									records.size())))) {
						worked += records.size() - antes;
						// records = null;
						System.out.println("> Successfully entered records! "
								+ " part " + (parts) + " - "
								+ new Date(System.currentTimeMillis()));
					}
				} else {
					if (resulter.insertResult(clientName, records.subList(
							antes, cant * (n + 1)))) {
						worked += (cant * (n + 1)) - antes;
						// records = null;
						System.out.println("> Successfully entered records! "
								+ new Date(System.currentTimeMillis()));
					}
				}
			}
			records.clear();
			records = null;
		} catch (Exception e) {
			System.out
					.println("There was a problem trying send the results to the server.");
			System.out.println("ERROR:");
			System.out.println(e.getMessage());
			System.out.println();
			System.out.println(".....Trying to send back the results in "
					+ parts + " parts....");
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			insertResults(parts++);
		}
	}

	/**
	 * evaluate the records after received
	 */
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
			 * Update the ISOs dictionary to make the comparisons
			 */
			shapeFileManager.setCountriesISO(worker
					.getCountriesISO(shapeFileManager
							.getCountriesFromCoordinates(records)));

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
				shapeComparison = shapeFileManager.compareCountriesISO(rec
						.getLatitude(), rec.getLongitude(), rec
						.getIso_country_code());
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

			try {
				insertResults(1);
			} catch (RemoteException e) {
				System.out.println("Can't connect to the RMI server.");
				System.out.println("ERROR: ");
				System.out.println(e.getMessage());
			}
		} catch (Exception e) {
			System.out
					.println("There was a problem trying to get the countries codes (ISOs)");
			System.out.println("ERROR: ");
			System.out.println(e.getMessage());
		}

	}

}
