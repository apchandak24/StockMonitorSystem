package com.project.server.MonitorStockPrices.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model class containing important data for a stock gathered from web YAHOO
 * services. Out of many fields available in response the application saves name,
 * price, volume, date and time for a particular stock
 * 
 * @author ankita
 *
 */

@XmlRootElement
public class StockModel {

	private String name;
	private String symbol;
	private double price;
	private long ts;
	private long volume;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name + " " + price + " " + new Date(ts * 1000);
	}

}
