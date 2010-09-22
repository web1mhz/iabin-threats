package server;

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

public class ServerConfig {

	private static final String XML_FILE = "server_config.xml";
	private static final String XML_UNVERIFIED_RECORDS = "unverified_records";
	private static final String XML_UNRELIBLE_RECORDS = "unrelible_records";
	private static final String XML_GOOD_RECORDS = "good_records";
	private static final String XML_FINAL_RECORDS = "final_records";
	private static final String XML_IP_ADDR = "ip_addr";
	private static final String XML_PORT = "port";
	private static final String XML_NAME = "name";
	private static final String XML_COLUMN = "column";
	private static final String XML_CONTENT = "content";

	/**
	 * login of the user of th database
	 */
	public static String database_user;
	/**
	 * password for the user login of the database
	 */
	public static String database_password;
	/**
	 * name of the table in which the good records are inserted
	 */
	public static String dbTableGoods;
	/**
	 * name of the table in which the unreliable records are inserted
	 */
	public static String dbTableUnreliable;
	/**
	 * name of the table of the totally filtered records
	 */
	public static String dbTableFinalRecords;
	/**
	 * name of the table of the records to work 
	 */
	public static String dbTableRecords;
	/**
	 * ip address of the database host
	 */
	public static String dbIPAddress;
	/**
	 * network port to connect to the database
	 */
	public static String database_port;
	/**
	 * name of the database
	 */
	public static String database_name;
	/**
	 * names of the columns of the environmental variables data
	 */
	public static Set<String> dbVariablesName;
	/**
	 * name of the of the counter of outlier
	 */
	public static String dbOutlierCount;
	/**
	 * network port to connect through RMI
	 */
	public static int rmi_port;

	/**
	 * Initialize the variables of the server configuration. It must be called
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

	@SuppressWarnings("unchecked")
	private static void readFromXML() throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder(false);
		Document doc = builder.build(new File(XML_FILE));
		Element config = doc.getRootElement();

		Element rmi = config.getChild("rmi");
		rmi_port = Integer.parseInt(rmi.getAttributeValue(XML_PORT));

		Element database = config.getChild("database");
		dbIPAddress = database.getAttributeValue(XML_IP_ADDR);
		database_name = database.getAttributeValue(XML_NAME);
		database_port = database.getAttributeValue(XML_PORT);
		database_user = database.getChild("user").getTextTrim();
		database_password = database.getChild("password").getTextTrim();
		List<Element> tables = (List<Element>) database.getChild("tables")
				.getChildren();
		for (Element table : tables) {

			String content = table.getAttribute("content").getValue();
			if (content.equalsIgnoreCase(XML_FINAL_RECORDS)) {
				dbTableFinalRecords = table.getTextTrim();
			} else {
				if (content.equalsIgnoreCase(XML_UNVERIFIED_RECORDS)) {
					dbTableRecords = table.getTextTrim();
				} else {
					if (content.equalsIgnoreCase(XML_UNRELIBLE_RECORDS)) {
						dbTableUnreliable = table.getTextTrim();
					} else {
						if (content.equalsIgnoreCase(XML_GOOD_RECORDS)) {
							dbTableGoods = table.getTextTrim();
							List<Element> vars = (List<Element>) table
									.getChildren();
							dbVariablesName = new LinkedHashSet<String>();
							for (Element variable : vars) {
								if (variable.getAttributeValue(XML_CONTENT)
										.equalsIgnoreCase("variable")) {
									dbVariablesName.add(variable.getTextTrim());
								} else {
									if (variable.getAttributeValue(XML_CONTENT)
											.equalsIgnoreCase("outlier_count")) {
										dbOutlierCount = variable.getTextTrim();
									}
								}
							}
						} else {
							// unknow table;
						}
					}
				}

			}
		}

	}

	public static void main(String[] args) {
		init();
	}

	private static void createDeafaultXML() throws JDOMException, IOException {

		Element config = new Element("config");

		Element rmi = new Element("rmi");
		rmi.setAttribute(XML_PORT, "10999");

		Element database = new Element("database");
		// portal_gbif
		database.setAttribute(XML_NAME, "portal");
		database.setAttribute(XML_IP_ADDR, "localhost");
		database.setAttribute(XML_PORT, "3306");
		Element user = new Element("user");
		user.setAttribute("name", "server");
		user.setText("db_user");
		Element password = new Element("password");
		password.setAttribute("user", "server");
		// TODO encriptar clave
		password.setText("123456");
		Element tables = new Element("tables");
		Element records = new Element("table");
		records.setAttribute("content", XML_UNVERIFIED_RECORDS);
		records.setText("temp_land_5A");
		Element good = new Element("table");
		good.setAttribute("content", XML_GOOD_RECORDS);
		good.setText("temp_good_records");
		Element finalrecords = new Element("table");
		finalrecords .setAttribute("content", XML_FINAL_RECORDS);
		finalrecords .setText("filtered_records");
		Element unreliable = new Element("table");
		unreliable.setAttribute("content", XML_UNRELIBLE_RECORDS);
		unreliable.setText("temp_bad_records");

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

		Element column = new Element(XML_COLUMN);
		for (String var : var_names) {
			column = new Element(XML_COLUMN);
			column.setAttribute(XML_CONTENT, "variable");
			column.setText(var);
			good.addContent(column);
		}

		column = new Element(XML_COLUMN);
		column.setAttribute(XML_CONTENT, "outlier_count");
		column.setText("outlier");
		good.addContent(column);

		database.addContent(user);
		database.addContent(password);
		tables.addContent(records);
		tables.addContent(good);
		tables.addContent(finalrecords);
		tables.addContent(unreliable);
		database.addContent(tables);
		config.addContent(rmi);
		config.addContent(database);
		Document doc = new Document(config);

		XMLOutputter outp = new XMLOutputter();
		outp.setFormat(Format.getPrettyFormat());
		FileOutputStream file = new FileOutputStream(XML_FILE);
		outp.output(doc, file);

		file.flush();
		file.close();
		// outp.output(doc, System.out);
	}

}
