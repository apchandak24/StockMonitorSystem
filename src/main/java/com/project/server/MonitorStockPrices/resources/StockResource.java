package com.project.server.MonitorStockPrices.resources;

import java.util.ArrayList;

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
/**
 * Entry point for the web services
 * @author ankita
 *
 */
@Path("/stocks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StockResource {
	private StockService stockService = new StockService();

	/**
	 * Get request mapping URL /stocks/symbols
	 * It returns the list of symbols
	 * @return
	 */
	@GET
	@Path("/symbols")
	public ArrayList<Symbol> getSymbolList() {
		try {
			ArrayList<Symbol> list = stockService.getSymbolList();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Post request to save symbol into database
	 * it has URL /stocks and the symbol data goes into POST body
	 * @param symbol
	 * @return
	 */

	@POST
	public Symbol addSymbol(Symbol symbol) {
		if (!symbol.getSymbol().isEmpty()){
			Symbol s = stockService.addSymbol(symbol);
			return s;
		}
		else
			return null;
	}
	/**
	 * Get request mapping the URL stocks/history/{symbolname} It returns the complete data
	 * For range query URL is stocks/history/{symbolname}/?startDate=1458841481000&endDate=1458841481000
	 * @param symbol
	 * @param bean
	 * @return
	 */

	@GET
	@Path("/history/{symbol}")
	public ArrayList<StockModel> getStockHistory(@PathParam("symbol") Symbol symbol, @BeanParam StockFilterBean bean) {
		if (bean.getStartDate() == 0 || bean.getEndDate() == 0)
			return stockService.getSymbolHistory(symbol);
		else
			return stockService.getSymbolHistoryInRange(symbol, bean.getStartDate(), bean.getEndDate());
	}
	/**
	 * Delete request to delete a symbol from database
	 * @param symbol
	 */
	@DELETE
	@Path("/{symbol}")
	public void deleteSymbol(@PathParam("symbol") Symbol symbol) {
		stockService.deleteSymbol(symbol);
	}

}
