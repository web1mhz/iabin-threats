package org.ciat.ita.maps.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
	
	private Properties properties;
	
	private static PropertiesManager instance;

	public static PropertiesManager getInstance() {
		if (instance == null)
			throw new RuntimeException("Instance has not been initialized");
		
		return instance;
	}

	public static void register(String propertiesPath) {
		if (instance != null)
			throw new RuntimeException("Instance has already been initialized");

		instance = new PropertiesManager(propertiesPath);
	}

	private PropertiesManager(String propertiesPath) {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(propertiesPath));
		} catch (IOException e) {
			System.out.println("The indicated file has not been found, file needed: \"iabin.properties\"");
			e.getLocalizedMessage();
			System.exit(-1);
		}
	}
	
	public boolean existProperty(String name) {
		return properties.get(name) != null;
	}
	
	public String getPropertiesAsString(String name) {
		return properties.getProperty(name);
	}
	
	public int getPropertiesAsInt(String name) {
		return Integer.parseInt(getPropertiesAsString(name));
	}
	
	public String[] getPropertiesAsStringArray(String name) {
		return getPropertiesAsString(name).split(";");
	}
	
	public int[] getPropertiesAsIntArray(String name) {
		String[] str = getPropertiesAsString(name).split(";");
		int[] array = new int[str.length];
		for(int i = 0; i<str.length;i++)
			array[i] = Integer.parseInt(str[i]);
		
		return array;
	}
	
	public float[] getPropertiesAsFloatArray(String name) {
		String[] str = getPropertiesAsString(name).split(";");
		float[] array = new float[str.length];
		for(int i = 0; i<str.length;i++)
			array[i] = Float.parseFloat(str[i]);
		
		return array;
	}
	
	public float[] getPropertiesAsRGB(String name) {
		float[] rgb = getPropertiesAsFloatArray(name);
		
		if(rgb.length != 3)
			throw new RuntimeException("Invalid RGB values in variable "+name);
		
		return rgb;
	}

	public float getPropertiesAsFloat(String name) {
		return Float.parseFloat(getPropertiesAsString(name));
	}
}
