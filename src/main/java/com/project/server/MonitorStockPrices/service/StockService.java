package com.project.server.MonitorStockPrices.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.project.server.MonitorStockPrices.HttpRequest.GetStockPrice;
import com.project.server.MonitorStockPrices.database.DatabaseClass;
import com.project.server.MonitorStockPrices.database.DatabaseService;
import com.project.server.MonitorStockPrices.database.PropertiesInstance;
import com.project.server.MonitorStockPrices.model.Resources;
import com.project.server.MonitorStockPrices.model.StockModel;
import com.project.server.MonitorStockPrices.model.Symbol;

/**
 * Service class to carry out all the tasks using the information provided in
 * web service. The class is creating Database connection instance each time a
 * request is made and closed at the end of operation.
 * 
 * @author ankita
 *
 */
public class StockService {

	DatabaseService dbService = new DatabaseService();
	final static Logger logger = Logger.getLogger(StockService.class);
	/**
	 * get the list of existing symbols added by user from database
	 * 
	 * @return ArrayList<Symbol>
	 */
	public ArrayList<Symbol> getSymbolList() {
		Connection dbConnection = DatabaseClass.getConnection(PropertiesInstance.getInstance().getProperties());
		try {
			ArrayList<Symbol> list = dbService.getSymbolList(dbConnection);
			return list;
		} finally {
			if (dbConnection != null)
				try {
					dbConnection.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
		}
	}

	/**
	 * Add a new symbol to database. Before adding the symbol the function
	 * checks if it is a valid symbol by making HTTP request to YAHOO web
	 * service. If it is valid and is not a duplicate entry then it is added to
	 * database.
	 * 
	 * @param symbol
	 * @return
	 */
	public Symbol addSymbol(Symbol symbol) {
		Connection dbConnection = DatabaseClass.getConnection(PropertiesInstance.getInstance().getProperties());
		try {
			GetStockPrice getPrice = new GetStockPrice();
			ArrayList<Resources> stocks = getPrice.getStockPrices(symbol.getSymbol());
			if (stocks.size() != 0) {
				if (dbService.insertSymbol(symbol, dbConnection)) {
					for (Resources s : stocks) {
						dbService.insertStockData(s.getResource().getFields(), dbConnection);
					}
					return symbol;
				} else
					return null;
			} else
				return null;

		} finally {
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
		}

	}

	/**
	 * Get the complete stock data available in database for a particular symbol
	 * 
	 * @param symbol
	 * @return ArrayList<StockModel>
	 */

	public ArrayList<StockModel> getSymbolHistory(Symbol symbol) {
		Connection dbConnection = DatabaseClass.getConnection(PropertiesInstance.getInstance().getProperties());
		try {
			return dbService.getStocks(symbol, dbConnection);
		} finally {
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * Get the stock data for a symbol between specified dates
	 * 
	 * @param symbol
	 * @param startDate
	 *            long
	 * @param endDate
	 *            long
	 * @return ArrayList<StockModel>
	 */
	public ArrayList<StockModel> getSymbolHistoryInRange(Symbol symbol, long startDate, long endDate) {
		Connection dbConnection = DatabaseClass.getConnection(PropertiesInstance.getInstance().getProperties());
		try {
			return dbService.getStocksInRange(symbol, startDate, endDate, dbConnection);
		} finally {
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
		}

	}

	/**
	 * Delete a particular symbol from database, it also deletes the
	 * corresponding stock data from stocks table
	 * 
	 * @param symbol
	 */
	public boolean deleteSymbol(Symbol symbol) {
		Connection dbConnection = DatabaseClass.getConnection(PropertiesInstance.getInstance().getProperties());
		try {
			return dbService.deleteSymbol(symbol, dbConnection);
		} finally {
			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
		}

	}
}
