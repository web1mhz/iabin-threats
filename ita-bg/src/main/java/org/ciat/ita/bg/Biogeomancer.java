package org.ciat.ita.bg;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.JDOMParseException;
import org.jdom.input.SAXBuilder;

public class Biogeomancer {

	// private static final String BgServer =
	// "http://bg.berkeley.edu:8080/ws-test/batch";
	private static final String BgServer = "http://bg.berkeley.edu/ws/batch";
	private static String debugMode=null;

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
	public static Set<Record> startGeorref(Set<Record> places, Connection conx)
			throws IOException, DocumentException, JaxenException {

		// writes down timing statistics file and amount of geo referenced data
	
	
		File f=null;
		String str;
		String outputFile = "out.txt";
		String horaInicio = getDateTime();
		FileOutputStream fop;
	//	____________Debugging mode on____________________________________________________________________
		
		if(debugMode.equalsIgnoreCase("true"))
		{

		 f= new File(outputFile);
		 fop = new FileOutputStream(f, true);

		if (f.exists()) {
			str = "\n\n*****************\nstart time : " + getDateTime();
			fop.write(str.getBytes());

			fop.flush();
			fop.close();
		}
		}//endif
//		________________________________________________________________________________

		try {
			String xmlData = dataToXML(places);
			
			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
			System.out.println("despues de datatoxml :" + xmlData);
			
			

			// writing xml query file on disk
			String outputFile2 = "xmlData.txt";

			File f2 ;
			FileOutputStream fop2;
			
			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
			{
				f2= new File(outputFile2);
			
				 fop2= new FileOutputStream(f2, true);

				if (f.exists()) {
					fop2.write(xmlData.getBytes());
					fop2.flush();
					fop2.close();
				}
			
			}//end if debugging mode

			// enviando a servidor biogeomancer y obteniendo respuesta.
			URL urlBgServer;
			urlBgServer = new URL(BgServer);

			// sigue solicitando informacion al servidor hasta que devuelva
			// diferente de null durante 20 intentos mas
			
			
			
		//	String xmlResult = setServiceUrl(urlBgServer, xmlData);
			String xmlResult=null;
			
	/*		int intentos = 0;
			String horaInicioAtaqueDos = getDateTime();
			while (xmlResult == null && intentos < 2) { // prueba 1 vez con el
															// server
				System.out.println("\ntiempo : " + getDateTime());
				System.out.println("\nnumero de intentos de conexion: "
						+ intentos);
				xmlResult = setServiceUrl(urlBgServer, xmlData);
				intentos++;
			}
			System.out.println("empezo a las " + horaInicioAtaqueDos);
			System.out.println("termino a las " + getDateTime());
*/
			/*----------------------------------------------------------------------------*/


			/*----------------------------------------------------------------------------*/

			/*
			 * TODO: Interpretar dicho String (xmlResult) como un xml y extraer
			 * cada uno de los valores de latitud y longitud para ser ingresadas
			 * en el objeto Record de la coleccion.
			 * 
			 * Completar el codigo a continuacion:
			 */
			
			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))		System.out.println("va a leer los tags xml");
			// Working with XML

			// System.out.println("xmlresult inputstream: " + xmlResult);

			// se selecciona la fuente desde la que se obtiene el archivo xml de
			// BG
			Document document = null;
			if (xmlResult != null) {
				document = org.dom4j.DocumentHelper.parseText(xmlResult);
				
				
				//	____________Debugging mode on____________________________________________________________________
				if(debugMode.equalsIgnoreCase("true"))
				{
					System.out.println("se leyo el xml del servidor ********");
					System.out.println("xmlresult: " + xmlResult); 	
					// muestra el xml resultado recibido de biogeomancer
				}
				
			}/*
			 * else{System.out.println(
			 * "se requiere el archivo 100Records.xml para ser analizado");
			 * 
			 * document=getDocument("100Records.xml"); }
			 */

			// System.out.println("*******************************************");

			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
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
				// System.out.println("\n" + ((Node) nodes.get(i)).getName() +
				// " numero " + i);
				// System.out.println("getText"+((Node)nodes.get(i)).getText()
				// );
				
				//	____________Debugging mode on____________________________________________________________________
				if(debugMode.equalsIgnoreCase("true"))
				System.out.println("getStringValue"	+ ((Node) nodes.get(i)).getStringValue());
				
				values = ((Node) nodes.get(i)).getStringValue();

