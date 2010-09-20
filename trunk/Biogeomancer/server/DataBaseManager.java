package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {

	
	/**
	 * initialize database driver
	 * @return false if the driver was not found
	 */
	public static boolean registerDriver() {
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		}
		return true;
	}

	/**
	 * open the connection to the database
	 * @param user
	 * @param password
	 * @return
	 */
	public static Connection openConnection(String user, String password) {
		try {
			Connection conexion = DriverManager.getConnection("jdbc:mysql://"
					+ ServerConfig.dbIPAddress + ":"
					+ ServerConfig.database_port + "/"
					+ ServerConfig.database_name, user, password);
			return conexion;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean closeConnection(Connection conexion) {
		try {
			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * This method make a change in the database. This method has to start with
	 * the word UPDATE or INSERT. If you want to make a query, u should use the
	 * method makeQuery.
	 * 
	 * @param updateQuery
	 *            where is the SQL code to make an insert or an update. NOT a
	 *            select.
	 * @param conexion
	 *            . The object Connection.
	 * @return The number of rows that changed, or -1 in case an error ocurrs.
	 */
	public static int makeChange(String updateQuery, Connection conexion) {
		Statement stmMakeChange;
		try {
			if (updateQuery.toLowerCase().startsWith("update")
					|| updateQuery.toLowerCase().startsWith("insert")) {
				
				//stmMakeChange = conexion.prepareStatement(updateQuery);
				//int v = stmMakeChange.executeUpdate();
				stmMakeChange = conexion.createStatement();
				int v = stmMakeChange.executeUpdate(updateQuery);
				stmMakeChange.close();
				//stmMakeChange.close();
				//stmMakeChange = null;
				return v;
			}
		} catch (Exception e) {

			return -1;

		}
		return -1;
	}

	/**
	 * This method execute a query. The query string should start with the word
	 * SELECT.
	 * 
	 * @param query
	 *            where is the SQL code to take data from the database.
	 * @param conexion
	 *            . The object Connection.
	 * @return ResulSet object that correspond to the query result. Or null if
	 *         there was an error.
	 */
	public static ResultSet makeQuery(String query, Connection conexion) {
		//PreparedStatement stmMakeQuery;
		try {
			if (query.toLowerCase().startsWith("select")) {
				//stmMakeQuery = conexion.prepareStatement(query);
				//return  stmMakeQuery.executeQuery();
				return conexion.createStatement().executeQuery(query); 
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	public static String correctStringToQuery(String cadena) {
		return cadena.replaceAll("'", "\\\\'");
	}
}
