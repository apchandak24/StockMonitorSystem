package com.project.server.MonitorStockPrices.database;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.project.server.MonitorStockPrices.model.StockModel;
import com.project.server.MonitorStockPrices.model.Symbol;

public class DatabaseClass {

	private static HashMap<Symbol, ArrayList<StockModel>> stockMap = new HashMap<>();

	public static HashMap<Symbol, ArrayList<StockModel>> getStockMap() {
		return stockMap;
	}

	public Connection getConnection() throws Exception {
		try {
			Properties properties = new Properties();
			InputStream input = null;
			input = getClass().getClassLoader().getResourceAsStream("config/config.properties");

			properties.load(input);
			
			String dbName = properties.getProperty("database");
			String dbUser = properties.getProperty("dbuser");
			String dbPassword = properties.getProperty("dbpassword");

			String connectionURL = "jdbc:mysql://localhost:3306/"+dbName;
			Connection connection = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, dbUser,dbPassword);
			return connection;
		} catch (Exception e) {
			throw e;
		}
	}

}
