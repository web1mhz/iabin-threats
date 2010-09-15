package utils;

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
			e.printStackTrace();
			System.exit(-1);
		}
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
	public int[] getPropertiesAsRGB(String name) {
		int[] rgb = getPropertiesAsIntArray(name);
		
		if(rgb.length != 3)
			throw new RuntimeException("Invalid RGB values in variable "+name);
		
		return rgb;
	}
}
