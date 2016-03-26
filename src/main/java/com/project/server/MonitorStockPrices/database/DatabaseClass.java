package com.project.server.MonitorStockPrices.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.project.server.MonitorStockPrices.service.StockService;

/**
 * Helper class to get the database connection object.
 * It reads database name, user name and password from properties file
 * @author ankita
 */
public class DatabaseClass {
	final static Logger logger = Logger.getLogger(DatabaseClass.class);
	/**
	 * Get the Database connection object
	 */
	public static Connection getConnection(Properties properties){
		try {
			
			String dbName = properties.getProperty("database");
			String dbUser = properties.getProperty("dbuser");
			String dbPassword = properties.getProperty("dbpassword");

			String connectionURL = "jdbc:mysql://localhost:3306/"+dbName;
			Connection connection = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, dbUser,dbPassword);
			return connection;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	

}
