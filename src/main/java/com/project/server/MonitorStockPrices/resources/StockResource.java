package com.project.server.MonitorStockPrices.resources;

import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.project.server.MonitorStockPrices.bean.StockFilterBean;
import com.project.server.MonitorStockPrices.model.StockModel;
import com.project.server.MonitorStockPrices.model.Symbol;
import com.project.server.MonitorStockPrices.service.StockService;

@Path("/stocks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StockResource {
	private StockService stockService = new StockService();

	@GET
	@Path("/listsymbols")
	public ArrayList<Symbol> getSymbolList() {
		try {
			ArrayList<Symbol> list = stockService.getSymbolList();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@POST
	public Symbol addSymbol(Symbol symbol) {
		if (!symbol.getSymbol().isEmpty())
			return stockService.addSymbol(symbol);
		else
			return null;
	}

	@GET
	@Path("/history/{symbol}")
	public ArrayList<StockModel> getStockHistory(@PathParam("symbol") Symbol symbol, @BeanParam StockFilterBean bean) {
		if (bean.getStartDate() == 0 || bean.getEndDate() == 0)
			return stockService.getSymbolHistory(symbol);
		else
			return stockService.getSymbolHistoryInRange(symbol, bean.getStartDate(), bean.getEndDate());
	}

	@DELETE
	@Path("/{symbol}")
	public void deleteSymbol(@PathParam("symbol") Symbol symbol) {
		stockService.deleteSymbol(symbol);
	}

}
