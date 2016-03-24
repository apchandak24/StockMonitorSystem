package com.project.server.MonitorStockPrices.resources;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.project.server.MonitorStockPrices.model.Symbol;
import com.project.server.MonitorStockPrices.service.StockService;

@Path("/stocks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StockResource {
	private StockService stockService = new StockService();
	@GET
	@Path("/listsymbols")
	public ArrayList<Symbol> getSymbolList(){
		try{
		
		ArrayList<Symbol> list = stockService.getSymbolList();
		//list.add(new Symbol("hello"));
		return list;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@POST
	public Symbol addSymbol(Symbol symbol){
		return stockService.addSymbol(symbol);
	}
	
}
