package org.ciat.ita.client.correctormanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.ciat.ita.client.ClientConfig;
import org.ciat.ita.server.ServerConfig;
import org.ciat.ita.server.database.DataBaseManager;


public class MaxentManager {

	public void exportSpeciesIntoCsv(String tableName, int number,
			String outputDirectory) {

		try {
			String tempFile = "last_nub.txt";
			DataBaseManager.registerDriver();
			Connection con = DataBaseManager.openConnection(
					ServerConfig.getInstance().database_user, ServerConfig.getInstance().database_password);
			boolean hasMoreRecords = true;
			File arch = new File(tempFile);
			int lastNubId = 0;
			if (arch.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(
						tempFile));
				lastNubId = Integer.parseInt(reader.readLine());
			}
			long before;
			long timeTotal = 0;
			long temp;
			int cont;
			FileWriter file;
			// Recorre lote
			while (hasMoreRecords) {
				cont = 0;

				before = System.currentTimeMillis();
				ResultSet res = DataBaseManager
						.makeQuery(
								"SELECT nub_concept_id, count(nub_concept_id) number FROM "
										+ tableName
										+ " WHERE nub_concept_id > "
										+ lastNubId
										+ " GROUP BY nub_concept_id ORDER BY nub_concept_id LIMIT "
										+ number, con);

				// Recorre especies
				while (res.next()) {
					cont++;
					lastNubId = res.getInt(1);
					file = new FileWriter(tempFile, false);
					file.write(lastNubId + "\n");
					file.flush();
					file.close();
					before = System.currentTimeMillis();
					String sentencia = "select 'taxon','lon','lat', 'bio_1','bio_2','bio_3','bio_4','bio_5','bio_6','bio_7','bio_8','bio_9','bio_10','bio_11','bio_12','bio_13','bio_14','bio_15','bio_16','bio_17','bio_18','bio_19' union (select nub_concept_id,longitude,latitude,bio_1,bio_2,bio_3,bio_4,bio_5,bio_6,bio_7,bio_8,bio_9,bio_10,bio_11,bio_12,bio_13,bio_14,bio_15,bio_16,bio_17,bio_18,bio_19 into outfile '"
							+ outputDirectory
							+ "/specie_"
							+ lastNubId
							+ "_"
							+ res.getInt(2)
							+ ".csv' FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' from "
							+ tableName
							+ " where nub_concept_id="
							+ lastNubId
							+ ")";
					ResultSet r = DataBaseManager.makeQuery(sentencia, con);
					r.getStatement().close();
					r.close();
				}

				file = new FileWriter(tempFile, false);
				file.write(0);
				file.flush();
				file.close();

				DecimalFormat format = new DecimalFormat("###############.000");
				temp = System.currentTimeMillis() - before;
				timeTotal += temp;
				System.out.println(format.format((temp / 1000.0))
						+ "seg - TOTAL: "
						+ format.format((timeTotal / 60000.0))
						+ "min | LastNubConceptID: " + lastNubId);
				if (cont < number)
					hasMoreRecords = false;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public synchronized void executeMaxent(String maxentFile,
			String sampleDirectory, String backgroundFile,
			String outputLambdaDirectory, String bioclimaticDirectory,
			String finalOutput, int threadSize, int poolSize) {

		File maxent = new File(maxentFile);
		File sample = new File(sampleDirectory);
		File background = new File(backgroundFile);
		File outputLambda = new File(outputLambdaDirectory);
		File climaticDir = new File(bioclimaticDirectory);
		File output = new File(finalOutput);

		String error = "";

		if (!maxent.exists() || !maxent.isFile()) {
			error += "Maxent jar file not found\n";
		}
		if (!sample.exists() || !sample.isDirectory()) {
			error += "Sample file not found\n";
		}
		if (!background.exists() || !background.isFile()) {
			error += "Background file not found\n";
		}
		if (!outputLambda.exists() || !outputLambda.isDirectory()) {
			error += "Lambda output directory not found\n";
		}
		if (!climaticDir.exists() || !climaticDir.isDirectory()) {
			error += "bioclimatic directory not found\n";
		}
		if (!output.exists() || !output.isDirectory()) {
			error += "Output directory not found";
		}

		File[] sampleFiles = sample.listFiles(new FileFilter() {

			@Override
			public boolean accept(File p) {
				if (p.getName().endsWith(".csv"))
					return true;
				return false;
			}
		});

		if (error.equals("")) {
			int cont = 0;
			long before = System.currentTimeMillis();
			long temp = 0, timeTotal = 0;
			File[] others = null;
			int numberRecords = 0, testPercentage = 0;
			ScheduledThreadPoolExecutor poolThread = new ScheduledThreadPoolExecutor(
					threadSize);

			for (File sampleFile : sampleFiles) {
				
				if (!(new File(finalOutput + File.separatorChar
						+ (sampleFile.getName().split("_")[1])+".asc")).exists()) {
					cont++;

					poolThread.submit(new MaxentProcessThread(sampleFile,
							numberRecords, testPercentage, maxentFile,
							backgroundFile, outputLambdaDirectory,
							outputLambda, others, finalOutput, climaticDir,
							this));

					// maxentProcess(sampleFile, numberRecords, testPercentage,
					// maxentFile, backgroundFile, outputLambdaDirectory,
					// outputLambda, others, finalOutput, climaticDir);

					// System.out.println(cont);
					// System.out.println(poolThread.getActiveCount());
					if (cont % poolSize == 0) {
						try {
							while (cont != 0) {
								this.wait();
								cont--;
							}
							DecimalFormat format = new DecimalFormat(
									"############.000");
							temp = System.currentTimeMillis() - before;
							timeTotal += temp;
							System.out.println(format.format((temp / 1000.0))
									+ "seg - TOTAL: "
									+ format.format((timeTotal / 60000.0))
									+ "min | Last File: " + sampleFile);
						} catch (InterruptedException e) {
							System.out.println(e.getMessage());
						}
					}
				} else {
					System.out.println("File " + finalOutput
							+ File.separatorChar
							+ (sampleFile.getName().split("_")[1])
							+ " already exists");
				}
			}

			while (cont != 0) {
				try {
					this.wait();
					cont--;
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
			System.exit(0);
		} else {
			System.out.println(error);
		}

	}

	private void maxentProcess(File sampleFile, int numberRecords,
			int testPercentage, String maxentFile, String backgroundFile,
			String outputLambdaDirectory, File outputLambda, File[] others,
			String finalOutput, File climaticDir) {
		try {
			numberRecords = Integer
					.parseInt(sampleFile.getName().split("[_.]")[2]);
			if (numberRecords <= 12) {
				testPercentage = 0;
			} else {
				if (numberRecords > 12 && numberRecords <= 20) {
					testPercentage = 15;
				} else {
					testPercentage = 20;
				}
			}
			int memmory = 1000;
			// Corriendo Maxent desde SWD.
			String line = "java -mx" + memmory + "m -jar " + maxentFile
					+ " -s " + sampleFile + " -e " + backgroundFile + " -o "
					+ outputLambdaDirectory + " -X " + testPercentage
					+ " nowarnings -z -a -r";

			// System.out.println(line);

			Process maxentProcess = Runtime.getRuntime().exec(line);
			maxentProcess.waitFor();
			maxentProcess.destroy();

			// Corriendo maxent desde archivos lambdas.
			String line2 = "java -mx" + memmory + "m -cp " + maxentFile
					+ " density.Project " + outputLambdaDirectory
					+ File.separatorChar + (sampleFile.getName().split("_")[1])
					+ ".lambdas " + climaticDir + " " + finalOutput
					+ File.separatorChar + (sampleFile.getName().split("_")[1])
					+ " nowarnings -r -a dontwriteclampgrid";
			// System.err.println(line2);
			maxentProcess = Runtime.getRuntime().exec(line2);
			maxentProcess.waitFor();
			maxentProcess.destroy();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

	private class MaxentProcessThread implements Runnable {

		private File sampleFile;
		private int numberRecords;
		private int testPercentage;
		private String maxentFile;
		private String backgroundFile;
		private String outputLambdaDirectory;
		private File outputLambda;
		private File[] others;
		private String finalOutput;
		private File climaticDir;
		private MaxentManager mm;

		public MaxentProcessThread(File sampleFile, int numberRecords,
				int testPercentage, String maxentFile, String backgroundFile,
				String outputLambdaDirectory, File outputLambda, File[] others,
				String finalOutput, File climaticDir, MaxentManager mm) {
			super();
			this.sampleFile = sampleFile;
			this.numberRecords = numberRecords;
			this.testPercentage = testPercentage;
			this.maxentFile = maxentFile;
			this.backgroundFile = backgroundFile;
			this.outputLambdaDirectory = outputLambdaDirectory;
			this.outputLambda = outputLambda;
			this.others = others;
			this.finalOutput = finalOutput;
			this.climaticDir = climaticDir;
			this.mm = mm;
		}

		@Override
		public void run() {

			maxentProcess(sampleFile, numberRecords, testPercentage,
					maxentFile, backgroundFile, outputLambdaDirectory,
					outputLambda, others, finalOutput, climaticDir);

			mm.goNotify();

		}

	}

	private synchronized void goNotify() {
		this.notify();
	}

	public static void main(String[] args) {

		if (args == null || args.length == 0) {
			System.out.println("[ARGUMENTS]");
			System.out.println();
			System.out.println("  <csv>");
			System.out
					.println("          [-n]  <Number of records that should be analized to show the time statistics>");
			System.out
					.println("          [-d]  <Exactly location of the output directory>");
			System.out.println();
			System.out.println("  <maxent>");
			System.out
					.println("          [-i] <Maxent number of instances that will be executed at same time>");

		} else {
			if (args[0].equalsIgnoreCase("csv")) {
				if (args.length == 5) {
					try {
						int number = -1;
						String dir = null;
						for (int c = 1; c < args.length; c++) {
							if (args[c].equalsIgnoreCase("-n")) {
								number = Integer.parseInt(args[c + 1]);
							}
							if (args[c].equalsIgnoreCase("-d")) {
								dir = args[c + 1];
							}
						}
						System.out.println(number + " " + dir);
						if (number != -1 && dir != null) {
							ClientConfig.init();
							String tableName = ServerConfig.getInstance().dbTableFinalRecords;
							MaxentManager mm = new MaxentManager();
							mm.exportSpeciesIntoCsv(tableName, number, dir);
							System.out.println("\nOk!");
						} else {
							System.out.println("Wrong Arguments.");
						}
					} catch (NumberFormatException e) {
						System.out.println("Incorrect Number.");
					}
				} else {
					System.out.println("Insuficient Arguments.");
				}
			}
			if (args[0].equalsIgnoreCase("maxent")) {
				if (args.length == 3) {
					if (args[1].equalsIgnoreCase("-i")) {
						try {
							ClientConfig.init();

							int threadSize = Integer.parseInt(args[2]);// Could
							// be
							// the
							// number
							// of
							// Processors
							int poolSize = threadSize * 10; // Pool Size
							String maxentFile = ClientConfig.maxent_file;
							String sampleDirectory = ClientConfig.sample_dir;
							String backgroundFile = ClientConfig.backgroung_file;
							String outputLambdaDirectory = ClientConfig.lambda_output_dir;
							String bioclimaticDir = ClientConfig.maxent_variables_dir;
							String finalOutput = ClientConfig.final_output_dir;
							MaxentManager mm = new MaxentManager();
							mm.executeMaxent(maxentFile, sampleDirectory,
									backgroundFile, outputLambdaDirectory,
									bioclimaticDir, finalOutput, threadSize,
									poolSize);
							System.out.println("\nOk!");
						} catch (NumberFormatException e) {
							System.out.println("Incorrect Number.");
						}
					} else {
						System.out.println("Wrong Arguments.");
					}
				} else {
					System.out.println("Insuficient Arguments.");
				}
			}

		}

	}

}
