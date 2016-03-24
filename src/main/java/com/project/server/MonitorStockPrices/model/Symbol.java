package com.project.server.MonitorStockPrices.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Symbol {
	
	private String symbol;
	
	public Symbol() {
	
	}
	public Symbol(String s){
		this.symbol = s;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	@Override
	public int hashCode() {
		return symbol.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		return super.equals(symbol);
	}
	

}
