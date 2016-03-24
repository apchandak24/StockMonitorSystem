package com.project.server.MonitorStockPrices.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.project.server.MonitorStockPrices.model.StockModel;
import com.project.server.MonitorStockPrices.model.Symbol;

public class DatabaseService {
	private static  final String SYMBOL_TABLE_NAME = "symbol"; 
	private static final String DATABASE_NAME = "stock";
	private static final String STOCKS_TABLE_NAME ="stocks";
	Connection dbConnection;
	public DatabaseService() {
		try {
			dbConnection = new DatabaseClass().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean insertSymbol(Symbol symbol){
		createSymbolTable();
		try {
			String query = "INSERT INTO "+SYMBOL_TABLE_NAME
					+ " (symbol) VALUES"
					+ "(?)";
			PreparedStatement stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, symbol.getSymbol());
			stmt.executeUpdate();
			return true;
		}catch(MySQLIntegrityConstraintViolationException e){
			return false;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<Symbol> getSymbolList(){
		ArrayList<Symbol> symbols = new ArrayList<>();
		try {
			Statement stmt = dbConnection.createStatement();
			String query = "SELECT * from "+ SYMBOL_TABLE_NAME ;
			ResultSet result = stmt.executeQuery(query);
			while(result.next()){
				Symbol sym = new Symbol(result.getString("symbol"));
				symbols.add(sym);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return symbols;
	}
	
	private void createSymbolTable(){
		try {
			Statement stmt = dbConnection.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS "+ SYMBOL_TABLE_NAME + 
	                   " (id INTEGER NOT NULL AUTO_INCREMENT, " +
	                   " symbol VARCHAR(255) NOT NULL UNIQUE, " + 
	                   " PRIMARY KEY ( id ))"; 

	      stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	     
	}
	public ArrayList<StockModel> getStocks(Symbol s){
		ArrayList<StockModel> stocks = new ArrayList<>();
		try {
			Statement stmt = dbConnection.createStatement();
			String query = "SELECT * from "+ STOCKS_TABLE_NAME +" where symbol LIKE '"+s.getSymbol() +"'";
			ResultSet result = stmt.executeQuery(query);
			while(result.next()){
				StockModel stock = new StockModel();
				stock.setName(result.getString("name"));
				stock.setPrice(result.getDouble("price"));
				stock.setSymbol(result.getString("symbol"));
				stock.setVolume(result.getLong("volume"));
				stock.setTs(result.getLong("timestamp"));
				stocks.add(stock);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stocks;
	}
	
	
	private void createDatabase(){
		try {
			Statement stmt = dbConnection.createStatement();
			String query = "CREATE DATABASE IF NOT EXISTS "+ DATABASE_NAME; 

	      stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
