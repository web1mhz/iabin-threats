package org.ciat.ita.server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.ciat.ita.model.Record;
import org.ciat.ita.model.ShapeFile;
import org.ciat.ita.server.ServerConfig;
import org.omg.PortableServer.LifespanPolicyOperations;

public class PortalManager {

	private Connection conx;
	private ResultSet rs;
	/**
	 * @uml.property name="regs"
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="true"
	 *                     inverse="base:model.Record"
	 */
	private List<Record> regs;
	private boolean endRecords;
	private int lastID;

	/**
	 * Creates the manager of the database
	 * 
	 * @param conx
	 *            is the connection to the database
	 */
	public PortalManager(Connection conx) {
		super();
		this.conx = conx;
		this.endRecords = true;
		this.lastID = 0;
		this.regs = new LinkedList<Record>();
	}

	/**
	 * Inserts a list of records that were evaluated as good in the goods table
	 * 
	 * @param recs
	 *            are the records to be insert
	 * @return the number of records inserted.
	 */
	public int insertGoodRecords(List<Record> recs) {
		if (recs != null && recs.size() > 0) {
			StringBuffer query = new StringBuffer();

			query.append("INSERT INTO " + ServerConfig.getInstance().dbTableGoods);
			query.append("(id, nub_concept_id, longitude, latitude, iso_country_code, canonical");
			for (String var : recs.get(0).getVariables().keySet()) {
				query.append(", " + var);
			}
			query.append(") values");
			for (Record rec : recs) {
				query.append("(" + rec.getId() + "," + rec.getNub_concept_id() + "," + rec.getLongitude()
						+ "," + rec.getLatitude() + ",'" + rec.getIso_country_code() + "','"
						+ DataBaseManager.correctStringToQuery(getCanonical(rec.getNub_concept_id())) + "'");
				for (String var : rec.getVariables().keySet()) {
					query.append("," + rec.getVariables().get(var));
				}
				query.append("),");
			}
			query.deleteCharAt(query.length() - 1);
			query.append(";");

			// return DataBaseManager.makeChange(query.toString(), conx);
			//System.out.println(query.toString());
			return DataBaseManager.makeChange(query.toString(), conx);
		}
		
		return 0;
	}

	/**
	 * Inserts a record that was evaluated as good in the goods table
	 * 
	 * @param rec
	 *            is the record to be insert
	 * @return 1 - if the insertion was succes. 0 - otherwise.
	 */
	public int insertGoodRecord(Record rec) {

		StringBuffer query = new StringBuffer();

		query.append("INSERT INTO " + ServerConfig.getInstance().dbTableGoods);
		query.append("(id, nub_concept_id, longitude, latitude, iso_country_code, canonical");
		for (String var : rec.getVariables().keySet()) {
			query.append(", " + var);
		}
		query.append(") values(" + rec.getId() + "," + rec.getNub_concept_id() + "," + rec.getLongitude()
				+ "," + rec.getLatitude() + ",'" + rec.getIso_country_code() + "','"
				+ DataBaseManager.correctStringToQuery(getCanonical(rec.getNub_concept_id())) + "'");
		for (String var : rec.getVariables().keySet()) {
			query.append("," + rec.getVariables().get(var));
		}
		query.append(");");
		return DataBaseManager.makeChange(query.toString(), conx);

	}

