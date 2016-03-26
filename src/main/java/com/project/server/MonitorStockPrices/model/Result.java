package com.project.server.MonitorStockPrices.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result {

	private boolean result;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	
	
	
}
