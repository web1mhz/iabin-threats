package org.ciat.ita.bg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import java.util.List;
import java.util.Set;

import org.ciat.ita.bg.database.DataBaseManager;
import org.ciat.ita.bg.model.Record;
import org.ciat.ita.bg.server.ServerConfig;
//import org.jdom.Element;
//import org.jdom.JDOMException;
//import org.jdom.input.SAXBuilder;
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

	// private static final String BgServer =
	// "http://bg.berkeley.edu:8080/ws-test/batch";
	private static final String BgServer = "http://bg.berkeley.edu/ws/batch";

	/**
	 * @param places
	 *            - A Set of places (Records) that have not been georreferenced.
	 * @return The same Set of places but re-georreferenced.
	 * @throws IOException
	 * @throws JDOMException
	 * @throws DocumentException
	 * @throws JaxenException 
	 */
	public static Set<Record> startGeorref(Set<Record> places)
			throws IOException, DocumentException, JaxenException {
		
		
		String outputFile="out.txt";
		String horaInicio=getDateTime();
		String str;
		
		File f=new File(outputFile);
	      FileOutputStream fop=new FileOutputStream(f, true);

	      if(f.exists()){
	      str="\n\n*****************\nstart time : "+getDateTime();
	          fop.write(str.getBytes());

	          fop.flush();
	          fop.close();
	      }
        
        
       
		
		try {
			String xmlData = dataToXML(places);
			System.out.println("despues de datatoxml :" + xmlData);

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
			System.out.println("va a leer los tags xml");
			// Working with XML

			System.out.println("xmlresult inputstream: " + xmlResult);
			Document document = org.dom4j.DocumentHelper.parseText(xmlResult);

			System.out.println("*******************************************");

			System.out.println("*****************shows xml values**************************");
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("dwc", "http://rs.tdwg.org/dwc/terms/");
			
			XPath xpath = DocumentHelper.createXPath("//record");
			xpath.setNamespaceContext(new SimpleNamespaceContext(map));
			
			List nodes = xpath.selectNodes(document);
//***********
			String values = "";
			String[] data;
			String[] coordinates;
			String longitude;
			String latitude;
			String uncertainty;
			
			int sinrespuesta=0,unasolarespuesta=0,dosrespuestas=0,masde2respuestas=0;
	//*************		
			for (int i = 0; i < nodes.size(); i++) {
				//System.out.println("contador :"+i);
				System.out.println("\n"+((Node)nodes.get(i)).getName() +" numero "+i  );
				//System.out.println("getText"+((Node)nodes.get(i)).getText() );
				System.out.println("getStringValue"+((Node)nodes.get(i)).getStringValue());
				values=((Node)nodes.get(i)).getStringValue();	
			
				
				
				data = values.split("\n");
				int x = data.length;
				System.out.println("largo del arreglo split :"+x);

				if(x==6)
				{
					sinrespuesta++;
				}
				if(x==12)
				{
					unasolarespuesta++;
					uncertainty = data[data.length - 1];
					latitude = data[data.length - 4];
					longitude = data[data.length - 3];
					
					System.out.println("latitude : " + latitude);
					System.out.println("longitude : " + longitude);
					System.out.println("uncertainty : " + uncertainty);
				}
				if(x==19)
				{
					dosrespuestas++;
					uncertainty = data[data.length - 1];
					latitude = data[data.length - 4];
					longitude = data[data.length - 3];
					
					System.out.println("latitude : " + latitude);
					System.out.println("longitude : " + longitude);
					System.out.println("uncertainty : " + uncertainty);
					
					uncertainty = data[data.length - 8];
					latitude = data[data.length - 11];
					longitude = data[data.length - 10];
					
					System.out.println("latitude : " + latitude);
					System.out.println("longitude : " + longitude);
					System.out.println("uncertainty : " + uncertainty);
				}
				if(x>19)masde2respuestas++;	

				//muestra cada valor del record en el arreglo despues del split
			  for (int y = 0; y < x; y++) {
					System.out.println("valor " + y + " : " + data[y]);
				}

				System.out.println("largo del arreglo split :"+x);

				
			}
			System.out.println("los registros que no tienen respuesta son :"+sinrespuesta);
			System.out.println("los registros que tienen 1 respuesta  son :"+unasolarespuesta);
			System.out.println("los registros que tienen 2 respuestas son :"+dosrespuestas);
			System.out.println("los registros que tienen >2 respuestas son :"+masde2respuestas);
			System.out.println("el total de los registros es :"+nodes.size());
			
			//writes the timing and results to d:/out.txt
			fop=new FileOutputStream(f, true);
			
			if(f.exists()){
			      str="\nstart time"+getDateTime()
			      +"\nlos registros que no tienen respuesta son :"+sinrespuesta
			      +"\nlos registros que tienen 1 respuesta  son :"+unasolarespuesta
			      +"\nlos registros que tienen 2 respuestas son :"+dosrespuestas
			      +"\nlos registros que tienen >2 respuestas son :"+masde2respuestas
			      +"\nel total de los registros es :"+nodes.size()
			      +"\nstart time : "+horaInicio
			      +"\nend time : "+getDateTime();
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
	 * @return A XML-String formatted with the parameters needed for the
	 *         Biogeomancer Server to the understanding of the information.
	 */
	private static String dataToXML(Set<Record> places) {
		System.out
				.println("-------------------------------------------dataToXML(Set<Record> places) {");

		StringBuffer data = new StringBuffer(
				"<?xml version=\"1.0\" encoding=\"utf-8\"?><biogeomancer xmlns=\"http://bg.berkeley.edu\" xmlns:dwc=\"http://rs.tdwg.org/dwc/terms\" ><request type=\"batch\" interpreter=\"yale\" header=\"true\">");
		for (Record p : places) {
			data.append("<record> <dwc:country>");
			data.append(p.getCountry());
			//System.out.println("pais ---------------------> : "+ p.getCountry());
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
			System.out.println("response code: " + responseCode);
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

			System.out.println("setserviceurl :" + xmlText);
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

	public static void main(String[] args) {
		
		//for(int j=0;j<5;j++){
//System.out.println("main"+j);
		Connection conx;
		DataBaseManager.registerDriver();
		conx = DataBaseManager.openConnection(
				ServerConfig.getInstance().database_user, ServerConfig
						.getInstance().database_password);

		/*
		 * aqui se hace la consulta a la base de datos y las respuestas se
		 * almacenan en un ResultSet
		 */
		System.out.println("hace la consulta y devuelve el result set");
System.out.println("inicia query : "+getDateTime());
		ResultSet rs = DataBaseManager.makeQuery("select " + "*" + " from "
				+ "temp_georeferenced_records" +" order by RAND()"+ " limit 100", conx);
System.out.println("termina query : "+getDateTime());
		/*
		 * se crea el HashSet en donde se almacenaran los records creados con
		 * cada linea del Resultset
		 */
		HashSet<Record> grup = new HashSet<Record>();

		try { /* se recorre el result set y se crean los records */
			System.out.println("recorriendo el result set");

			while (rs.next()) {
				//System.out.println("recorriendo el result set");

				// isoCountryCode, country, state, county, locality, latitude,
				// longitude, nudConceptId, canonical, id, decode

				Record rec = new Record(rs.getString("iso_country_code"), rs
						.getString("country"), rs.getString("state_province"),
						rs.getString("county"), rs.getString("locality"), 0.0,
						0.0, 0, null, rs.getInt("id"), true);

				//System.out.println("tamaño del HashSet .........: "+ grup.size());

				grup.add(rec);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DataBaseManager.closeConnection(conx);

		String horaEmpieza = getDateTime();
		System.out.println("inicia startGeorref a las "+horaEmpieza);
		
		
		/* se inicia la georreferenciacion */
		try {


			startGeorref(grup);
		

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
		System.out.println("termina startGeorref a las "+horaTermina);
		
		
		System.out.println("empezó a las : " + horaEmpieza + "\r\n"+ " finalizó a las : " + horaTermina);
		System.out.println("started at : " + horaEmpieza + "\r\n"+ " ended at : " + horaTermina);
		
		DataBaseManager.closeConnection(conx);
		
		///}cierra el for
	}

	public Double distancePoints() {
		Double distance = 0.0;

		Point punto1 = null;
		Point punto2 = null;
		punto1.distance(punto2);
		System.out.println("distance  from 1 to 2 : " + "");
		//pendiente convertir de grados a metros
		/*
		 * http://www.vividsolutions.com/jts/javadoc/com/vividsolutions/jts/geom/Geometry.html#distance(com.vividsolutions.jts.geom.Geometry)
		 */

		return distance;
	}
	private static String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
