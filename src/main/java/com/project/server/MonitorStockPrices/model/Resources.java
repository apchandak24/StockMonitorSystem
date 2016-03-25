package com.project.server.MonitorStockPrices.model;
/**
 * Resource is a field in Json response from YAHOO web services so class is used for direct mapping
 * @author ankita
 *
 */

public class Resources {
	private Resource resource;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return resource.toString();
	}
}
