package org.ciat.ita.bg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.ciat.ita.bg.database.DataBaseManager;
import org.ciat.ita.bg.model.Record;
import org.ciat.ita.bg.server.ServerConfig;
import com.vividsolutions.jts.geom.Point;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;

public class Biogeomancer {

	// private static final String BgServer = "http://bg.berkeley.edu:8080/ws-test/batch";
	private static final String BgServer = "http://bg.berkeley.edu/ws/batch";

	// http://bg.berkeley.edu:8080/ws/batch

	/**
	 * @param places
	 *            - A Set of places (Records) that have not been geo-referenced.
	 * @return The same Set of places but re-geo-referenced.
	 * @throws IOException
	 * @throws JDOMException
	 * @throws DocumentException
	 * @throws JaxenException
	 */
	public static Set<Record> startGeorref(Set<Record> places, Connection conx) throws IOException, DocumentException, JaxenException {

		// escribe archivo de estadisticas de tiempo y cantidad de datos referenciados
		String outputFile = "out.txt";
		String horaInicio = getDateTime();
		String str;

		File f = new File(outputFile);
		FileOutputStream fop = new FileOutputStream(f, true);

		if (f.exists()) {
			str = "\n\n*****************\nstart time : " + getDateTime();
			fop.write(str.getBytes());

			fop.flush();
			fop.close();
		}

		try {
			String xmlData = dataToXML(places);
			System.out.println("despues de datatoxml :" + xmlData);

			// escribiendo el archivo de consulta en disco
			String outputFile2 = "xmlData.txt";

			File f2 = new File(outputFile2);
			FileOutputStream fop2 = new FileOutputStream(f2, true);

			if (f.exists()) {
				fop2.write(xmlData.getBytes());
				fop2.flush();
				fop2.close();
			}

			// enviando a servidor biogeomancer y obteniendo respuesta.
			URL urlBgServer;
			urlBgServer = new URL(BgServer);

			// sigue solicitando informacion al servidor hasta que devuelva
			// diferente de null
			String xmlResult = setServiceUrl(urlBgServer, xmlData);
			int intentos = 0;
			String horaInicioAtaqueDos = getDateTime();
			while (xmlResult == null && intentos < 20) { // prueba 1 vez con el server
				System.out.println("\ntiempo : " + getDateTime());
				System.out.println("\nnumero de intentos de conexion: " + intentos);
				xmlResult = setServiceUrl(urlBgServer, xmlData);
				intentos++;
			}
			System.out.println("empezo a las " + horaInicioAtaqueDos);
			System.out.println("termino a las " + getDateTime());

			/*
			 * TODO: Interpretar dicho String (xmlResult) como un xml y extraer
			 * cada uno de los valores de latitud y longitud para ser ingresadas
			 * en el objeto Record de la coleccion.
			 * 
			 * Completar el codigo a continuacion:
			 */
			System.out.println("va a leer los tags xml");
			// Working with XML

			// System.out.println("xmlresult inputstream: " + xmlResult);

			// se selecciona la fuente desde la que se obtiene el archivo xml de BG
			Document document = null;
			if (xmlResult != null) {
				document = org.dom4j.DocumentHelper.parseText(xmlResult);
				System.out.println("se leyo el xml del servidor ********");
			}/*else{
				System.out.println("se requiere el archivo 100Records.xml para ser analizado");
				
				document=getDocument("100Records.xml");
				}*/

			System.out.println("*******************************************");

			System.out.println("*****************shows xml values**************************");

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("dwc", "http://rs.tdwg.org/dwc/terms/");

			XPath xpath = DocumentHelper.createXPath("//record");
			xpath.setNamespaceContext(new SimpleNamespaceContext(map));

			List nodes = xpath.selectNodes(document);
			// ***********
			String values = "";
			String[] data;
			String longitude;
			String latitude;
			String uncertainty;
			Double minorUncertainty = 0.0;
			String minorLongitude = "";
			String minorLatitude = "";

			int sinrespuesta = 0, unasolarespuesta = 0, dosrespuestas = 0, masde2respuestas = 0;

			Iterator<Record> it = places.iterator();
			// it.next();
			Record reco = null;
			int idrec;

			// *************
			for (int i = 0; i < nodes.size(); i++) {
				// System.out.println("contador :"+i);
				// System.out.println("\n" + ((Node) nodes.get(i)).getName() + " numero " + i);
				// System.out.println("getText"+((Node)nodes.get(i)).getText()
				// );
				System.out.println("getStringValue" + ((Node) nodes.get(i)).getStringValue());
				values = ((Node) nodes.get(i)).getStringValue();

				data = values.split("\n");
				int x = data.length;
				System.out.println("largo del arreglo split :" + x);

				if (x == 6) {
					sinrespuesta++;
					minorUncertainty = -1.0;
					minorLongitude = "";
					minorLatitude = "";
				}
				if (x == 12) {
					unasolarespuesta++;
					uncertainty = data[data.length - 1];
					latitude = data[data.length - 4];
					longitude = data[data.length - 3];

					// System.out.println("latitude : " + latitude);
					// System.out.println("longitude : " + longitude);
					// System.out.println("uncertainty : " + uncertainty);
				}
				if (x == 13) {
					unasolarespuesta++;
					uncertainty = data[data.length - 1];
					latitude = data[data.length - 4];
					longitude = data[data.length - 3];

					// System.out.println("latitude : " + latitude);
					// System.out.println("longitude : " + longitude);
					// System.out.println("uncertainty : " + uncertainty);
				}
				if (x == 19) {
					dosrespuestas++;
					uncertainty = data[data.length - 1];
					latitude = data[data.length - 4];
					longitude = data[data.length - 3];

					// System.out.println("latitude : " + latitude);
					// System.out.println("longitude : " + longitude);
					// System.out.println("uncertainty : " + uncertainty);

					uncertainty = data[data.length - 8];
					latitude = data[data.length - 11];
					longitude = data[data.length - 10];

					// System.out.println("latitude : " + latitude);
					// System.out.println("longitude : " + longitude);
					// System.out.println("uncertainty : " + uncertainty);
				}
				if (x > 19)
					masde2respuestas++;

				int z = x;
				// muestra cada valor del record en el arreglo despues del split
				/*	for (int y = 0; y < x; y++) {
						System.out.println("valor " + y + " : " + data[y]);

					}*/
				// se halla el punto con menor uncertainty
				int j = 11;

				if (x > 11) {
					minorUncertainty = Double.parseDouble(data[data.length-1]);
					minorLatitude = data[data.length-4];
					minorLongitude = data[data.length-3];
				}
				while (j < x) {

					// System.out.println("minor uncertainty :" + minorUncertainty);
					if (Double.parseDouble(data[data.length-1]) < minorUncertainty) {
						minorUncertainty = Double.parseDouble(data[data.length-1]);
						minorLatitude = data[data.length-4];
						minorLongitude = data[data.length-3];

					}
					j = j + 7;
				}
				System.out.println("the minor uncertainty is : " + minorUncertainty);
				System.out.println("minor Latitude is : " + minorLatitude);
				System.out.println("minor Longitude is : " + minorLongitude);

				// escribir los resultados en archivo csv
				Boolean da = writeFile(minorLatitude, minorLongitude, minorUncertainty, "records.csv");
				System.out.println("escribio el resultado " + da);

				System.out.println("largo del arreglo split :" + x);

				// escribir las coordenadas en los campos Blatitud , Blongitud, uncertainty and is_fixed
				// is_fixed=1 se georreferencio correctamente
				// is_fixed=2 Biogeomancer no pudo referenciar el registro

				if (it.hasNext()) {
					reco = it.next();
					idrec = reco.getId();
					System.out.println("id's editadas " + idrec);
					/*consulta("update temp_georeferenced_records set blatitude="+minorLatitude
							+", blongitude="+minorLongitude+", uncertainty="
							+minorUncertainty+", is_fixed=1 where id="+idrec+" ;" );*/
					if (minorLatitude != "" && minorLongitude != "") {
						consulta("update georeferenced_records set blatitude=" + minorLatitude + ", blongitude=" + minorLongitude + ", uncertainty=" + minorUncertainty + ", is_fixed=1 where id="
								+ idrec + " ;", conx);
					} else {
						consulta("update georeferenced_records set is_fixed=2 where id=" + idrec + " ;", conx);
					}

				} else {
					System.out.println("no hay records en el resultset places");
				}
			}

			str = "\nstart time" + getDateTime() + "\nlos registros que no tienen respuesta son :" + sinrespuesta + "\nlos registros que tienen 1 respuesta  son :" + unasolarespuesta
					+ "\nlos registros que tienen 2 respuestas son :" + dosrespuestas + "\nlos registros que tienen >2 respuestas son :" + masde2respuestas + "\n el total de los registros es :"
					+ nodes.size() + "\nstart time : " + horaInicio + "\nend time : " + getDateTime();

			System.out.println(str);
			/*		System.out.println("los registros que no tienen respuesta son :"
							+ sinrespuesta);
					System.out.println("los registros que tienen 1 respuesta  son :"
							+ unasolarespuesta);
					System.out.println("los registros que tienen 2 respuestas son :"
							+ dosrespuestas);
					System.out.println("los registros que tienen >2 respuestas son :"
							+ masde2respuestas);
					System.out.println("el total de los registros es :" + nodes.size());*/

			// writes the timing and results to d:/out.txt
			fop = new FileOutputStream(f, true);

			if (f.exists()) {

				fop.write(str.getBytes());

				fop.flush();
				fop.close();
			}

			System.out.println("*******************************************");

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		return places;
	}

	/**
	 * @param places
	 *            - A Set of places (Records) that have not been georreferenced.
	 * @return A XML-String formatted with the parameters needed for the Biogeomancer Server to the understanding of the information.
	 */
	private static String dataToXML(Set<Record> places) {
		System.out.println("-------------------------------------------dataToXML(Set<Record> places) {");

		StringBuffer data = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"utf-8\"?><biogeomancer xmlns=\"http://bg.berkeley.edu\" xmlns:dwc=\"http://rs.tdwg.org/dwc/terms\" ><request type=\"batch\" interpreter=\"yale\" header=\"true\">");
		for (Record p : places) {
			data.append("<record> <dwc:country>");
			data.append(p.getCountry());
			// System.out.println("pais ---------------------> : "+
			// p.getCountry());
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
	 *            service URL connect to URL: set URL Method to POST and write batch interface for doPost to read the interface is in String data (class static variable) write the xml text to a file,
	 *            default file name is autoGenerate.xml store in current working directory
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
			System.out.println("response code: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
				xmlText = toString(inputStream);

				// aqui se escribe la informacion recibida del servidor de BG en
				// un archivo xml
				File f = new File("outputBiogeomancer.xml");
				FileOutputStream fop = new FileOutputStream(f, true);
				String texto;

				if (f.exists()) {
					texto = xmlText;
					fop.write(texto.getBytes());

					fop.flush();
					fop.close();
				}

			} else {
				// TODO: Manejar el error en caso de que exista algun problema.
				// (retornar null?).
				if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
					// inputStream = connection.getErrorStream();
					System.out.println("HTTP Status-Code 500: Internal Server Error.");

				} else {
					System.out.println("other error " + responseCode);
				}
			}

			// write the output to the console
			System.out.println("esto se recibe del servidor : \n");

			// System.out.println("setserviceurl :" + xmlText);
			System.out.println("setserviceurl :");
			// BgManager.recordToFile("autoGenerate.xml", xmlText);
			connection.disconnect();
			// return xmlText;
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

	/**
	 * the sql consult is performed, then every record is stored into a hashset, then is called the method startGeorref.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		DataBaseManager.registerDriver();

		boolean bandera = true;
		Connection conx;
		conx = DataBaseManager.openConnection(ServerConfig.getInstance().database_user, ServerConfig.getInstance().database_password);

		int contadorr = 0;
		while (bandera) {

			System.out.println(getDateTime() + "------------------- se han consultado " + contadorr + " registros");

			// si no se especifica la consulta se hace por 10 records
			String manyRecord = "6";
			if (args.length > 0)
				manyRecord = args[0];

			/*
			 * aqui se hace la consulta a la base de datos y las respuestas se
			 * almacenan en un ResultSet
			 */
			System.out.println("hace la consulta y devuelve el result set");
			System.out.println("inicia query : " + getDateTime());
			/*ResultSet rs = DataBaseManager.makeQuery("select " + "*" + " from "
					+ "temp_georeferenced_records" +" where !is_fixed=1"+" group by locality" //+ " order by RAND()"
					+ " limit "+manyRecord, conx);*/

			/**
			 * TODO @Jorge La consulta se debe agrupar tanto por locality, country, county, etc... porque puede ser que existan varias locality iguales pero con diferente ubicación.
			 */
			ResultSet rs = DataBaseManager.makeQuery("select * from georeferenced_records where latitude is null and longitude is null "
					+ "and locality is not null and is_fixed=0 group by locality limit " + manyRecord, conx);

			/**
			 * TODO @Jorge Esto no es del todo cierto. Cuando el resulset arroja null es porque hubo un error en la consulta. No estoy seguro pero si ya no hay más registros, la consulta devuelve un
			 * resulset vacío.
			 */
			if (rs == null) {
				bandera = false;
				System.out.println("no hay registros para georreferenciar. Quit");
			}

			System.out.println("termina query : " + getDateTime());
			/*
			 * se crea el HashSet en donde se almacenaran los records creados con
			 * cada linea del Resultset
			 */
			HashSet<Record> grup = new HashSet<Record>();

			try { /* se recorre el result set y se crean los records */
				System.out.println("recorriendo el result set:" + manyRecord + " veces");

				while (rs.next()) {
					// System.out.println("recorriendo el result set");

					// isoCountryCode, country, state, county, locality, latitude,
					// longitude, nudConceptId, canonical, id, decode

					Record rec = new Record(rs.getString("iso_country_code"), rs.getString("country"), rs.getString("state_province"), rs.getString("county"), rs.getString("locality"), 0.0, 0.0, 0,
							null, rs.getInt("id"), true);

					// System.out.println("tamaño del HashSet .........: "+
					// grup.size());

					grup.add(rec);

				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			

			String horaEmpieza = getDateTime();
			System.out.println("inicia startGeorref a las " + horaEmpieza);

			/* se inicia la georreferenciacion */
			try {

				startGeorref(grup, conx);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JaxenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String horaTermina = getDateTime();
			System.out.println("termina startGeorref a las " + horaTermina);

			System.out.println("empezó a las : " + horaEmpieza + "\r\n" + " finalizó a las : " + horaTermina);
			System.out.println("started at : " + horaEmpieza + "\r\n" + " ended at : " + horaTermina);

			// /}cierra el for
			contadorr++;
		}// cierra while

		DataBaseManager.closeConnection(conx);

	}

	/**
	 * Finds the distance beetwen two given points
	 * 
	 * @return the number in meters
	 */
	public Double distancePoints() {
		Double distance = 0.0;

		Point punto1 = null;
		Point punto2 = null;
		punto1.distance(punto2);
		System.out.println("distance  from 1 to 2 : " + "");
		// pendiente convertir de grados a metros
		/*
		 * http://www.vividsolutions.com/jts/javadoc/com/vividsolutions/jts/geom/
		 * Geometry.html#distance(com.vividsolutions.jts.geom.Geometry)
		 */

		return distance;
	}

	public static int consulta(String query, Connection conx) {

		/*
		 * aqui se hace la consulta a la base de datos y las respuestas se
		 * almacenan en un ResultSet
		 */
		System.out.println("realiza el update o insert");
		System.out.println("inicia update : " + getDateTime());
		System.out.println(query + "\n");

		return DataBaseManager.makeChange(query, conx);

	}

	/**
	 * 
	 * prints out the system's date and time
	 * 
	 * @return the date and time in string format
	 */
	private static String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static Document getDocument(final String xmlFileName) {
		Document document = null;
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(xmlFileName);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	private static Boolean writeFile(String lat, String lon, Double minorUncertainty, String filename) throws IOException {

		String str;

		File f = new File(filename);
		FileOutputStream fop = new FileOutputStream(f, true);

		if (f.exists()) {
			str = lat + ";" + lon + ";" + minorUncertainty + "\n";
			fop.write(str.getBytes());

			fop.flush();
			fop.close();
		}

		return true;
	}

}