				data = values.split("\n");
				int x = data.length;
				
				//	____________Debugging mode on____________________________________________________________________
				if(debugMode.equalsIgnoreCase("true"))
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
				/*
				 * for (int y = 0; y < x; y++) { System.out.println("valor " + y
				 * + " : " + data[y]);
				 * 
				 * }
				 */
				// se halla el punto con menor uncertainty
				int j = 11;

				if (x > 11) {
					minorUncertainty = Double
							.parseDouble(data[data.length - 1]);
					minorLatitude = data[data.length - 4];
					minorLongitude = data[data.length - 3];
				}
				while (j < x) {

					// System.out.println("minor uncertainty :" +
					// minorUncertainty);
					if (Double.parseDouble(data[data.length - 1]) < minorUncertainty) {
						minorUncertainty = Double
								.parseDouble(data[data.length - 1]);
						minorLatitude = data[data.length - 4];
						minorLongitude = data[data.length - 3];

					}
					j = j + 7;
				}
				
				
				//	____________Debugging mode on____________________________________________________________________
				if(debugMode.equalsIgnoreCase("true"))
				{
					System.out.println("the minor uncertainty is : "+ minorUncertainty);
					System.out.println("minor Latitude is : " + minorLatitude);
					System.out.println("minor Longitude is : " + minorLongitude);
				}

				
				//	____________Debugging mode on____________________________________________________________________
				if(debugMode.equalsIgnoreCase("true"))
				{
					// escribir los resultados en archivo csv
					Boolean da = writeFile(minorLatitude, minorLongitude,minorUncertainty, "records.csv");
					System.out.println("escribio el resultado " + da);
					System.out.println("largo del arreglo split :" + x);
				}

				
				// escribir las coordenadas en los campos Blatitud , Blongitud,
				// uncertainty and is_fixed
				// is_fixed=1 se georreferencio correctamente
				// is_fixed=2 Biogeomancer no pudo referenciar el registro
				// is_fixed=3 Biogeomancer returned an error, internal error

