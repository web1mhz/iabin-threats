package org.ciat.ita.client.manage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.ciat.ita.client.ClientConfig;
import org.ciat.ita.model.BufferedDataOutputStream;
import org.ciat.ita.model.WorldMask;


public class EnvironmentMaskManager {

	private static final String EXC_IO_MESSAGE = "There was an error on reading/writing the files";
	private String[] variablesName;
	/**
	 * @uml.property name="worldMask"
	 * @uml.associationEnd multiplicity="(1 1)" ordering="true"
	 *                     inverse="base:model.WorldMask"
	 */
	private WorldMask worldMask;
	private String directoryFiles;
	private RandomAccessFile[] readers;
	private File[] listEMMFiles;
	private int xMin;
	private int yMin;
	private long salto;
	private int threadCounter;
	private RandomAccessFile readerAIO;

	// El directorio debe contener los directorios de cada una de las variables
	// o en su defecto los archivos ASCII.

	/**
	 * This constructor should be called after the constructor of
	 * WorldMaskManager class.
	 * 
	 * @param directoryFiles
	 *            - The path where the bioclimatics files are (format & AIO).
	 * @param worldMask
	 *            - The object that represents a WorlMask class.
	 * @throws FileNotFoundException
	 *             - In case the path doesn't contains the files required.
	 */
	public EnvironmentMaskManager(String directoryFiles, WorldMask worldMask)
			throws FileNotFoundException, IOException {

		variablesName = ClientConfig.getInstance().variablesName
				.toArray(new String[ClientConfig.getInstance().variablesName.size()]);
		readers = new RandomAccessFile[variablesName.length];
		listEMMFiles = new File[variablesName.length];
		this.directoryFiles = directoryFiles;
		this.worldMask = worldMask;

		// validateEMMFiles();
		validateAIOEMMFile();
		// readVariablesFromEMMFiles(189, 190);

	}

	private synchronized void validateAIOEMMFile() throws FileNotFoundException {
		String format = "BioAIO.format";
		try {
			// Busca el archivo .emm que contiene la información de todas las
			// variables bioclimáticas.
			File directory = new File(directoryFiles);
			if (directory.isDirectory()) {
				File emm = new File(directory + File.separator + "BioAIO.emm");
				File emmFormat = new File(directory + File.separator + format);
				if (emm.exists() && emmFormat.exists() && emm.isFile()
						&& emmFormat.isFile()) {
					ObjectInputStream readerFormat;
					readerFormat = new ObjectInputStream(new FileInputStream(
							emmFormat));
					variablesName = (String[]) readerFormat.readObject();
					readerFormat.close();
					readerAIO = new RandomAccessFile(emm, "r");

				} else {
					// El archivo no existe y hay que crearlo.
					File[] asciiFiles = new File[variablesName.length];
					for (int c = 0; c < variablesName.length; c++) {
						asciiFiles[c] = new File(directory + File.separator
								+ variablesName[c] + ".asc");
					}
					generateAllInOneEMMFile(asciiFiles);
				}
			} else {
				throw new FileNotFoundException(
						"Files in variables path not found, please correct the path in the client configuration file");
			}
		} catch (IOException e) {
			System.err.println(EXC_IO_MESSAGE);
		} catch (ClassNotFoundException e) {
			System.err.println("The " + format + " file is not well created");
		}
	}
	