	/**
	 * Inserts a list of records that weren't evaluated as good in the unreliable table
	 * 
	 * @param recs
	 *            are the records to be insert
	 * @return the number of records inserted
	 */
	public int insertUnreliableRecords(List<Record> recs) {
		if (recs != null && recs.size() > 0) {
			StringBuffer query = new StringBuffer();
			query
					.append("Insert into "
							+ ServerConfig.getInstance().dbTableUnreliable
							+ " (id, nub_concept_id, longitude, latitude, iso_country_code, canonical, error) values ");

			for (Record rec : recs) {
				if (rec.getNote().equals(ShapeFile.E_NULL_COUNTRY)) {
					query.append("(" + rec.getId() + "," + rec.getNub_concept_id() + "," + rec.getLongitude()
							+ "," + rec.getLatitude() + "," + rec.getIso_country_code() + ",'"
							+ DataBaseManager.correctStringToQuery(getCanonical(rec.getNub_concept_id())) + "','"
							+ rec.getNote() + "'),");
				} else {
					query.append("(" + rec.getId() + "," + rec.getNub_concept_id() + "," + rec.getLongitude()
							+ "," + rec.getLatitude() + ",'" + rec.getIso_country_code() + "','"
							+ DataBaseManager.correctStringToQuery(getCanonical(rec.getNub_concept_id())) + "','"
							+ rec.getNote() + "'),");
				}				
			}
			query.deleteCharAt(query.length() - 1);
			query.append(";");
			return DataBaseManager.makeChange(query.toString(), conx);
		}
		return 0;
	}

	/**
	 * Inserts a record that was evaluated as good in the goods table
	 * 
	 * @param rec
	 *            is the record to be insert
	 * @return
	 */
	public int insertGoodRecordWithCanonical(Record rec) {

		StringBuffer query = new StringBuffer();

		query.append("INSERT INTO " + ServerConfig.getInstance().dbTableGoods);
		query.append("(id, nub_concept_id, longitude, latitude, iso_country_code, canonical");
		for (String var : rec.getVariables().keySet()) {
			query.append(", " + var);
		}
		query.append(") values(" + rec.getId() + "," + rec.getNub_concept_id() + "," + rec.getLongitude()
				+ "," + rec.getLatitude() + ",'" + rec.getIso_country_code() + "','"
				+ DataBaseManager.correctStringToQuery(rec.getCanonical()) + "'");
		for (String var : rec.getVariables().keySet()) {
			query.append("," + rec.getVariables().get(var));
		}
		query.append(");");

		return DataBaseManager.makeChange(query.toString(), conx);

	}

	/**
	 * Inserts a record that wasn't evaluated as good in the unreliable table
	 * 
	 * @param rec
	 *            is the record to be insert
	 * @return
	 */
	public int insertUnreliableRecord(Record rec) {

		if (rec.getNote().equals(ShapeFile.E_NULL_COUNTRY)) {
			return DataBaseManager.makeChange("Insert into " + ServerConfig.getInstance().dbTableUnreliable
					+ " values(" + rec.getId() + "," + rec.getNub_concept_id() + "," + rec.getLongitude()
					+ "," + rec.getLatitude() + "," + rec.getIso_country_code() + ",'"
					+ DataBaseManager.correctStringToQuery(getCanonical(rec.getNub_concept_id())) + "','"
					+ rec.getNote() + "');", conx);
		} else {
			return DataBaseManager.makeChange("Insert into " + ServerConfig.getInstance().dbTableUnreliable
					+ "  values(" + rec.getId() + "," + rec.getNub_concept_id() + "," + rec.getLongitude()
					+ "," + rec.getLatitude() + ",'" + rec.getIso_country_code() + "','"
					+ DataBaseManager.correctStringToQuery(getCanonical(rec.getNub_concept_id())) + "','"
					+ rec.getNote() + "');", conx);
		}
	}

	/**
	 * Inserts a record that wasn't evaluated as good in the unreliable table
	 * 
	 * @param rec
	 *            is the record to be insert
	 * @return
	 */
	public int insertUnreliableRecordWithCanonical(Record rec) {

		if (rec.getNote().equals(ShapeFile.E_NULL_COUNTRY)) {
			return DataBaseManager.makeChange("Insert into " + ServerConfig.getInstance().dbTableUnreliable
					+ " values(" + rec.getId() + "," + rec.getNub_concept_id() + "," + rec.getLongitude()
					+ "," + rec.getLatitude() + "," + rec.getIso_country_code() + ",'"
					+ DataBaseManager.correctStringToQuery(rec.getCanonical()) + "','" + rec.getNote()
					+ "');", conx);
		} else {
			return DataBaseManager.makeChange("Insert into " + ServerConfig.getInstance().dbTableUnreliable
					+ "  values(" + rec.getId() + "," + rec.getNub_concept_id() + "," + rec.getLongitude()
					+ "," + rec.getLatitude() + ",'" + rec.getIso_country_code() + "','"
					+ DataBaseManager.correctStringToQuery(rec.getCanonical()) + "','" + rec.getNote()
					+ "');", conx);
		}
	}

