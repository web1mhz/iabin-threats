package org.ciat.ita.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ClientConfig {

	private static final String XML_FILE = "client_config.xml";
	private static final String XML_IP_ADDR = "ip_addr";
	private static final String XML_PATH = "path";
	private static final String XML_NAME = "name";
	private static final String XML_RECORDS = "records";
	private static final String XML_ISO = "ISO";
	private static final String XML_COUNTRY = "country";
	private static final String XML_STATE = "state";
	private static final String XML_COUNTY = "county";
	private static final String XML_COMMUNICATION_TYPE = "communication_type";
	public static final String RMI_COMMUNICATION = "RMI";
	public static final String TCP_COMMUNICATION = "TCP";
	public static final String OBJECT_COMMUNICATION = "OBJECT";
	public static final String HTTP_PROXY_SERVER = "server";
	public static final String HTTP_PROXY_PORT = "port";

	/**
	 * IP/Domain address of the Http-Proxy for connection to Biogeomancer service.
	 */
	public static String httpProxyServer;	
	/**
	 * Http-Proxy port for connection to Biogeomancer service. 
	 */
	public static String httpProxyPort;	
	/**
	 * IP address of the server to connect
	 */
	public static String server_ipaddr;
	/**
	 * full path of the shape file in .SHP extension
	 */
	public static String shapeFile;
	/**
	 * full path of the mask file in .JGM extension
	 */
	public static String maskFile;
	/**
	 * Amount of records to work by round
	 */
	public static int quantityRecords = 10000;
	/**
	 * Names of the bioclimatic variables
	 */
	public static Set<String> variablesName;
	/**
	 * bioclimatic varibles directory path
	 */
	public static String dirBioclimaticFiles;
	/**
	 * maxent jar file path
	 */
	public static String maxent_file;
	/**
	 * bioclimatic variables for maxent directory path
	 */
	public static String maxent_variables_dir;
	/**
	 * background csv file path
	 */
	public static String backgroung_file;
	/**
	 * lambda output directory path
	 */
	public static String lambda_output_dir;
	/**
	 * species csv data directory path
	 */
	public static String sample_dir;
	/**
	 * final output directory path
	 */
	public static String final_output_dir;
	/**
	 * Name of the shape file column of the ISO country value
	 */
	public static String nameColumnISO;
	/**
	 * Name of the shape file column of the country name value
	 */
	public static String nameColumnCountry;
	/**
	 * Name of the shape file column of the state value
	 */
	public static String nameColumnState;
	/**
	 * Name of the shape file column of the county value
	 */
	public static String nameColumnCounty;
	/**
	 * The way how it connects to the server. RMI or TCP
	 */
	public static String communication_type;

	/**
	 * Initialize the variables of the client configuration. It must be called
	 * at the beginning otherwise there will be a error if they are used
	 */
	public static void init() {
		try {
			File file = new File(XML_FILE);
			/**
			 * if the configuration file does not exist yet, then create the
			 * default one
			 */
			if (!file.exists()) {
				createDeafaultXML();
			}
			readFromXML();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		init();
	}

	@SuppressWarnings("unchecked")
	private static void readFromXML() throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder(false);
		Document doc = builder.build(new File(XML_FILE));
		Element config = doc.getRootElement();

		Element server = config.getChild("server");
		server_ipaddr = server.getAttributeValue(XML_IP_ADDR);
		communication_type = server.getAttributeValue(XML_COMMUNICATION_TYPE);

		quantityRecords = Integer.parseInt(config.getChild("work")
				.getAttributeValue(XML_RECORDS));

		Element mask = config.getChild("mask");
		maskFile = mask.getAttributeValue(XML_PATH);

		Element shape = config.getChild("shape");
		shapeFile = shape.getAttributeValue(XML_PATH);

		List<Element> columns = (List<Element>) shape.getChildren();
		for (Element column : columns) {
			String name = column.getAttributeValue(XML_NAME);
			if (name.equalsIgnoreCase(XML_ISO)) {
				nameColumnISO = column.getText();
			} else {
				if (name.equalsIgnoreCase(XML_COUNTRY)) {
					nameColumnCountry = column.getText();
				} else {
					if (name.equalsIgnoreCase(XML_STATE)) {
						nameColumnState = column.getText();
					} else {
						if (name.equalsIgnoreCase(XML_COUNTY)) {
							nameColumnCounty = column.getText();
						} else {
							// unknow column
						}
					}
				}
			}
		}

		Element maxent = config.getChild("maxent");
		List<Element> maxentfiles = (List<Element>) maxent.getChildren("file");
		for (Element fi : maxentfiles) {
			if (fi.getAttributeValue("name").equals("maxent_file")) {
				maxent_file = fi.getAttributeValue("path");
			} else {
				if (fi.getAttributeValue("name").equals("backgroung_file")) {
					backgroung_file = fi.getAttributeValue("path");
				} else {
					if (fi.getAttributeValue("name").equals("lambda_output_dir")) {
						lambda_output_dir = fi.getAttributeValue("path");
					} else {
						if (fi.getAttributeValue("name").equals("final_output_dir")) {
							final_output_dir = fi.getAttributeValue("path");
						} else {
							if (fi.getAttributeValue("name").equals(
									"bio_variables_dir")) {
								maxent_variables_dir = fi
										.getAttributeValue("path");
							} else {
								if (fi.getAttributeValue("name")
										.equals("sample_dir")) {
									sample_dir = fi.getAttributeValue("path");

								}
							}
						}
					}
				}
			}
		}

		Element variables = config.getChild("variables");
		dirBioclimaticFiles = variables.getAttributeValue(XML_PATH);
		List<Element> vars = (List<Element>) variables.getChildren();
		variablesName = new LinkedHashSet<String>();
		for (Element variable : vars) {
			variablesName.add(variable.getTextTrim());
		}
		
		Element proxy = config.getChild("Proxy");
		if(proxy != null) {
			Element httpProxy = proxy.getChild("HttpProxy");
			if(httpProxy != null) {
				httpProxyServer = httpProxy.getAttributeValue(HTTP_PROXY_SERVER);
				httpProxyPort = httpProxy.getAttributeValue(HTTP_PROXY_PORT);
			}
		}
	}

	private static void createDeafaultXML() throws IOException {
		Element config = new Element("config");
		Element variables = new Element("variables");
		variables.setAttribute("type", "bioclimatic");
		// /media/Lacie_Disk/variables/
		// /media/Lacie_Disk/carlos_maxent/
		variables.setAttribute("path", "/variables/");

		Set<String> var_names = new LinkedHashSet<String>();

		var_names.add("alt");
		var_names.add("bio_1");
		var_names.add("bio_2");
		var_names.add("bio_3");
		var_names.add("bio_4");
		var_names.add("bio_5");
		var_names.add("bio_6");
		var_names.add("bio_7");
		var_names.add("bio_8");
		var_names.add("bio_9");
		var_names.add("bio_10");
		var_names.add("bio_11");
		var_names.add("bio_12");
		var_names.add("bio_13");
		var_names.add("bio_14");
		var_names.add("bio_15");
		var_names.add("bio_16");
		var_names.add("bio_17");
		var_names.add("bio_18");
		var_names.add("bio_19");

		Element variable = new Element("variable");
		for (String var : var_names) {
			variable = new Element("variable");
			variable.setText(var);
			variables.addContent(variable);
		}

		Element maxent = new Element("maxent");
		Element mfile = new Element("file");

		mfile = new Element("file");
		mfile.setAttribute("name", "maxent_file");
		mfile.setAttribute("type", "jar");
		mfile.setAttribute("path", "/maxent.jar");
		maxent.addContent(mfile);
		mfile = new Element("file");
		mfile.setAttribute("name", "sample_dir");
		mfile.setAttribute("type", "directory");
		mfile.setAttribute("path", "/samples/");
		maxent.addContent(mfile);
		mfile = new Element("file");
		mfile.setAttribute("name", "backgroung_file");
		mfile.setAttribute("type", "csv");
		mfile.setAttribute("path", "/background_clm.csv");
		maxent.addContent(mfile);
		mfile = new Element("file");
		mfile.setAttribute("name", "lambda_output_dir");
		mfile.setAttribute("type", "directory");
		mfile.setAttribute("path", "/lambda_output/");
		maxent.addContent(mfile);
		mfile = new Element("file");
		mfile.setAttribute("name", "final_output_dir");
		mfile.setAttribute("type", "directory");
		mfile.setAttribute("path", "/final_output/");
		maxent.addContent(mfile);
		mfile = new Element("file");
		mfile.setAttribute("name", "bio_variables_dir");
		mfile.setAttribute("type", "directory");
		mfile.setAttribute("path", "/small_variables/");
		maxent.addContent(mfile);

		Element server = new Element("server");
		server.setAttribute(XML_IP_ADDR, "localhost");
		server.setAttribute(XML_COMMUNICATION_TYPE, OBJECT_COMMUNICATION);

		Element shape = new Element("shape");
		// /home/danipilze/shapes/Global_high_level/GADM_v0-6.shp
		shape.setAttribute(XML_PATH, "/GADM_v0-6.shp");

		Element columnISO = new Element("column");
		columnISO.setAttribute(XML_NAME, XML_ISO);
		columnISO.setText("ISO");
		Element columnCountry = new Element("column");
		columnCountry.setAttribute(XML_NAME, XML_COUNTRY);
		columnCountry.setText("ISOCOUNTRY");
		Element columnState = new Element("column");
		columnState.setAttribute(XML_NAME, XML_STATE);
		columnState.setText("NAME_1");
		Element columnProvince = new Element("column");
		columnProvince.setAttribute(XML_NAME, XML_COUNTY);
		columnProvince.setText("NAME_2");

		shape.addContent(columnISO);
		shape.addContent(columnCountry);
		shape.addContent(columnState);
		shape.addContent(columnProvince);

		Element mask = new Element("mask");
		// /home/danipilze/mask/inland_msk.jgm
		mask.setAttribute(XML_PATH, "/inland_msk.jgm");

		Element work = new Element("work");
		work.setAttribute(XML_RECORDS, "10000");	
		
		Element proxy = new Element("Proxy");
		Element httpProxy = new Element("HttpProxy");
		proxy.addContent(httpProxy);
		httpProxy.setAttribute(HTTP_PROXY_SERVER, "proxy4.ciat.cgiar.org");
		httpProxy.setAttribute(HTTP_PROXY_PORT, "8080");
		
		config.addContent(server);
		config.addContent(proxy);
		config.addContent(work);
		config.addContent(shape);
		config.addContent(mask);
		config.addContent(maxent);
		config.addContent(variables);

		Document doc = new Document(config);

		XMLOutputter outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());
		FileOutputStream file = new FileOutputStream(XML_FILE);
		outp.output(doc, file);

		file.flush();
		file.close();

	}

}
