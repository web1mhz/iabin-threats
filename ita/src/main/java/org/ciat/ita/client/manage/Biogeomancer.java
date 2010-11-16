package org.ciat.ita.client.manage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ciat.ita.model.Record;
import org.ciat.ita.server.database.PortalInterface;

public class Biogeomancer {

	//private static final String BgServer = "http://bg.berkeley.edu:8080/ws-test/batch";
	private static final String BgServer = "http://bg.berkeley.edu/ws/batch";

	/** 
	 * @param places - A Set of places (Records) that have not been georreferenced.
	 * @return The same Set of places but re-georreferenced.
	 */
	public static Set<Record> startGeorref(Set<Record> places) {
		try {
			String xmlData = dataToXML(places);
			System.out.println(xmlData);
			
			// enviando a servidor biogeomancer y obteniendo respuesta.
			URL urlBgServer;
			urlBgServer = new URL(BgServer);

			String xmlResult = setServiceUrl(urlBgServer, xmlData);

			/*
			 * TODO: Interpretar dicho String (xmlResult) como un xml y extraer
			 * cada uno de los valores de latitud y longitud para ser ingresadas
			 * en el objeto Record de la colección.
			 * 
			 * Completar el codigo a continuacion:
			 */

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

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		return places;
	}

	/**
	 * @param places - A Set of places (Records) that have not been georreferenced.
	 * @return A XML-String formatted with the parameters needed for the
	 *         Biogeomancer Server to the understanding of the information.
	 */
	private static String dataToXML(Set<Record> places) {
		System.out.println("private static String dataToXML(Set<Record> places) {");
		//List<Record> listado= PortalInterface.getInstance().getWork("clientname", 1); 
		StringBuffer data = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"utf-8\"?><biogeomancer xmlns=\"http://bg.berkeley.edu\" xmlns:dwc=\"http://rs.tdwg.org/dwc/terms\" ><request type=\"batch\" interpreter=\"yale\" header=\"true\">");
		for (Record p : places) {
			data.append("<record> <dwc:country>");
			data.append(p.getCountry());
			data.append("</dwc:country>");
			data.append("<dwc:stateProvince>");
			data.append(p.getState());
			data.append("</dwc:stateProvince><dwc:county>");
			data.append(p.getCounty());
			data.append("</dwc:county><dwc:locality>");			
			data.append(p.getLocality());
			data.append("</dwc:locality></record>");
		}
		data.append("</request>" + "</biogeomancer>");
		return data.toString();
	}

	/**
	 * @param URL
	 *            service URL connect to URL: set URL Method to POST and write
	 *            batch interface for doPost to read the interface is in String
	 *            data (class static variable) write the xml text to a file,
	 *            default file name is autoGenerate.xml store in current working
	 *            directory
	 */
	private static String setServiceUrl(URL serviceUrl, String XMLdata) {

		System.out.println("inicia setServiceUrl");
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

			String xmlText = null;

			// Retrieve the output
			int responseCode = connection.getResponseCode();
			InputStream inputStream;
			System.out.println("response code: "+responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
				xmlText = toString(inputStream);
			} else {
				// TODO: Manejar el error en caso de que exista algún problema.
				// (retornar null?).
				inputStream = connection.getErrorStream();
			}

			// write the output to the console
			System.out.println("esto se recibe del servidor : \n");
			
			 System.out.println(xmlText);
			// BgManager.recordToFile("autoGenerate.xml", xmlText);
			connection.disconnect();
			return xmlText;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Writes the content of the input stream to a <code>String<code>.
	 */
	private static String toString(InputStream inputStream) throws IOException {
		String string;
		StringBuilder outputBuilder = new StringBuilder();
		if (inputStream != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			while (null != (string = reader.readLine())) {
				outputBuilder.append(string).append('\n');
			}
		}
		return outputBuilder.toString();
	}
	
	public static void main(String[] args) {
		Record rec=new Record("", "", "California", "", "Berkeley", 0.0, 0.0, 0, null, 0, true);
		HashSet<Record> grup=new HashSet<Record>();
		grup.add(rec);
		System.out.println("inicia startGeorref");
		startGeorref(grup);
		System.out.println("termina startGeorref");
		
		
	}
}
