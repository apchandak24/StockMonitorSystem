package com.project.server.MonitorStockPrices.model;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class StockModel {
	
	private String name;
	private String symbol;
	private double day_high;
	private double day_low;
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
	public double getDay_high() {
		return day_high;
	}
	public void setDay_high(double day_high) {
		this.day_high = day_high;
	}
	public double getDay_low() {
		return day_low;
	}
	public void setDay_low(double day_low) {
		this.day_low = day_low;
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
		return name+" "+price+" "+day_high+" "+day_low+" "+new Date(ts*1000);
	}
	

}