	/**
	 * Gets a list of records to work
	 * 
	 * @param numRecords
	 *            to get
	 * @return
	 */
	public synchronized List<Record> getRecords(int numRecords) {
		rs = DataBaseManager.makeQuery("select * from " + ServerConfig.getInstance().dbTableRecords
				+ " where id > " + lastID + " and id not in " + "(select id from "
				+ ServerConfig.getInstance().dbTableGoods + ")" + " and id not in " + "(select id from "
				+ ServerConfig.getInstance().dbTableUnreliable + ")" + " order by id limit " + numRecords,
				conx);

		endRecords = false;
		int contador = 0;
		regs.clear();
		// regs = new LinkedList<Record>();
		try {
			while (contador < numRecords && !endRecords) {
				if (rs.next()) {
					regs.add(new Record(rs.getString("iso_country_code"), rs.getDouble("latitude"), rs
							.getDouble("longitude"), rs.getInt("nub_concept_id"), lastID = rs.getInt("id")));
					contador++;
				} else {
					endRecords = true;
					lastID = 0;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.getStatement().close();
				rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// rs = null;
		// System.gc();
		return regs;
	}

	/**
	 * Gets a list of records to work including country, state and county
	 * information
	 * 
	 * @param numRecords
	 *            to get
	 * @return
	 */
	public synchronized List<Record> getRecordsWithCountyLevelInfo(int numRecords) {
		rs = DataBaseManager.makeQuery("select * from " + ServerConfig.getInstance().dbTableRecords
				+ " where id > " + lastID + " and id not in " + "(select id from "
				+ ServerConfig.getInstance().dbTableGoods + ")" + " and id not in " + "(select id from "
				+ ServerConfig.getInstance().dbTableUnreliable + ")" + " order by id limit " + numRecords,
				conx);

		endRecords = false;
		int contador = 0;
		regs.clear();
		// regs = new LinkedList<Record>();
		try {
			while (contador < numRecords && !endRecords) {
				if (rs.next()) {
					regs.add(new Record(rs.getString("iso_country_code"), rs.getString("Pais"), rs
							.getString("Departamento"), rs.getString("Municipio"), rs.getDouble("latitude"),
							rs.getDouble("longitude"), rs.getInt("nub_concept_id"),
							rs.getString("canonical"), lastID = rs.getInt("id")));
					contador++;
				} else {
					endRecords = true;
					lastID = 0;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.getStatement().close();
				rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// rs = null;
		// System.gc();
		return regs;
	}

	/**
	 * gets the canonical of a taxon concept
	 * 
	 * @param nub_concept_id_BD
	 *            to search the canonical
	 * @return
	 */
	public String getCanonical(int nub_concept_id_BD) {

		ResultSet rs = DataBaseManager.makeQuery(
				"select tn.canonical from taxon_name tn, taxon_concept tc where tc.id= " + nub_concept_id_BD
						+ " and tc.taxon_name_id = tn.id", conx);
		try {
			if (rs.next()) {
				String cadena = rs.getString(1);
				rs.getStatement().close();
				rs.close();
				return cadena;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the ISO code from a country name
	 * 
	 * @param pais
	 *            is the country to search the ISO
	 * @return
	 */
	public String getCountryISO(String pais) {

		ResultSet rs = DataBaseManager.makeQuery("select * from country_name where name= '"
				+ DataBaseManager.correctStringToQuery(pais) + "'", conx);
		try {
			if (rs.next()) {
				String cadena = rs.getString("iso_country_code");
				rs.getStatement().close();
				rs.close();
				return cadena;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

}
