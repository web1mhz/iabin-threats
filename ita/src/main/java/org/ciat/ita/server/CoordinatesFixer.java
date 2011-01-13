package org.ciat.ita.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.ciat.ita.client.manage.ShapeFileManager;
import org.ciat.ita.server.database.DataBaseManager;

import com.vividsolutions.jts.geom.Coordinate;

public class CoordinatesFixer {

	private static Connection conx;
	private static ResultSet rs;

	public static void main(String[] args) {

		try {
			DataBaseManager.registerDriver();
			conx = DataBaseManager.openConnection(
					ServerConfig.getInstance().database_user, ServerConfig
							.getInstance().database_password);
			fixCoordinates(conx);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void fixCoordinates() throws SQLException {
		ShapeFileManager sfman = new ShapeFileManager();

		rs = DataBaseManager
				.makeQuery(
						"select id, I3N_latitude, I3N_longitude, iso_country_code from I3N_occurrence_record",
						conx);

		Map<Integer, Coordinate> fixed = new HashMap<Integer, Coordinate>();

		double lat = 0;
		double lon = 0;
		int maxDistance = 3;
		String i3nlat = null;
		String i3nlon = null;
		String iso = null;
		boolean fixable = true;
		int analized = 0;
		int vacio = 0;
		while (rs.next()) {
			analized++;
			i3nlat = rs.getString("I3N_latitude");
			i3nlon = rs.getString("I3N_longitude");
			iso = rs.getString("iso_country_code");
			lat = 0;
			lon = 0;
			fixable = true;

			if (i3nlon != null
					&& i3nlat != null
					&& (i3nlat.contains("W") || i3nlat.contains("w")
							|| i3nlat.contains("E") || i3nlat.contains("e")
							|| i3nlat.contains("O") || i3nlat.contains("X")
							|| i3nlon.contains("Y") || i3nlon.contains("S")
							|| i3nlon.contains("s") || i3nlon.contains("N") || i3nlon
							.contains("n"))) {
				String temp = i3nlat;
				i3nlat = i3nlon;
				i3nlon = temp;
			}

			if (i3nlon != null && !i3nlon.isEmpty()) {
				i3nlon = i3nlon.trim();
				i3nlon = i3nlon.replace(":", "");
				i3nlon = i3nlon.replace("X", "");
				i3nlon = i3nlon.replace(",", ".");
				try {
					lon = Double.parseDouble(i3nlon);
				} catch (NumberFormatException e) {
					try {
						lon = fixLongitude(i3nlon);
					} catch (Exception e1) {
						// System.out.println(i3nlon + " can't fix");
						lon = 0;
					}
				}
			}

			if (i3nlat != null && !i3nlat.isEmpty()) {
				i3nlat = i3nlat.trim();
				i3nlat = i3nlat.replace("Y", "");
				i3nlat = i3nlat.replace(":", "");
				i3nlat = i3nlat.replace(",", ".");
				try {
					lat = Double.parseDouble(i3nlat);
				} catch (NumberFormatException e) {
					try {
						lat = fixLatitude(i3nlat);
					} catch (Exception e1) {
						// System.out.println(i3nlat + " can't fix");
						lat = 0;
					}
				}
			}

			while (lon > 180 || lon < -180) {
				lon /= 10;
			}
			while (lat > 90 || lat < -90) {
				lat /= 10;
			}

			// shape corrector:

			if (sfman.compareISO(lat, lon, iso) != null) {
				// changing longitude hemisphere
				if (sfman.compareISO(lat, -lon, iso, maxDistance) == null) {
					lon *= -1;
				} else {
					// changing both hemispheres
					if (sfman.compareISO(-lat, -lon, iso, maxDistance) == null) {
						lat *= -1;
						lon *= -1;
					} else {
					// try changing coordinates
					if (sfman.compareISO(lon, lat, iso, maxDistance) == null) {
						double temp = lon;
						lon = lat;
						lat = temp;
					} else {

						// try changing coordinates and orientation
						if (sfman.compareISO(-lon, -lat, iso, maxDistance) == null) {
							double temp = -lon;
							lon = -lat;
							lat = temp;
						} else {
							// try changing coordinates and orientation, and
							// reducing latitude
							if (sfman.compareISO(-lon, -(lat / 10), iso,
									maxDistance) == null) {
								double temp = -lon;
								lon = -(lat / 10);
								lat = temp;
							}
						}
					}}
				}
			}

			if (lat == 0 || lon == 0) {
				fixable = false;
			}

			if (fixable && sfman.compareISO(lat, lon, iso, maxDistance) != null) {
				fixable = false;
			}

			try {
				FileWriter fgoods = new FileWriter(new File(
						"I3N_good_coords.txt"), true);
				FileWriter fbads = new FileWriter(
						new File("I3N_bad_coords.txt"), true);

				if (fixable) {
					fixed.put(rs.getInt("id"), new Coordinate(lon, lat));

					fgoods.write(i3nlat + "\t" + lat + "\t" + i3nlon + "\t"
							+ lon + "\n");
				} else {
					if (i3nlon != null && !i3nlon.isEmpty() && i3nlat != null
							&& !i3nlat.isEmpty()) {

						fbads.write(i3nlat + "\t" + lat + "\t" + i3nlon + "\t"
								+ lon + "\n");
					} else {
						vacio++;
					}
				}

				fgoods.flush();
				fgoods.close();
				fbads.flush();
				fbads.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		System.out.println("Analized:" + analized + " Acepted_Fixed:"
				+ fixed.size() + " Null_Empty:" + vacio + " Not Fixed:"
				+ (analized - vacio - fixed.size()));
		rs.getStatement().close();
		rs.close();
		rs = null;

		// update coordinates
		for (Integer id : fixed.keySet()) {
			DataBaseManager.makeChange(
					"UPDATE I3N_occurrence_record set latitude="
							+ fixed.get(id).y + " , longitude="
							+ fixed.get(id).x + " where id='" + id + "'", conx);
		}

	}

	private static double fixLatitude(String i3nlat) throws Exception {

		byte dir = 1;
		if (i3nlat.contains("S") || i3nlat.contains("s")) {
			dir = -1;
			i3nlat = i3nlat.replace("S", "");
			i3nlat = i3nlat.replace("s", "");
		}
		if (i3nlat.contains("N") || i3nlat.contains("n")) {
			i3nlat = i3nlat.replace("N", "");
			i3nlat = i3nlat.replace("n", "");
			if (dir == -1) {
				throw new Exception("Can't determinate if \"" + i3nlat
						+ "\" is in the South or in the North");
			}
		}
		i3nlat = i3nlat.trim();
		return degreesToDecimal(i3nlat) * dir;

	}

	private static double fixLongitude(String i3nlon) throws Exception {

		byte dir = 1;
		if (i3nlon.contains("W") || i3nlon.contains("w")
				|| i3nlon.contains("O")) {
			dir = -1;
			i3nlon = i3nlon.replace("W", "");
			i3nlon = i3nlon.replace("w", "");
			i3nlon = i3nlon.replace("O", "");
		}
		if (i3nlon.contains("E") || i3nlon.contains("e")) {
			i3nlon = i3nlon.replace("E", "");
			i3nlon = i3nlon.replace("e", "");
			if (dir == -1) {
				throw new Exception("Can't determinate if \"" + i3nlon
						+ "\" is in the East or in the West");
			}
		}
		i3nlon = i3nlon.trim();
		return degreesToDecimal(i3nlon) * dir;

	}

	private static double degreesToDecimal(String val) {
		val = val.trim();
		val = val.replace("â", "");
		val = val.replace("â", "");
		val = val.replace("™", "");
		val = val.replace("€", "'");
		String degreeValue = "0";
		String minutesValue = "0";
		String secondsValue = "0";

		if (val.contains("°") || val.contains("º") || val.contains("o")) {
			String[] degreeSplit = val.split("[°ºo]");

			if (degreeSplit.length > 0) {
				String degree = degreeSplit[0];
				for (int i = 0; i < degree.length(); i++) {
					if (Character.isDigit(degree.charAt(i))) {
						degreeValue = degreeValue + degree.charAt(i);
					}
				}

				if (degreeSplit.length > 1) {
					String[] minutesSplit = degreeSplit[1].split("['´]");

					if (minutesSplit.length > 0) {
						String minutes = minutesSplit[0];
						for (int i = 0; i < minutes.length(); i++) {
							if (Character.isDigit(minutes.charAt(i))) {
								minutesValue = minutesValue + minutes.charAt(i);
							}
						}

						if (minutesSplit.length > 1) {

							String[] secondsSplit = minutesSplit[1]
									.split("[\"]");
							if (secondsSplit.length > 0) {

								String seconds = secondsSplit[0];
								for (int i = 0; i < seconds.length(); i++) {
									if (Character.isDigit(seconds.charAt(i))) {
										secondsValue = secondsValue
												+ seconds.charAt(i);
									}
								}

							}

						}
					}
				}
			}
		} else {
			String[] dmsValue = val.split("[. ]");
			if (dmsValue.length > 2) {
				degreeValue = dmsValue[0];
				minutesValue = dmsValue[1];
				secondsValue = dmsValue[2];
			} else {
				if (dmsValue.length > 1) {
					return Double.parseDouble(dmsValue[0] + "." + dmsValue[2]);
				}
			}
		}

		return (Double.parseDouble(degreeValue)
				+ (Double.parseDouble(minutesValue) / 60.0) + (Double
				.parseDouble(secondsValue) / 3600.0));
	}

	public static void fixCoordinates(Connection connection)
			throws SQLException, IOException {
		conx = connection;
		fixCoordinates();
		DataBaseManager.closeConnection(conx);

	}

}