	/**
	 * @deprecated
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unused")
	private synchronized void validateEMMFiles() throws FileNotFoundException {
		// Buscar archivos ya convertidos a bytes (.emm), de lo contrario
		// realizar la conversion.

		File directory = new File(directoryFiles);
		if (directory.isDirectory()) {
			File[] listEMM = directory.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().toLowerCase().endsWith(".emm");
				}
			});
			Arrays.sort(listEMM);

			List<File> arr = Arrays.asList(listEMM);
			// Todos los archivos .emm de las variables bioclimaticas deben
			// estar en dicha carpeta. De lo contrario se arrojara una excepcion
			// indicando que hay archivos insuficientes.
			boolean allEMMFiles = true;
			for (String variable : variablesName) {
				if (!arr.contains(new File(directory + File.separator
						+ variable + ".emm"))) {
					allEMMFiles = false;
				}
			}
			if (!allEMMFiles) {
				System.out
						.println("The directory introduced doesn't contain all the EMM files");
				System.out.println("Looking for ASCII files.......");

				// Validar que en dicho directorio alemnos se encuentren todos
				// los archivos ASCII para poder realizar la conversion.
				File[] listASCII = directory.listFiles(new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						return pathname.getName().toLowerCase()
								.endsWith(".asc");
					}
				});
				Arrays.sort(listASCII);

				boolean allASCIIFiles = true;
				for (File asciiFile : listASCII) {
					if (!ClientConfig.getInstance().variablesName.contains(asciiFile
							.getName().substring(0,
									asciiFile.getName().length() - 4))) {
						allASCIIFiles = false;
					}
				}
				if (!allASCIIFiles) {
					throw new FileNotFoundException(
							"The directory introduced doesn't contain all the ASCII files, the program couldn't continue");
				} else {
					// Se crea un hilo controlado que realizara dos ejecuciones
					// al tiempo, para generar el archivo EMM correspondiente a
					// su archivo ASCII.
					ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(
							2);
					threadCounter = 0;
					// Se descartan los archivos EMM que ya existan... y se
					// generan los demás.
					for (String nameFileToGenerate : variablesName) {
						File fileEMM = new File(directory + File.separator
								+ nameFileToGenerate + ".emm");
						if (fileEMM.exists()) {
							System.out.println("File " + fileEMM.getName()
									+ " already exists");
						} else {
							threadPoolExecutor.submit(new EMMGeneratorThread(
									new File(directory + File.separator
											+ nameFileToGenerate + ".asc"),
									this));
							threadCounter++;
						}
					}

					// No continuara hasta que todos los hilos terminen.
					try {
						while (threadCounter != 0) {
							this.wait();
							threadCounter--;
						}
						System.out.println("EMM Files created succesfully!");
						validateEMMFiles();

					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
			} else {
				// Ya están todos los correspondientes EMM files, por tal motivo
				// se crea el arreglo de flujos a cada uno de dichos archivos.
				prepareEMMStreams(directory);
			}
		} else {
			throw new FileNotFoundException(
					"The parameter introduced is not a valid directory");
		}

	}

	private void prepareEMMStreams(File directory) throws FileNotFoundException {
		File file;
		for (int c = 0; c < variablesName.length; c++) {
			file = new File(directory + File.separator + variablesName[c]
					+ ".emm");
			readers[c] = new RandomAccessFile(file, "r");
			listEMMFiles[c] = file;
			// listASCIIFiles[c] =
			// directory+File.separator+i.next()+".asc";
		}
	}

	private synchronized void goNotify() {
		this.notify();
	}

	private class EMMGeneratorThread implements Runnable {
		private File ascii;
		private EnvironmentMaskManager emm;

		public EMMGeneratorThread(File ascii, EnvironmentMaskManager emm) {
			this.ascii = ascii;
			this.emm = emm;
		}

		@Override
		public void run() {
			generateEMMFiles(ascii);

			emm.goNotify();
		}

		@Override
		protected void finalize() throws Throwable {
			super.finalize();

		}
	}

	private boolean generateAllInOneEMMFile(File[] asciiFiles) {
		try {
			BufferedReader[] lectores = new BufferedReader[asciiFiles.length];
			for (int c = 0; c < asciiFiles.length; c++) {
				lectores[c] = new BufferedReader(new FileReader(asciiFiles[c]));
			}
			BufferedDataOutputStream writer = new BufferedDataOutputStream(
					new FileOutputStream(directoryFiles + File.separator
							+ "BioAIO.emm"));

			System.out.println("Generating file ..." + File.separator
					+ "BioAIO.emm");

			// Partes --> [# variables] x [# Cols]
			String[][] partes = new String[asciiFiles.length][worldMask
					.getNcols()];

			// Leyendo las primeras 6 lineas de todos los archivos, ya que no
			// brinda ninguna información relevante que ya sepamos.
			for (BufferedReader lec : lectores) {
				for (int c = 0; c < 6; c++) {
					lec.readLine();
				}
			}

			// Creando un archivo de formato en donde dará a conocer el orden en
			// que serán guardadas las variables bioclimaticas. Se escribirá un
			// arreglo de String[].
			ObjectOutputStream writerOther = new ObjectOutputStream(
					new FileOutputStream(directoryFiles + File.separator
							+ "BioAIO.format"));
			writerOther.writeObject(variablesName);
			writerOther.flush();
			writerOther.close();

			// Leyendo todo y generando el archivo...
			for (int w = 0; w < worldMask.getNrows(); w++) {
				for (int c = 0; c < asciiFiles.length; c++) {
					partes[c] = lectores[c].readLine().split(" ");
				}
				for (int i = 0; i < worldMask.getNcols(); i++) {
					for (int c = 0; c < asciiFiles.length; c++) {
						writer.writeShort(Short.parseShort(partes[c][i]));
					}
				}
				if ((w % (worldMask.getNrows() / 30) == 0)) {
					System.out.print((int) (((double) w / (double) worldMask
							.getNrows()) * 100)
							+ "% ");
				}
				writer.flush();
			}
			System.out.println();
			writer.close();
			for (int c = 0; c < 6; c++) {
				lectores[c].close();
			}
			validateAIOEMMFile();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(EXC_IO_MESSAGE);
		}

		return true;
	}

	private boolean generateEMMFiles(File asciiFile) {
		try {
			BufferedReader lector = new BufferedReader(
					new FileReader(asciiFile));
			String fileName = asciiFile.getAbsolutePath().substring(0,
					asciiFile.getAbsolutePath().length() - 4);
			BufferedDataOutputStream writer = new BufferedDataOutputStream(
					new FileOutputStream(fileName + ".emm"));

			// Imprimiendo archivo creado
			System.out.println("Generating file ..." + File.separator
					+ fileName + ".emm");

			String[] partes;

			for (int c = 0; c < 6; c++) {
				partes = lector.readLine().split(" ");
				writer.writeDouble(Double
						.parseDouble(partes[partes.length - 1]));
			}
			writer.flush();
			for (int c = 0; c < worldMask.getNrows(); c++) {
				partes = lector.readLine().split(" ");
				for (String parte : partes) {
					writer.writeShort(Short.parseShort(parte));
				}
				writer.flush();
			}
			writer.close();
			lector.close();
			System.out.println("File ..." + File.separator + fileName
					+ ".emm --> OK");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * This method calculate the value represented in the bioclimatic variables
	 * for an specific coordenate. This method use the AIO file.
	 * 
	 * @param lng
	 *            - longitude
	 * @param lat
	 *            - latitude
	 * @return A Map Object where the keys are the bio_x name and his
	 *         corresponding value for that especific coordenate.
	 */
	public Map<String, Short> readVariablesFromEmmAIOFile(double lng, double lat) {
		try {

			Map<String, Short> variablesFromEMMFile = new HashMap<String, Short>();
			yMin = worldMask.num_raw(lat);
			xMin = worldMask.num_column(lng);

			salto = (long) (((yMin * worldMask.getNcols()) + xMin) * 2)
					* (variablesName.length);
			readerAIO.seek(salto);
			for (String var : variablesName) {
				variablesFromEMMFile.put(var, readerAIO.readShort());
			}
			return variablesFromEMMFile;
		} catch (IOException e) {
			System.err.println(EXC_IO_MESSAGE);

		}

		return null;
	}

