package com.project.server.MonitorStockPrices.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.project.server.MonitorStockPrices.database.DatabaseClass;
import com.project.server.MonitorStockPrices.database.DatabaseService;
import com.project.server.MonitorStockPrices.model.StockModel;
import com.project.server.MonitorStockPrices.model.Symbol;

public class StockService {

	DatabaseService dbService = new DatabaseService();

	public ArrayList<Symbol> getSymbolList() {
		Connection dbConnection = DatabaseClass.getConnection(loadPropertiesFile());
		try {
			ArrayList<Symbol> list = dbService.getSymbolList(dbConnection);
			return list;
		} finally {
			if (dbConnection != null)
				try {
					dbConnection.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
		}
	}

	public Symbol addSymbol(Symbol symbol) {
		Connection dbConnection = DatabaseClass.getConnection(loadPropertiesFile());
		try {
			if (dbService.insertSymbol(symbol, dbConnection)) {
				System.out.println("Symbol inserted successfully");
				return symbol;
			} else
				return null;
		} finally {
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

	}

	public ArrayList<StockModel> getSymbolHistory(Symbol symbol) {
		Connection dbConnection = DatabaseClass.getConnection(loadPropertiesFile());
		try {
			return dbService.getStocks(symbol, dbConnection);
		} finally {
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	public ArrayList<StockModel> getSymbolHistoryInRange(Symbol symbol, long startDate, long endDate) {
		Connection dbConnection = DatabaseClass.getConnection(loadPropertiesFile());
		try {
			return dbService.getStocksInRange(symbol, startDate, endDate, dbConnection);
		} finally {
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

	}

	public void deleteSymbol(Symbol symbol) {
		Connection dbConnection = DatabaseClass.getConnection(loadPropertiesFile());
		try {
			dbService.deleteSymbol(symbol, dbConnection);
		} finally {
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}

	}

	private Properties loadPropertiesFile() {
		Properties properties = new Properties();
		try {
			InputStream input = null;
			input = getClass().getClassLoader().getResourceAsStream("config/config.properties");
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

}
