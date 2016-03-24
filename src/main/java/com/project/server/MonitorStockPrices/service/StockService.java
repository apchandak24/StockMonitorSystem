package com.project.server.MonitorStockPrices.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.project.server.MonitorStockPrices.database.DatabaseClass;
import com.project.server.MonitorStockPrices.database.DatabaseService;
import com.project.server.MonitorStockPrices.model.StockModel;
import com.project.server.MonitorStockPrices.model.Symbol;

public class StockService {
	
	private HashMap<Symbol, ArrayList<StockModel>> stockMap = DatabaseClass.getStockMap();
	DatabaseService dbService = new DatabaseService();
	
	public ArrayList<Symbol> getSymbolList(){
		return dbService.getSymbolList();
	}
	
	public Symbol addSymbol(Symbol symbol){
		if(stockMap.containsKey(symbol))
			return null;
		stockMap.put(symbol, new ArrayList<StockModel>());
		if(dbService.insertSymbol(symbol)){
			System.out.println("Symbol inserted successfully");
			return symbol;
		}else{
			System.out.println("Symbol already exists");
			return null;
		}
	}

}
