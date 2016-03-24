package com.project.server.MonitorStockPrices.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.project.server.MonitorStockPrices.model.StockModel;
import com.project.server.MonitorStockPrices.model.Symbol;

public class DatabaseService {
	private static final String SYMBOL_TABLE_NAME = "symbol";
	private static final String DATABASE_NAME = "stock";
	private static final String STOCKS_TABLE_NAME = "stocks";
	public DatabaseService() {
	}

	public boolean insertSymbol(Symbol symbol,Connection dbConnection){
		createSymbolTable(dbConnection);
		try {
			String query = "INSERT INTO " + SYMBOL_TABLE_NAME + " (symbol) VALUES" + "(?)";
			PreparedStatement stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, symbol.getSymbol());
			stmt.executeUpdate();
			return true;
		} catch(Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}

	public ArrayList<Symbol> getSymbolList(Connection dbConnection) {
		ArrayList<Symbol> symbols = new ArrayList<>();
		try {
			Statement stmt = dbConnection.createStatement();
			String query = "SELECT * from " + SYMBOL_TABLE_NAME;
			ResultSet result = stmt.executeQuery(query);
			while (result.next()) {
				Symbol sym = new Symbol(result.getString("symbol"));
				symbols.add(sym);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return symbols;
	}

	private void createSymbolTable(Connection dbConnection) {
		try {
			Statement stmt = dbConnection.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS " + SYMBOL_TABLE_NAME + " (id INTEGER NOT NULL AUTO_INCREMENT, "
					+ " symbol VARCHAR(255) NOT NULL UNIQUE, " + " PRIMARY KEY ( id ))";

			stmt.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public ArrayList<StockModel> getStocks(Symbol s,Connection dbConnection) {
		ArrayList<StockModel> stocks = new ArrayList<>();
		int symId = getSymbolID(s.getSymbol(),dbConnection);
		if (symId > 0) {
			try {
				String query = "SELECT * from " + STOCKS_TABLE_NAME + " where symbolid = ?";
				PreparedStatement stmt = dbConnection.prepareStatement(query);
				stmt.setInt(1, symId);
				ResultSet result = stmt.executeQuery();
				while (result.next()) {
					StockModel stock = new StockModel();
					stock.setName(result.getString("name"));
					stock.setPrice(result.getDouble("price"));
					stock.setSymbol(s.getSymbol());
					stock.setVolume(result.getLong("volume"));
					stock.setTs(result.getLong("timestamp"));
					stocks.add(stock);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return stocks;
	}

	public ArrayList<StockModel> getStocksInRange(Symbol s, long start, long end,Connection dbConnection) {
		ArrayList<StockModel> stocks = new ArrayList<>();
		Date startDate = new Date(start);
		Date endDate = new Date(end);
		int symId = getSymbolID(s.getSymbol(),dbConnection);
		if (symId > 0) {
			try {
				String query = "SELECT * from " + STOCKS_TABLE_NAME + " where symbolid = ?";
				PreparedStatement stmt = dbConnection.prepareStatement(query);
				stmt.setInt(1, symId);
				ResultSet result = stmt.executeQuery();
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
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return stocks;
	}

	private int getSymbolID(String symbol,Connection dbConnection) {
		int id = -1;
		try {
			String query = "SELECT id from " + SYMBOL_TABLE_NAME + " where symbol LIKE ?";
			PreparedStatement stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, symbol);
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				id = result.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return id;
	}

	
	public  void deleteSymbol(Symbol symbol,Connection dbConnection){
		PreparedStatement preparedStatement = null;

		String query = "DELETE from symbol WHERE symbol LIKE ?";
		try {
			preparedStatement = dbConnection.prepareStatement(query);
			preparedStatement.setString(1, symbol.getSymbol());
			preparedStatement.executeUpdate();
			
			System.out.println("Record is deleted!");
			
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} 
	}

}