	/**
	 * @deprecated Use readVariablesFromEmmAIOFile
	 * @param lng
	 * @param lat
	 * @return
	 */
	public Map<String, Short> readVariablesFromEMMFiles(double lng, double lat) {
		// TODO Mejorar para que no consuma tanta memoria
		Map<String, Short> variablesFromEMMFiles = new HashMap<String, Short>();
		try {
			yMin = worldMask.num_raw(lat);
			xMin = worldMask.num_column(lng);

			// calcular cual es el numero bytes que debe saltar para cada uno de
			// los archivos de las variables.
			salto = (8 * 6) + (((yMin * worldMask.getNcols()) + xMin) * 2);
			for (int c = 0; c < listEMMFiles.length; c++) {
				readers[c].seek(salto);
				variablesFromEMMFiles.put(variablesName[c], readers[c]
						.readShort());
			}
			return variablesFromEMMFiles;

		} catch (IOException e) {
			System.err.println(EXC_IO_MESSAGE);
		}
		return null;
	}

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("[ARGUMENTS]");
			System.out.println();
			System.out
					.println("          [-d]  <exactly location of the Directory that contains the bioclimatic ASCII files>");
			System.out
					.println("		NOTE: The directory must contain all the ASCII files named like the XML File");
			System.out.println();
		} else {
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("-d")) {
					File dir = new File(args[1]);
					if (dir.exists() && dir.isDirectory()) {
						File[] files = dir.listFiles(new FileFilter() {
							@Override
							public boolean accept(File p) {
								if (p.getName().toLowerCase().endsWith(".asc")) {
									return true;
								}
								return false;
							}
						});
						WorldMaskManager wmm = new WorldMaskManager(
								ClientConfig.getInstance().maskFile);
						try {
							EnvironmentMaskManager emm = new EnvironmentMaskManager(
									args[1], wmm.getWorldMask());
							emm.generateAllInOneEMMFile(files);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else {
						System.out
								.println("The file doesn't exist or is not a directory");
					}
				}
			} else {
				System.out.println("Insuficient Argumments!");
			}
		}
	}

}
