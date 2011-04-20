package org.ciat.ita.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Info {
	private static final Properties prop = new Properties();

	private static Info instance;

	private final String user;
	private final String pass;
	private final String ip;
	private final String port;
	private final String database;
	private final String path;

	public static Info getInstance() {
		if (instance == null)
			instance = new Info();

		return instance;
	}

	private Info() {
		try {
			prop.load(new FileInputStream("ita-wt.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		user = prop.getProperty("user");
		pass = prop.getProperty("pass");
		ip = prop.getProperty("ip");
		port = prop.getProperty("port");
		database = prop.getProperty("database");
		path = prop.getProperty("path");


	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public String getIp() {
		return ip;
	}

	public String getPort() {
		return port;
	}

	public String getDatabase() {
		return database;
	}

	public String getPath() {
		return path;
	}
}