				if (it.hasNext()) {
					reco = it.next();
					idrec = reco.getId();
					
					//	____________Debugging mode on____________________________________________________________________
					if(debugMode.equalsIgnoreCase("true"))
					System.out.println("id's editadas " + idrec);
					/*
					 * consulta("update temp_georeferenced_records set blatitude="
					 * +minorLatitude
					 * +", blongitude="+minorLongitude+", uncertainty="
					 * +minorUncertainty+", is_fixed=1 where id="+idrec+" ;" );
					 */
					if (minorLatitude != "" && minorLongitude != "") {
						consulta("update "+ServerConfig.getInstance().dbTableRecords+" set blatitude="
								+ minorLatitude + ", blongitude="
								+ minorLongitude + ", uncertainty="
								+ minorUncertainty + ", is_fixed=1 where id="
								+ idrec + " ;", conx);
					} else {
						consulta(
								"update "+ServerConfig.getInstance().dbTableRecords+" set is_fixed=2 where id="
										+ idrec + " ;", conx);
					}

				} else {
					System.out.println("no hay records en el resultset places");
				}
			}

			str = "\nstart time" + getDateTime()
					+ "\nlos registros que no tienen respuesta son :"
					+ sinrespuesta
					+ "\nlos registros que tienen 1 respuesta  son :"
					+ unasolarespuesta
					+ "\nlos registros que tienen 2 respuestas son :"
					+ dosrespuestas
					+ "\nlos registros que tienen >2 respuestas son :"
					+ masde2respuestas + "\n el total de los registros es :"
					+ nodes.size() + "\nstart time : " + horaInicio
					+ "\nend time : " + getDateTime();

			
			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
			System.out.println(str);
			
			//when the via POST request fails it connects via GET
			if(nodes.size()==0||xmlResult == null)
			{

				try {
					connectByGet(places, conx);
				} catch (JDOMException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			/*
			 * System.out.println("los registros que no tienen respuesta son :"
			 * + sinrespuesta);
			 * System.out.println("los registros que tienen 1 respuesta  son :"
			 * + unasolarespuesta);
			 * System.out.println("los registros que tienen 2 respuestas son :"
			 * + dosrespuestas);
			 * System.out.println("los registros que tienen >2 respuestas son :"
			 * + masde2respuestas);
			 * System.out.println("el total de los registros es :" +
			 * nodes.size());
			 */

			if (nodes.size() == 0) {

				System.out.println("sleeps during 10 seconds ...");
				for (int i = 0; i < 10; i++) {

					System.out.print(i + " ");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				System.out.println("wakes up, retrying");
			}

			
			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
			{
				// writes the timing and results to d:/out.txt
				fop = new FileOutputStream(f, true);

				if (f.exists()) {

					fop.write(str.getBytes());

					fop.flush();
					fop.close();
				}

				System.out.println("*******************************************");
			}//end if debugging mode
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		return places;
	}

	/**
	 * @param places
	 *            - A Set of places (Records) that have not been georreferenced.
	 * @return A XML-String formatted with the parameters needed for the
	 *         Biogeomancer Server to the understanding of the information.
	 */
	private static String dataToXML(Set<Record> places) {
		
		//	____________Debugging mode on____________________________________________________________________
		if(debugMode.equalsIgnoreCase("true"))
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
	 *            service URL connect to URL: set URL Method to POST and write
	 *            batch interface for doPost to read the interface is in String
	 *            data (class static variable) write the xml text to a file,
	 *            default file name is autoGenerate.xml store in current working
	 *            directory
	 */
	private static String setServiceUrl(URL serviceUrl, String XMLdata) {

		//	____________Debugging mode on____________________________________________________________________
		if(debugMode.equalsIgnoreCase("true"))
		System.out.println("inicia setServiceUrl");
		
		HttpURLConnection connection;
		try {
			// START PROXY CODE CONFIGURATION IN CIAT PLACE
			System.getProperties().put("proxySet", "true");
			System.getProperties().put("proxyHost", "proxy4.ciat.cgiar.org");
			System.getProperties().put("proxyPort", "8080");
			// END PROXY CODE CONFIGURATION

			connection = (HttpURLConnection) serviceUrl.openConnection();
System.out.println("incio el envio");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			System.out.println("escribe el xml");
			// Post the data item
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(XMLdata.getBytes());
			outputStream.flush();
			outputStream.close();

			String xmlText = null;

			System.out.println("espera respuesta");
			// Retrieve the output
			int responseCode = connection.getResponseCode();
			InputStream inputStream;
			
			System.out.println("respuestaaaaaaaaaaaa");
			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
			System.out.println("response code: " + responseCode);
			
			
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
				xmlText = toString(inputStream);

				
				//	____________Debugging mode on____________________________________________________________________
				if(debugMode.equalsIgnoreCase("true"))
				{
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
				}//end if debugging mode

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

			
			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
			{
				// write the output to the console
				System.out.println("esto se recibe del servidor : \n");

				// System.out.println("setserviceurl :" + xmlText);
				System.out.println("setserviceurl :");
			
			}
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			while (null != (string = reader.readLine())) {
				outputBuilder.append(string).append('\n');
			}
		}
		return outputBuilder.toString();
	}

	/**
	 * the sql consult is performed, then every record is stored into a hashset,
	 * then is called the method startGeorref.
	 * 
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) {
		DataBaseManager.registerDriver();

		System.getProperties().put("proxySet", "true");
		System.getProperties().put("proxyHost", "proxy4.ciat.cgiar.org");
		System.getProperties().put("proxyPort", "8080");

		String DBname = ServerConfig.getInstance().dbTableRecords;
		debugMode = ServerConfig.getInstance().database_debug;
		System.out.println("debug mode: "+debugMode);
		
		boolean bandera = true;
		Connection conx;
		conx = DataBaseManager.openConnection(
				ServerConfig.getInstance().database_user, ServerConfig
						.getInstance().database_password);

		int contadorr = 0;
		while (bandera) {

			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
			System.out.println(getDateTime()+ "------------------- se van a consultar " + contadorr	+ " registros");

			// si no se especifica la consulta se hace por 10 records
			String manyRecord = "100";
			if (args.length > 0)
				manyRecord = args[0];

			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
			{
				/*
				 * aqui se hace la consulta a la base de datos y las respuestas se
				 * almacenan en un ResultSet
				 */
				System.out.println("hace la consulta y devuelve el result set");
				System.out.println("inicia query : " + getDateTime());
			}
			/*
			 * ResultSet rs = DataBaseManager.makeQuery("select " + "*" +
			 * " from " + ServerConfig.getInstance().dbTableRecords
			 * +" where !is_fixed=1"+" group by locality" //+ " order by RAND()"
			 * + " limit "+manyRecord, conx);
			 */

			/**
			 * TODO @Jorge La consulta se debe agrupar tanto por locality,
			 * country, county, etc... porque puede ser que existan varias
			 * locality iguales pero con diferente ubicaci�n.
			 */
			
			  ResultSet rs = DataBaseManager .makeQuery( "select * from "+
			  DBname + " where latitude is null and longitude is null " +
			  "and locality is not null and is_fixed=0 group by locality, country, county, state_province limit "
			  + manyRecord, conx);
			 
		/*	ResultSet rs = DataBaseManager.makeQuery("select * from " + DBname
					+ " where is_fixed=8", conx);

			/*
			 * 
			 * //esta consulta geo referencia registros buenos para validar la
			 * calidad de los datos de biogeomancer ResultSet rs =
			 * DataBaseManager .makeQuery(
			 * 
			 * "select * from "+ DBname + " "+ "where latitude is not null " +
			 * "and longitude is not null " + "and latitude !=0 " +
			 * "and longitude !=0 " + "and latitude!='' " + "and longitude!='' "
			 * + "and locality!='.' " + "and locality!='-' " +
			 * "and locality is not null "+ "and country!='' " +
			 * "and country!='-' "+ "and country is not null " +
			 * "and is_fixed=0 "+ "group by state_province limit 100" ,conx);
			 */

			/*
			 * //esta consulta geo referencia registros con error 'O' ResultSet
			 * rs = DataBaseManager .makeQuery(
			 * 
			 * "select * from georeferenced_records g, temp_bad_records b "+
			 * "where g.id=b.id "+ "and b.error='O' "+ "and is_fixed=0 "+
			 * "group by locality "+ "limit 100 " ,conx);
			 */
			// esta consulta geo referencia registros con error 'WC'
			/*
			 * ResultSet rs = DataBaseManager .makeQuery(
			 * 
			 * "select * from georeferenced_records g, temp_bad_records b "+
			 * "where g.id=b.id "+ "and b.error='WC' "+ "and is_fixed=0 "+
			 * "group by locality "+ "limit 100 " ,conx);
			 */
			/**
			 * TODO @Jorge Esto no es del todo cierto. Cuando el resulset arroja
			 * null es porque hubo un error en la consulta. No estoy seguro pero
			 * si ya no hay m�s registros, la consulta devuelve un resulset
			 * vac�o.
			 */

			try {
				if (rs == null || rs.isClosed()) {
					bandera = false;
					System.out
							.println("there are no records for georeferencing. Quit");
					break;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
			System.out.println("ends query : " + getDateTime());
			/*
			 * se crea el HashSet en donde se almacenaran los records creados
			 * con cada linea del Resultset
			 */
			HashSet<Record> grup = new HashSet<Record>();

			try { /* se recorre el result set y se crean los records */
				
				//	____________Debugging mode on____________________________________________________________________
				if(debugMode.equalsIgnoreCase("true"))
				System.out.println("recorriendo el result set:" + manyRecord+ " veces");

				while (rs.next()) {
					// System.out.println("recorriendo el result set");

					// isoCountryCode, country, state, county, locality,
					// latitude,
					// longitude, nudConceptId, canonical, id, decode

					Record rec = new Record(rs.getString("iso_country_code"),
							rs.getString("country"), rs
									.getString("state_province"), rs
									.getString("county"), rs
									.getString("locality"), 0.0, 0.0, 0, null,
							rs.getInt("id"), true);

					// System.out.println("tama�o del HashSet .........: "+
					// grup.size());

					grup.add(rec);

				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String horaEmpieza = getDateTime();
			System.out.println("Georeferenciation starts at " + horaEmpieza);

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
			System.out.println("georeferentiation ends at " + horaTermina);

			
			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
			{
			System.out.println("empez� a las : " + horaEmpieza + "\r\n"	+ " finaliz� a las : " + horaTermina);
			System.out.println("started at : " + horaEmpieza + "\r\n"	+ " ended at : " + horaTermina);
			}
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
		
		//	____________Debugging mode on____________________________________________________________________
		if(debugMode.equalsIgnoreCase("true"))
		{
		System.out.println("realiza el update o insert");
		System.out.println("inicia update : " + getDateTime());
		System.out.println(query + "\n");
		}
		
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

	private static Boolean writeFile(String lat, String lon,
			Double minorUncertainty, String filename) throws IOException {

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

	private static String connectByGet(Set<Record> places, Connection conx)
			throws DocumentException, JDOMException, IOException {

		String country;
		String state;
		String county;
		String locality;
		String address;
		int idrec;
		String xmlRes;
		String[] data;
		String values = "";
		String longitude;
		String latitude;
		String uncertainty;
		Double minorUncertainty = 99999999999999.0;
		String minorLongitude = "";
		String minorLatitude = "";

		String dbname = ServerConfig.getInstance().dbTableRecords;

		for (Record p : places) {
			xmlRes = "";
			country = p.getCountry();
			state = p.getState();
			county = p.getCounty();
			locality = p.getLocality();
			idrec = p.getId();

			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
			{
				System.out.println(country);
				System.out.println(county);
				System.out.println(state);
				System.out.println(locality);
				System.out.println("id : " + idrec +" done.");
			}
			if(!debugMode.equalsIgnoreCase("true")) System.out.print("id : " + idrec +" done. ");

			if (country == null)
				country = "";
			else
				country = country + ",";
			if (state == null)
				state = "";
			else
				state = state + ",";
			if (county == null)
				county = "";
			else
				county = county + ",";
			if (locality == null)
				locality = "";

			String address2;

			address = "http://bg.berkeley.edu:8080/ws/single?sp=" + country
					+ state + county + "&locality=" + locality;
			// address=
			// "http://bg.berkeley.edu:8080/ws/single?sp=Montana&locality=14%20mi%20SSW%20Missoula";
			
			//	____________Debugging mode on____________________________________________________________________
			if(debugMode.equalsIgnoreCase("true"))
			System.out.println(address);

			/*
			 * address2=URLEncoder.encode(address, "cp1252");
			 * System.out.println("cp1252\n"+address2);
			 * address2=URLEncoder.encode(address, "utf-8");
			 * System.out.println("utf-8\n"+address2);
			 * address2=URLEncoder.encode(address, "utf-16");
			 * System.out.println("utf-16\n"+address2);
			 * address2=URLEncoder.encode(address, "iso-8859-1");
			 * System.out.println("iso5981\n"+address2);
			 * 
			 * address2=URLDecoder.decode(address, "cp1252");
			 * System.out.println("cp1252\n"+address2);
			 * address2=URLDecoder.decode(address, "utf-8");
			 * System.out.println("utf-8\n"+address2);
			 * address2=URLDecoder.decode(address, "utf-16");
			 * System.out.println("utf-16\n"+address2);
			 * address2=URLDecoder.decode(address, "iso-8859-1");
			 * System.out.println("iso5981\n"+address2);
			 */

			// xmlRes=connectByGet(address);
			// System.out.println("xml: \n"+xmlRes);

			try {

				URL url = new URL(address);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						url.openStream()));
				String str;

				while ((str = in.readLine()) != null) {
					xmlRes = xmlRes + str + "\n";
					// System.out.println("STR: \n");
					//	____________Debugging mode on____________________________________________________________________
					if(debugMode.equalsIgnoreCase("true"))	System.out.println(str);

				}
				/*
				 * System.out.println("this is received from the server \n");
				 * System.out.println(xmlRes);
				 */

				in.close();
			} catch (MalformedURLException e) {
				System.out.println("MalformedURL");
			} catch (IOException e) {
				if(debugMode.equalsIgnoreCase("true")) System.out.println("I/O Error. Malformed response or none");
				if(!debugMode.equalsIgnoreCase("true")) System.out.print("is_fixed : 3\r\n");
			}

			// interpreting the xml

			SAXBuilder builder = new SAXBuilder(false);
			try {
				org.jdom.Document document = builder.build(new ByteArrayInputStream(xmlRes.getBytes()));

				Element root = document.getRootElement();

				List<Element> children = root.getChildren();
				
				//	____________Debugging mode on____________________________________________________________________
				if(debugMode.equalsIgnoreCase("true"))
				System.out.println("numero de resultados : "+ (children.size() - 1));

				for (int i = 0; i < children.size(); i++) {
					Element kid = children.get(i);
					// System.out.println("getValue:"+kid.getValue());
					// System.out.println("getName:"+kid.getName());

					if (kid.getName() == "georeference") {

						values = kid.getValue();
						// System.out.println("getValue: "+kid.getValue() );

						data = values.split("\n");
						int x = data.length;
						// System.out.println("largo del arreglo split :" + x);
						uncertainty = data[data.length - 1];
						latitude = data[data.length - 4];
						longitude = data[data.length - 3];

						if (Double.parseDouble(uncertainty) < minorUncertainty) {
							minorUncertainty = Double.parseDouble(uncertainty);
							minorLatitude = latitude;
							minorLongitude = longitude;
						}

						

					} else {
						if (kid.getName() != "interpreter")
							
							//	____________Debugging mode on____________________________________________________________________
							if(debugMode.equalsIgnoreCase("true"))
							{
							System.out.println("no hay resultados de biogeomancer");
							System.out.println("--------------------------------------");
							}

					}// ends else

				}// ends for

				//	____________Debugging mode on____________________________________________________________________
				if(debugMode.equalsIgnoreCase("true"))
				{
					System.out.println("latitude : " + minorLatitude);
					System.out.println("longitude : " + minorLongitude);
					System.out.println("uncertainty : " + minorUncertainty);
				}

				String fixedCountry = p.getCountry();
				if (p.getCountry() != null && p.getCountry() != "")
					fixedCountry = "'"	+ DataBaseManager.correctStringToQuery(p.getCountry()) + "'";
				else
					fixedCountry = null;
				String fixedCounty = p.getCounty();
				if (p.getCounty() != null && p.getCounty() != "")
					fixedCounty = "'"+ DataBaseManager.correctStringToQuery(p.getCounty()) + "'";
				else
					fixedCounty = null;
				String fixedState = p.getState();
				if (p.getState() != null && p.getState() != "")
					fixedState = "'"+ DataBaseManager.correctStringToQuery(p.getState()) + "'";
				else
					fixedState = null;
				String fixedLocality = p.getState();
				if (p.getLocality() != null && p.getLocality() != "")
					fixedLocality = "'"+ DataBaseManager.correctStringToQuery(p.getLocality()) + "'";
				else
					fixedLocality = null;

				// writes in DB
				if (minorLatitude != "" && minorLongitude != "") {
					/*
					 * consulta("update "+
					 * ServerConfig.getInstance().dbTableRecords +
					 * " set blatitude=" + minorLatitude + ", blongitude=" +
					 * minorLongitude + ", uncertainty=" + minorUncertainty +
					 * ", is_fixed=1 where country="+(fixedCountry) +
					 * " and county="+(fixedCounty) +
					 * " and locality="+(fixedLocality) +
					 * " and state_province="+(fixedState) +" ;", conx);
					 */
					consulta("update "
							+ ServerConfig.getInstance().dbTableRecords
							+ " set blatitude=" + minorLatitude
							+ ", blongitude=" + minorLongitude
							+ ", uncertainty=" + minorUncertainty
							+ ", is_fixed=1 where id=" + idrec + " ;", conx);
					if(!debugMode.equalsIgnoreCase("true")) System.out.print("is_fixed : 1\r\n");
				} else {
					consulta("update "
							+ ServerConfig.getInstance().dbTableRecords
							+ " set is_fixed=2 where id=" + idrec + " ;", conx);
					if(!debugMode.equalsIgnoreCase("true")) System.out.print("is_fixed : 2\r\n");
				}

				/*
				 * if (minorLatitude != "" && minorLongitude != "") {
				 * System.out.println("update "+
				 * ServerConfig.getInstance().dbTableRecords + " set blatitude="
				 * + minorLatitude + ", blongitude=" + minorLongitude +
				 * ", uncertainty=" + minorUncertainty +
				 * ", is_fixed=1 where id=" + idrec + " ;"); } else {
				 * System.out.println("update "+
				 * ServerConfig.getInstance().dbTableRecords +
				 * " set is_fixed=2 where id=" + idrec + " ;"); }
				 */

			} catch (JDOMParseException e) {
				
				//	____________Debugging mode on____________________________________________________________________
				if(debugMode.equalsIgnoreCase("true"))
				System.out.println("the server presented an error for this record, this will be marked with is_fixed=3");
				
				consulta("update " + ServerConfig.getInstance().dbTableRecords
						+ " set is_fixed=3 where id=" + idrec
						+ " and is_fixed=0 ;", conx);
			}

			minorUncertainty = 99999999999999.0;
			minorLongitude = "";
			minorLatitude = "";
		}
		return "success!!";
	}// ends method

	private static String connectByGet(String address) {
		String res = "";
		try {
			URL url = new URL(address);

			BufferedReader in = new BufferedReader(new InputStreamReader(url
					.openStream()));
			String str = null;

			while ((str = in.readLine()) != null) {
				res = res + str + "\n";
				// System.out.println(str);
			}

			in.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}

		return res;
	}

}
