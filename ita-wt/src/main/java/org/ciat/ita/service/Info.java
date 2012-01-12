package org.ciat.ita.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Info extends HttpServlet {
	public static final Properties prop = new Properties();

	public static Info instance;

	private final String user;
	private final String pass;
	private final String ip;
	private final String port;
	private final String database;
	private final String path;
	private final String publicPath;

	public static Info getInstance() {
		if (instance == null)
			instance = new Info();
		return instance;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		writer.print(getInstance().getPublicPath());
		writer.flush();
		writer.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	public Info() {
		try {
			System.out.println("Loading configuration file..."+new File("ita-wt.properties").getAbsolutePath());
			prop.load(new FileInputStream("ita-wt.properties"));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		user = prop.getProperty("user");
		pass = prop.getProperty("pass");
		ip = prop.getProperty("ip");
		port = prop.getProperty("port");
		database = prop.getProperty("database");
		path = prop.getProperty("path");
		publicPath = prop.getProperty("public_path");


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
	
	public String getPublicPath() {
		return publicPath;
	}
}
