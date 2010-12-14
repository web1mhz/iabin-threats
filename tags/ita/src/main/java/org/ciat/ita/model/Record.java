package org.ciat.ita.model;

import java.io.Serializable;
import java.util.Map;

import org.ciat.ita.client.manage.DecodeManager;


@SuppressWarnings("serial")
public class Record implements Serializable {

	private String iso_country_code;
	private String country;
	private String state;
	private String county;
	private String locality;
	private double latitude;
	private double longitude;
	private String canonical;
	private int nub_concept_id;
	private int id;
	private String note;
	private Map<String, Short> variables;

	public Record(String isoCountryCode, double latitude, double longitude, int nudConceptId, int id) {
		super();
		this.variables = null;
		this.iso_country_code = isoCountryCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.nub_concept_id = nudConceptId;
		this.id = id;
		this.note = null;
		this.canonical = null;
	}

	public Record(String isoCountryCode, String country, String state, String county, String locality,
			double latitude, double longitude, int nudConceptId, String canonical, int id, boolean decode) {
		super();
		iso_country_code = isoCountryCode;
		this.country = country;
		if (decode) {
			this.state = DecodeManager.decodeString(state);
			this.county = DecodeManager.decodeString(county);
			this.locality = DecodeManager.decodeString(locality);
		} else {
			this.state = state;
			this.county = county;
			this.locality = locality;
		}
		this.latitude = latitude;
		this.longitude = longitude;
		this.nub_concept_id = nudConceptId;
		this.canonical = canonical;
		this.id = id;
	}

	/**
	 * Use the new constructor that use the locality attribute.
	 * 
	 * @deprecated
	 */
	public Record(String isoCountryCode, String country, String state, String county, double latitude,
			double longitude, int nudConceptId, String canonical, int id) {
		super();
		iso_country_code = isoCountryCode;
		this.country = country;
		this.state = state;
		this.county = county;
		this.latitude = latitude;
		this.longitude = longitude;
		this.nub_concept_id = nudConceptId;
		this.canonical = canonical;
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Map<String, Short> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Short> variables) {
		this.variables = variables;
	}

	public int getId() {
		return id;
	}

	public String getIso_country_code() {
		return iso_country_code;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public int getNub_concept_id() {
		return nub_concept_id;
	}

	public String getCanonical() {
		return canonical;
	}

	public void setCanonical(String canonical) {
		this.canonical = canonical;
	}

	public void setIso_country_code(String isoCountryCode) {
		iso_country_code = isoCountryCode;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setNub_concept_id(int nubConceptId) {
		nub_concept_id = nubConceptId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public int hashCode() {
		return id;
	}

	public boolean equals(Object o) {
		return this.hashCode() == o.hashCode();
	}

	public String toString() {
		return id + "," + nub_concept_id + "," + iso_country_code + "," + latitude + "," + longitude;
	}
	


}
