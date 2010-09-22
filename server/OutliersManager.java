package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * Services to update the value of the outlier count in the table of goods For
 * each specie and for each environmental variable calculate variables to mark
 * the outlier records using the follwing procedure: sort the records in
 * ascendent order according the variable value Q1=is the value of the
 * (n*0.25)th record or the up aproximated Q3=is the value of the (n*0.73)th
 * record or the up aproximated IQR = Q3 - Q1 LB = Q1 - 1.5*IQR UB = Q3 +
 * 1.5*IQR [IF (Yi < LB OR Yi > UB) THEN Outlier ELSE OK where Yi is the value
 * of the variable Y of the record i
 * 
 * 
 */
public class OutliersManager {

	private static Connection conx;
	private static DecimalFormat dc = new DecimalFormat("###");
	private static int n;
	private static ResultSet rs;
	private static Set<Integer> nub_concepts;
	private static Map<Integer, Byte> outliers;
	private static final String XML_FILE = "last_nub.txt";
	private static final String NUB_CONCEPT_ID = "nub_concept_id";
	private static int last_nub = 0;

	public static void main(String[] args) {

		ServerConfig.init();
		try {
			DataBaseManager.registerDriver();
			conx = DataBaseManager.openConnection(
					server.ServerConfig.database_user,
					ServerConfig.database_password);
			n = ServerConfig.dbVariablesName.size();
			evaluateOutliers();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void evaluateOutliers() throws SQLException, IOException {
		loadSpecies();

		System.out.println("# Evaluating outliers for " + nub_concepts.size()
				+ " species " + new Date(System.currentTimeMillis()));

		int i = 0;
		FileWriter file;
		long inittime = System.currentTimeMillis();
		outliers = new HashMap<Integer, Byte>();
		/*
		 * For each specie
		 */
		 for (Integer nub : nub_concepts)
		{
			/*
			 * Integer nub = 13279733;
			 */
			i++;
			outliers.clear();

			/*
			 * detect the outlier records
			 */
			detectOutliersForSpecie(nub);

			for (Integer id : outliers.keySet()) {
				insertOutlierValueForRecord(id, outliers.get(id));
			}

			file = new FileWriter(XML_FILE, false);
			file.write(nub + "\n");
			file.flush();
			file.close();
			if (i % (nub_concepts.size()/20) == 0) {
				System.out.print((dc
						.format((i / (double) nub_concepts.size()) * 100))
						+ "% ");
			}
			// System.out.println(i + " " + nub + " out:" +
			// (System.currentTimeMillis()-inittime));
		}

		System.out.println("100%");
		System.out.println("# Evaluation finished for " + nub_concepts.size()
				+ " species in " + (System.currentTimeMillis() - inittime)+"ms");
		file = new FileWriter(XML_FILE, false);
		file.write("-1\n");
		file.flush();
		file.close();
	}

	public static void evaluateOutliers(Connection connection)
			throws SQLException, IOException {
		conx = connection;
		evaluateOutliers();

	}

	private static void loadSpecies() throws SQLException, IOException {

		int name = 0;

		File arch = new File(XML_FILE);
		if (arch.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(arch));
			try{
			last_nub = Integer.parseInt(reader.readLine());
			}catch(NumberFormatException e){
				System.err.println("The file "+XML_FILE+" does not have a number format, please correct it or delete it");
			}
		}

		rs = DataBaseManager.makeQuery("select " + NUB_CONCEPT_ID + " from "
				+ ServerConfig.dbTableGoods + " group by " + NUB_CONCEPT_ID,
				conx);
		nub_concepts = new LinkedHashSet<Integer>();
		while (rs.next()) {
			name = rs.getInt("nub_concept_id");
			if (name > last_nub) {
				nub_concepts.add(name);
			}
		}
		rs.getStatement().close();
		rs.close();
		rs = null;

	}

	public static int countRecordsBySpecie(int nub_concept)
			throws SQLException {
		int nRecords = 0;
		rs = DataBaseManager.makeQuery("select count(*) \"c\" from "
				+ ServerConfig.dbTableGoods + " where " + NUB_CONCEPT_ID + "="
				+ nub_concept, conx);
		if (rs.next())
			nRecords = rs.getInt("c");
		rs.getStatement().close();
		rs.close();
		rs = null;
		return nRecords;
	}

	private static int insertOutlierValueForRecord(int recordID, byte value) {
		return DataBaseManager.makeChange("update " + ServerConfig.dbTableGoods
				+ " set " + ServerConfig.dbOutlierCount + "=" + value
				+ " where id=" + recordID, conx);

	}

	private static Byte value;

	private static void aumentOutlierForRecord(int id) {
		value = outliers.get(Integer.valueOf(id));
		if (value == null) {
			outliers.put(Integer.valueOf(id), Byte.valueOf((byte) 1));

		} else {
			outliers.put(Integer.valueOf(id), Byte.valueOf((byte) (value
					.byteValue() + 1)));

		}

	}

	@SuppressWarnings("unchecked")
	private static void detectOutliersForSpecie(int nub_concept)
			throws SQLException {
		/*
		 * 1. hacer metodo para todas las variables de una sola vez en vez es
		 * decir, hacer un arreglo para las variables UB y LB para aprovechar
		 * unsa sola consultada para todas las variabels bioclimaticas-hecho
		 * 
		 * 2. quitar el order by de la segunda consulta-hecho
		 * 
		 * 3. posiblemente cargar los datos en memoria, dependiedo de la
		 * cantidad de datos usar este metodo o el anterior
		 * 
		 * 4. utilizar hilos por especie
		 */
		int v = -1;

		/*
		 * key: record id value: varibles values
		 */
		Map<Integer, List> recordsValues = new HashMap<Integer, List>();

		/*
		 * Get records for specie
		 */
		ResultSet rs = DataBaseManager.makeQuery("select * from "
				+ ServerConfig.dbTableGoods + " where " + NUB_CONCEPT_ID + "="
				+ nub_concept, conx);
		double Q1 = 0, Q3 = 0, IQR = 0, LB[] = new double[n], UB[] = new double[n];
		int p1 = 0, p3 = 0;
		/*
		 * all records values of a variable
		 */
		List<Double>[] variableValues = new ArrayList[n];
		/*
		 * clear variable values
		 */
		for (int j = 0; j < variableValues.length; j++) {
			// variableValues[j].clear();
			variableValues[j] = new ArrayList<Double>();
		}
		/*
		 * For each record
		 */
		while (rs.next()) {

			v = -1;
			List recordValues = new ArrayList<Double>();
			/*
			 * Save each varible value in arraylist
			 */
			for (String varColumn : ServerConfig.dbVariablesName) {
				v++;
				recordValues.add(rs.getDouble(varColumn));
				variableValues[v].add(rs.getDouble(varColumn));
			}
			recordsValues.put(rs.getInt("id"), recordValues);
		}
		rs.getStatement().close();
		rs.close();
		rs = null;
		double nRecords = variableValues[0].size();
		p1 = (int) (nRecords * 0.25);
		p3 = (int) (nRecords * 0.75);
		v = -1;
		/*
		 * for each variable
		 */
		for (@SuppressWarnings("unused") String varColumn : ServerConfig.dbVariablesName) {
			v++;

			/*
			 * Sort the list of values
			 */
			Collections.sort(variableValues[v]);
			
			/*if(varColumn.equals("alt")){
				for(int i=0;i<variableValues[v].size();i++){
					System.out.println(variableValues[v].get(i));
				}
			}*/
			
			Q1 = Double.parseDouble(variableValues[v].get(p1).toString());
			Q3 = Double.parseDouble(variableValues[v].get(p3).toString());

			IQR = Q3 - Q1;
			LB[v] = Q1 - (1.5 * IQR);
			UB[v] = Q3 + (1.5 * IQR);
			/*System.out.println(varColumn+" p1:" + p1 + " p3:" + p3 + " Q1:" + Q1 + " Q3:"
					+ Q3 + " IQR:" + IQR + " LB:" + LB[v] + " UB:" + UB[v]);*/
		}

		v = -1;
		for (@SuppressWarnings("unused") String varColumn : ServerConfig.dbVariablesName) {

			v++;
			for (Integer id : recordsValues.keySet()) {
				/*
				 * if record value is < LB or > UB, aument it's outlier value
				 */
				if (Double.parseDouble(recordsValues.get(id).get(v).toString()) < LB[v]
						|| Double.parseDouble(recordsValues.get(id).get(v)
								.toString()) > UB[v]) {
					aumentOutlierForRecord(id);
				}
			}
		}

		// if(nub_concept==13674438){
		// System.out.println("var:"+varColumn+"" +
		// " Q1:"+Q1+
		// " Q3:"+Q3+
		// " IQR:"+IQR+
		// " LB:"+LB+
		// " UB:"+UB
		// );
		//				
		// }
	}
}
