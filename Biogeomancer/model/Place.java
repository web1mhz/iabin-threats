package model;

import java.io.UnsupportedEncodingException;

public class Place {

	private int id;
	private String country;
	private String stateProvince;
	private String county;
	private String locality;
	private double latitude;
	private double longitude;

	public Place(int id, String country, String stateProvince, String county, String locality)
			throws UnsupportedEncodingException {
		super();
		this.id = id;
		this.country = country;
		this.stateProvince = decodeString(stateProvince);
		this.county = decodeString(county);
		this.locality = decodeString(locality);
	}

	private String decodeString(String str) {
		if (str != null && str.length() > 0) {
			String charset = "";
			charset = new CharsetToolkit(str.getBytes()).guessEncoding().name();
			try {
				if (!charset.contains("ASCII")) {
					if (charset.equals("UTF-8")) {
						// System.out.println(charset+" [UTF-8]: " + str +
						// " --> "
						// + new String(str.getBytes("ISO-8859-1"), "UTF-8"));
						return new String(str.getBytes("ISO-8859-1"), "UTF-8");
					} else if (str.contains("?")) {
						String newStr = new String(str.getBytes("ISO-8859-1"), "UTF-8");
						if (countSimbols(newStr, '?') < countSimbols(str, '?')) {
							// System.out.println("CHANGED! [Cont. (?)]: " + str
							// + " --> " + newStr);
							return newStr;
						} /*
						 * else {
						 * System.out.println("NOT CHANGED! [Cont. (?)]: " + str
						 * + " --> " + newStr); }
						 */
					} else if (str.contains("¤")) {
						String newStr = new String(str.getBytes("ISO-8859-1"), "UTF-8");
						if (countSimbols(newStr, '¤') < countSimbols(str, '¤')) {
							// System.out.println(charset+" [Cont. (¤)]: " + str
							// + " --> " + newStr);
							return newStr;
						}
					}
				} // else System.out.println(charset+" [is Ok!]: " + str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str;
	}

	private void decodingProcess() {
		String str = " ";
		if (country != null)
			str += country;
		if (stateProvince != null)
			str += stateProvince;
		if (county != null)
			str += county;
		if (locality != null)
			str += locality;

		// Guess the encoding type of the place. Is not 100% reliable.
		String charset = new CharsetToolkit(str.getBytes()).guessEncoding().name();
		try {
			if (!charset.equals("US-ASCII")) {
				if (charset.equals("UTF-8")) {
					country = country == null ? null : new String(country.getBytes("ISO-8859-1"), "UTF-8");
					stateProvince = stateProvince == null ? null : new String(stateProvince
							.getBytes("ISO-8859-1"), "UTF-8");
					county = county == null ? null : new String(county.getBytes("ISO-8859-1"), "UTF-8");
					locality = locality == null ? null : new String(locality.getBytes("ISO-8859-1"), "UTF-8");
				} else if (str.contains("" + (char) 150)) {
					int n = countSimbols(str, (char) 150);
					String countryTemp = country == null ? null : new String(country.getBytes("ISO-8859-1"),
							"UTF-8");
					String stateProvinceTemp = stateProvince == null ? null : new String(stateProvince
							.getBytes("ISO-8859-1"), "UTF-8");
					String countyTemp = county == null ? null : new String(county.getBytes("ISO-8859-1"),
							"UTF-8");
					String localityTemp = locality == null ? null : new String(locality
							.getBytes("ISO-8859-1"), "UTF-8");
					if (countSimbols(countryTemp + stateProvinceTemp + countyTemp + localityTemp, (char) 150) < n) {
						country = countryTemp;
						stateProvince = stateProvinceTemp;
						county = countyTemp;
						locality = localityTemp;
					}
				}
				System.out.println(this);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Count how many simbols exist in a specific string.
	 * 
	 * @param str
	 *            - the specified string
	 * @return the number of simbols.
	 */
	private int countSimbols(String str, char simbol) {
		int n = 0;
		for (char c : str.toCharArray()) {
			if (c == simbol)
				n++;
		}
		return n;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		String s = country + stateProvince + county + locality;
		String o = "[" + new CharsetToolkit(s.getBytes()).guessEncoding().name() + "] ";
		return o + "Place [" + id + " - country=" + country + ", stateProvince=" + stateProvince
				+ ", county=" + county + ", locality=" + locality + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getCountry() {
		return country;
	}

	public String getCounty() {
		return county;
	}

	public String getLocality() {
		return locality;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	@Override
	public boolean equals(Object obj) {
		return obj.hashCode() == this.hashCode();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
