package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import model.Place;

import org.biogeomancer.records.Georef;
import org.biogeomancer.records.Rec;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import util.BgUtil;

public class BgClient {
	
	
	public static String sampleData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
			+ "<biogeomancer xmlns=\"http://bg.berkeley.edu\" xmlns:dwcore=\"http://rs.tdwg.org/dwc/dwcore\" xmlns:dwgeo=\"http://rs.tdwg.org/dwc/dwgeo\">"
			+ "<request type=\"batch\" interpreter=\"yale\" header=\"true\">"
			+ "<record>"
			+ "<dwcore:Locality>Berkeley</dwcore:Locality>"
			+ "<dwcore:StateProvince>California</dwcore:StateProvince>"
			+ "</record>"
			+ "<record>"
			+ "<dwcore:Locality>Stuttgart</dwcore:Locality>"
			+ "<dwcore:Country>Germany</dwcore:Country>"
			+ "</record>"
			+ "<record>"
			+ "<dwcore:Locality>3 mi E Lolo</dwcore:Locality>"
			+ "<dwcore:County>Missoula</dwcore:County>"
			+ "</record>"
			+ "</request>" + "</biogeomancer>";

	public static String dataToXML(Set<Place> places) {
		String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<biogeomancer xmlns=\"http://bg.berkeley.edu\" xmlns:dwcore=\"http://rs.tdwg.org/dwc/dwcore\" xmlns:dwgeo=\"http://rs.tdwg.org/dwc/dwgeo\">"
				+ "<request type=\"batch\" interpreter=\"yale\" header=\"true\">";
		for (Place p : places) {
			data += "<record> <dwcore:Country>" + p.getCountry()
					+ "</dwcore:Country>" + "<dwcore:StateProvince>"
					+ p.getStateProvince() + "</dwcore:StateProvince>"
					+ "<dwcore:County>" + p.getCounty() + "</dwcore:County>"
					+ "<dwcore:Locality>" + p.getLocality()
					+ "</dwcore:Locality>" + "</record>";
		}
		data += "</request>" + "</biogeomancer>";
		return data;
	}

	public static void main(String[] argv) throws MalformedURLException,
			IOException {
		BgClient bgClient = new BgClient();
		// String serviceUrl = "http://localhost:8080/ws-test/batch";
		String serviceUrl = "http://bg.berkeley.edu:8080/ws-test/batch";

		URL connectUrl = new URL(serviceUrl);
		bgClient.setServiceUrl(connectUrl, sampleData);
	}

	/**
	 * Writes the content of the input stream to a <code>String<code>.
	 */

	private static String toString(InputStream inputStream) throws IOException {
		String string;
		StringBuilder outputBuilder = new StringBuilder();
		if (inputStream != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			while (null != (string = reader.readLine())) {
				outputBuilder.append(string).append('\n');
			}
		}
		return outputBuilder.toString();
	}

	public List<Georef> georeference(Rec r, String interpreter) {
		return BgUtil.georeference(r, interpreter);
	}

	/**
	 * @param URL
	 *            service URL connect to URL: set URL Method to POST and write
	 *            batch interface for doPost to read the interface is in String
	 *            data (class static variable) write the xml text to a file,
	 *            default file name is autoGenerate.xml store in current working
	 *            directory
	 */
	public void setServiceUrl(URL serviceUrl, String XMLdata) {
		
		HttpURLConnection connection;
		try {

			// START PROXY CODE CONFIGURATION IN CIAT PLACE
			System.getProperties().put("proxySet", "true");
			System.getProperties().put("proxyHost", "proxy4.ciat.cgiar.org");
			System.getProperties().put("proxyPort", "8080");
			// END PROXY CODE CONFIGURATION

			connection = (HttpURLConnection) serviceUrl.openConnection();
			// Proxy proxyCIAT = new Proxy(Type.HTTP, );
			// serviceUrl.openConnection()
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			// Post the data item
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(XMLdata.getBytes());
			outputStream.flush();
			outputStream.close();

			// Retrieve the output
			int responseCode = connection.getResponseCode();
			InputStream inputStream;
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
			} else {
				inputStream = connection.getErrorStream();
			}

			// Working with XML
			// try {
			// SAXBuilder builder = new SAXBuilder(false);
			// Document doc = builder.build(inputStream);
			// Element httpXML = doc.getRootElement();
			// Element records = httpXML.getChild("records");
			//				
			// List<Element> recordsArray = records.getChildren("record");
			// System.out.println(recordsArray.size());
			//				
			// } catch (JDOMException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// write the output to the console
			String xmlText = toString(inputStream);
			System.out.println(xmlText);
			BgUtil.recordToFile("autoGenerate.xml", xmlText);
			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
