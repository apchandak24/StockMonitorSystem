package com.project.server.MonitorStockPrices.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.project.server.MonitorStockPrices.model.StockModel;
import com.project.server.MonitorStockPrices.model.Symbol;

/**
 * A service class to carry out CRUD operations on stocks and symbol table
 * @author ankita
 */
public class DatabaseService {
	private static final String SYMBOL_TABLE_NAME = "symbol";
	private static final String STOCKS_TABLE_NAME = "stocks";
	final static Logger logger = Logger.getLogger(DatabaseService.class);
	public DatabaseService() {
	}

	/**
	 * Insert the symbol into database Returns true if symbol name is unique
	 * 
	 * @param symbol
	 * @param dbConnection
	 * @return boolean
	 */
	public boolean insertSymbol(Symbol symbol, Connection dbConnection) {
		createSymbolTable(dbConnection);
		PreparedStatement stmt=null;
		try {
			String query = "INSERT INTO " + SYMBOL_TABLE_NAME + " (symbol) VALUES" + "(?)";
			stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, symbol.getSymbol());
			stmt.executeUpdate();
			logger.info("Symbol inserted "+symbol.getSymbol());
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}finally{
			if(stmt!=null)
				try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
	}

	/**
	 * Get the list of existing symbols from database
	 * 
	 * @param dbConnection
	 * @return ArrayList<Symbol>
	 */
	public ArrayList<Symbol> getSymbolList(Connection dbConnection) {
		ArrayList<Symbol> symbols = new ArrayList<>();
		Statement stmt=null;
		ResultSet result=null;
		try {
			stmt = dbConnection.createStatement();
			String query = "SELECT * from " + SYMBOL_TABLE_NAME;
			result = stmt.executeQuery(query);
			while (result.next()) {
				Symbol sym = new Symbol(result.getString("symbol"));
				symbols.add(sym);
			}
			logger.info("Get Symbol list "+symbols.toString());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}finally{
			if(stmt!=null)
				try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
			if(result!=null)
				try {result.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return symbols;
	}

	/**
	 * Create the Symbol table if it is not present in database. This can be
	 * avoided if table is already created in data base before the application
	 * starts.
	 * 
	 * @param dbConnection
	 */

	private void createSymbolTable(Connection dbConnection) {
		Statement stmt=null;
		try {
		    stmt = dbConnection.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS " + SYMBOL_TABLE_NAME + " (id INTEGER NOT NULL AUTO_INCREMENT, "
					+ " symbol VARCHAR(255) NOT NULL UNIQUE, " + " PRIMARY KEY ( id ))";

			stmt.executeUpdate(query);
			logger.info("Symbol table created");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}finally{
			if(stmt!=null)
				try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
		}

	}

	/**
	 * Get the complete data for a particular stock. The data is fetched by
	 * stand alone java program
	 * 
	 * @param Symbol
	 *            s
	 * @param dbConnection
	 * @return ArrayList<StockModel>
	 */
	public ArrayList<StockModel> getStocks(Symbol s, Connection dbConnection) {
		ArrayList<StockModel> stocks = new ArrayList<>();
		int symId = getSymbolID(s.getSymbol(), dbConnection);
		PreparedStatement stmt = null;
		ResultSet result=null;
		if (symId > 0) {
			try {
				String query = "SELECT * from " + STOCKS_TABLE_NAME + " where symbolid = ?";
			    stmt = dbConnection.prepareStatement(query);
				stmt.setInt(1, symId);
				 result = stmt.executeQuery();
				while (result.next()) {
					StockModel stock = new StockModel();
					stock.setName(result.getString("name"));
					stock.setPrice(result.getDouble("price"));
					stock.setSymbol(s.getSymbol());
					stock.setVolume(result.getLong("volume"));
					stock.setTs(result.getLong("timestamp"));
					stocks.add(stock);
				}
				logger.info("Retrieved Stock Information "+stocks.toString());
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}finally{
				if(stmt!=null)
					try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
				if(result!=null)
					try {result.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}
		return stocks;
	}

	/**
	 * Get the data for a particular stock with specified start and end time The
	 * start and end time are represented as long values
	 * 
	 * @param Symbol
	 * @param start
	 * @param end
	 * @param dbConnection
	 * @return ArrayList<StockModel>
	 */
	public ArrayList<StockModel> getStocksInRange(Symbol s, long start, long end, Connection dbConnection) {
		ArrayList<StockModel> stocks = new ArrayList<>();
		Date startDate = new Date(start);
		Date endDate = new Date(end);
		int symId = getSymbolID(s.getSymbol(), dbConnection);
		PreparedStatement stmt = null;
		ResultSet result=null;
		if (symId > 0) {
			try {
				String query = "SELECT * from " + STOCKS_TABLE_NAME + " where symbolid = ?";
				stmt = dbConnection.prepareStatement(query);
				stmt.setInt(1, symId);
				result = stmt.executeQuery();
				while (result.next()) {
					Date currentDate = new Date(result.getLong("timestamp") * 1000);
					if (currentDate.equals(startDate) || currentDate.equals(endDate)
							|| (currentDate.after(startDate) && currentDate.before(endDate))) {
						StockModel stock = new StockModel();
						stock.setName(result.getString("name"));
						stock.setPrice(result.getDouble("price"));
						stock.setSymbol(s.getSymbol());
						stock.setVolume(result.getLong("volume"));
						stock.setTs(result.getLong("timestamp"));
						stocks.add(stock);
					}
				}
				logger.info("Retrieved Stock Information in range "+stocks.toString());
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}finally{
				if(stmt!=null)
					try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
				if(result!=null)
					try {result.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}
		return stocks;
	}

	/**
	 * Get the ID of a symbol from database. It is used for internal clustering
	 * 
	 * @param symbol
	 * @param dbConnection
	 * @return
	 */
	private int getSymbolID(String symbol, Connection dbConnection) {
		int id = -1;
		PreparedStatement stmt = null;
		ResultSet result=null;
		try {
			String query = "SELECT id from " + SYMBOL_TABLE_NAME + " where symbol LIKE ?";
			stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, symbol);
			result = stmt.executeQuery();
			while (result.next()) {
				id = result.getInt("id");
			}
			logger.info("retrieve symbol ID");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}finally{
			if(stmt!=null)
				try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
			if(result!=null)
				try {result.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return id;
	}

	/**
	 * Delete a symbol from database, deleting a symbol will also delete the
	 * corresponding history of it from stocks table
	 * 
	 * @param symbol
	 * @param dbConnection
	 */

	public boolean deleteSymbol(Symbol symbol, Connection dbConnection) {
		PreparedStatement stmt = null;

		String query = "DELETE from symbol WHERE symbol LIKE ?";
		try {
			stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, symbol.getSymbol());
			int status = stmt.executeUpdate();
			
			if(status>0){
				logger.info("Symbol deleted");
				return true;
			}
			else{
				logger.info("symbol not present");
				return false;
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;

		}finally{
			if(stmt!=null)
				try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
			
		}
	}
	/**
	 * Insert stock record in stocks table whenever a new symbol is added to database
	 * @param stock
	 * @param dbConnection
	 * @return true if stock data is added
	 */
	public boolean insertStockData(StockModel stock, Connection dbConnection) {
		createStocksTable(dbConnection);
		PreparedStatement stmt = null;

		int id = getSymbolID(stock.getSymbol(), dbConnection);
		if (id != -1) {
			try {
				String query = "INSERT INTO " + STOCKS_TABLE_NAME + " (symbolid, name, price, volume, timestamp) VALUES"
						+ "(?,?,?,?,?)";
		        stmt = dbConnection.prepareStatement(query);
				stmt.setInt(1, id);
				stmt.setString(2, stock.getName());
				stmt.setDouble(3, stock.getPrice());
				stmt.setLong(4, stock.getVolume());
				stmt.setLong(5, stock.getTs());
				stmt.executeUpdate();
				logger.info("stock inserted into database "+stock.toString());
				return true;
			} catch (SQLException exception) {
				logger.error(exception.getMessage());
			}finally{
				if(stmt!=null)
					try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
				
			}
		}
		return false;
	}

	/**
	 * Create the Stocks table if it is not present in database. This can be
	 * avoided if table is already created in data base before the application
	 * starts.
	 * In the table symbol ID from symbol table and time stamp are primary key.
	 * Symbol ID is foreign key referencing ID in symbol table 
	 * @param dbConnection
	 */
	private void createStocksTable(Connection dbConnection) {
		Statement stmt=null;
		try {
			stmt = dbConnection.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS " + STOCKS_TABLE_NAME + " (" + " symbolid INTEGER NOT NULL, "
					+ " name VARCHAR(255), " + " price DOUBLE, volume BIGINT, timestamp BIGINT, "
					+ " PRIMARY KEY (symbolid,timestamp), "
					+ " FOREIGN KEY(symbolid) REFERENCES symbol(id)  ON DELETE CASCADE )";
			stmt.executeUpdate(query);
		} catch (SQLException exception) {
			logger.error(exception.getMessage());
		}finally{
			if(stmt!=null)
				try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
			
		}

	}

}
