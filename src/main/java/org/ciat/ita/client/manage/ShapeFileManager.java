package org.ciat.ita.client.manage;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ciat.ita.client.ClientConfig;
import org.ciat.ita.model.Record;
import org.ciat.ita.model.ShapeFile;


public class ShapeFileManager {

	/**
	 * @uml.property name="shapefile"
	 * @uml.associationEnd multiplicity="(1 1)" ordering="true"
	 *                     inverse="base:model.ShapeFile"
	 */
	private ShapeFile shapefile;

	private Map<String, String> countriesISO;

	/**
	 * Sets the dictionary of countries and isos for subsequent comparison
	 * 
	 * @param countriesISO
	 */
	public void setCountriesISO(Map<String, String> countriesISO) {
		this.countriesISO = countriesISO;
	}

	public ShapeFileManager() {
		shapefile = new ShapeFile(new File(ClientConfig.getInstance().shapeFile));
	}

	public ShapeFileManager(int level) {
		shapefile = new ShapeFile(new File(ClientConfig.getInstance().shapeFile), level);
	}

	/***
	 * Given a set of coordinates and the name of a country, we evaluate if the
	 * coordinates correspond to that country
	 * 
	 * @param latitude
	 *            in database of record
	 * @param longitude
	 *            in database of record
	 * @param country
	 *            in database of Record
	 * @return null when the coordinates actually correspond to the country;
	 *         NOT_FOUND when the country is not in the shape file;
	 *         WRONG_COUNTRY when the county in database does not match with any
	 *         possible country in the shape file for those coordinates;
	 *         NULL_COUNTRY when there is no country data in the records
	 * @throws RemoteException
	 */
	public String compareCountriesISO(double latRecord, double lngRecord,
			String countryRecord) throws RemoteException {

		if (countryRecord == null || countryRecord.isEmpty()
				|| countryRecord.equals("")) {
			return ShapeFile.E_NULL_COUNTRY;
		} else {
			String isoComparar = "";
			List<String> possibleCountries = shapefile.polygonsForAPoint(
					latRecord, lngRecord);
			if (possibleCountries.size() > 0) {
				Iterator<String> iteraCmpPais = possibleCountries.iterator();
				while (iteraCmpPais.hasNext()) {
					isoComparar = countriesISO.get(iteraCmpPais.next());
					if (countryRecord.equalsIgnoreCase(isoComparar)) {
						possibleCountries = null;
						return null;
					}
				}
				possibleCountries = null;
				/***
				 * if none of the possible countries match with the country in
				 * database, is a wrong country data
				 */
				return ShapeFile.E_WRONG_COUNTRY;
			} else {
				possibleCountries = null;
				return ShapeFile.E_NOT_FOUND_COUNTRY;
			}
		}
	}

	public String compareCountries(double latRecord, double lngRecord,
			String countryRecord) throws RemoteException {

		if (countryRecord == null || countryRecord.isEmpty()
				|| countryRecord.equals("")) {
			return ShapeFile.E_NULL_COUNTRY;
		} else {
			
			List<String> possibleCountries = shapefile.polygonsForAPoint(
					latRecord, lngRecord);
			if (possibleCountries.size() > 0) {
				for (String possibleCountry : possibleCountries) {
					if (countryRecord.equalsIgnoreCase(possibleCountry)) {
						possibleCountries = null;
						return null;
					}else{
						 //System.out.println(countryRecord +" - "+badCodedCountry+" - "+possibleCountry);
					}
				}
				
				possibleCountries = null;
				/***
				 * if none of the possible countries match with the country in
				 * database, is a wrong country data
				 */
				return ShapeFile.E_WRONG_COUNTRY;
			} else {
				possibleCountries = null;
				return ShapeFile.E_NOT_FOUND_COUNTRY;
			}
		}
	}

	/**
	 * Get the countries names according to the records coordinates
	 * 
	 * @param records
	 *            to get the countries names according to the coordinates
	 * @return names of countries for all possibles countries of all these
	 *         records
	 */
	public Set<String> getCountriesFromCoordinates(List<Record> records) {
		Set<String> countries = new LinkedHashSet<String>();
		for (Record rec : records) {
			countries.addAll(shapefile.polygonsForAPoint(rec.getLatitude(), rec
					.getLongitude()));
		}

		return countries;
	}
}
