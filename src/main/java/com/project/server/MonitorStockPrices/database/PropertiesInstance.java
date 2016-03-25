package com.project.server.MonitorStockPrices.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * Singleton class to load properties file only once
 * @author ankita
 *
 */
public class PropertiesInstance {
	private static PropertiesInstance mProperties;
	private Properties properties;
	private PropertiesInstance(){
	    properties = new Properties();
		try {
			InputStream input = null;
			input = getClass().getClassLoader().getResourceAsStream("config/config.properties");
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PropertiesInstance getInstance(){
		if(mProperties!=null)
			return mProperties;
		else{
			mProperties = new PropertiesInstance();
			return mProperties;
		}
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
