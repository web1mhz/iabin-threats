package org.ciat.ita.service;

public class Info {

	private static String user = "guest";
	private static String pass = "cajanus";
	private static String ip = "gisbif.ciat.cgiar.org";
	private static String port = "3306";
	private static String database = "gbif_sept2010";
	private static String path = "";
	
	
	public static String getUser() {
		return user;
	} 
	public static String getPass() {
		return pass;
	}
	public static String getIp() {
		return ip;
	}
	public static String getPort() {
		return port;
	}
	public static String getDatabase() {
		return database;
	}
	public static String getPath() {
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			return "\\\\172.22.33.85\\geodata\\Threat-Assement\\species\\";
		} else {
			return "/mnt/HD724-geodata/iabin-threats/ITA/generated-files/species/";
		}
	}	
	
}
