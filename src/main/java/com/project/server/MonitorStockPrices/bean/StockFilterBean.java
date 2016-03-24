package com.project.server.MonitorStockPrices.bean;

import javax.ws.rs.QueryParam;

public class StockFilterBean {
	private @QueryParam("startDate") long startDate;
	private @QueryParam("endDate") long endDate;
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	
}
