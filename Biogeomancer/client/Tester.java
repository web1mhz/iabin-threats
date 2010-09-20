package client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import model.Place;
import server.DataBaseManager;
import server.ServerConfig;

public class Tester {

	Connection conx;

	public static void main(String[] args) {
		Tester t = new Tester();
		try {
			t.start();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void start() throws MalformedURLException, IOException {
		Set<Place> testcases = getTestCases();
		System.out.println("--------------");
		System.out.println("Testing " + testcases.size() + " locations");
		/*
		 * for (Place p : testcases) { System.out.println(p); }
		 */
		// BgClient bgClient = new BgClient();
		// -- String serviceUrl = "http://localhost:8080/ws-test/batch";

		// URL connectUrl = new
		// URL("http://bg.berkeley.edu:8080/ws-test/batch");
		// bgClient.setServiceUrl(connectUrl, BgClient.dataToXML(testcases));

	}

	public Tester() {
		ServerConfig.init();
		DataBaseManager.registerDriver();
		conx = DataBaseManager.openConnection(ServerConfig.database_user,
				ServerConfig.database_password);

	}

	public Set<Place> getTestCases() {
		ResultSet rs = DataBaseManager
				.makeQuery(
						"select  b.id, r.country, r.state_province, r.county, r.locality "
								+ "from raw_occurrence_record r, temp_bad_records b "
								+ "where b.id=r.id and b.error='WC' and r.country='SWEDEN' "
								+ "limit 10000", conx); // and r.country='SWEDEN'
		Set<Place> query = new HashSet<Place>();

		try {
			while (rs.next()) {
				// Evaluating the encoding type of the place and adding it to
				// the query Set.
				// query.add(new Place(rs.getString("country"), rs
				// .getString("state_province"), rs.getString("county"),
				// rs.getString("locality")));
				new Place(rs.getInt(1), rs.getString("country"), rs
						.getString("state_province"), rs.getString("county"),
						rs.getString("locality"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.getStatement().close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return query;
	}
}
