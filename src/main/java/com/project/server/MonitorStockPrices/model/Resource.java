package com.project.server.MonitorStockPrices.model;

/**
 * StockModel is a field in Json response from YAHOO web services so class is used for direct mapping
 * @author ankita
 *
 */

public class Resource {
	
	private StockModel fields;
	
	public StockModel getFields() {
		return fields;
	}

	public void setFields(StockModel fields) {
		this.fields = fields;
	}
	@Override
	public String toString() {
		return fields.toString();
	}

}
