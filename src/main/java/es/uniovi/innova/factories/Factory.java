package main.java.es.uniovi.innova.factories;

import main.java.es.uniovi.innova.services.ga.IGAService;
import main.java.es.uniovi.innova.services.ga.IPortalesService;

public class Factory {

	private IGAService serviceGoogleAnalyticsNewData;
	private IGAService serviceGoogleAnalyticsOldData;
	private IPortalesService servicePortales;

	public IGAService getServiceGoogleAnalyticsNewData() {
		return serviceGoogleAnalyticsNewData;
	}

	public void setServiceGoogleAnalyticsNewData(
			IGAService serviceGoogleAnalyticsNewData) {
		this.serviceGoogleAnalyticsNewData = serviceGoogleAnalyticsNewData;
	}

	public IGAService getServiceGoogleAnalyticsOldData() {
		return serviceGoogleAnalyticsOldData;
	}

	public void setServiceGoogleAnalyticsOldData(
			IGAService serviceGoogleAnalyticsOldData) {
		this.serviceGoogleAnalyticsOldData = serviceGoogleAnalyticsOldData;
	}

	public IPortalesService getServicePortales() {
		return servicePortales;
	}

	public void setServicePortales(IPortalesService servicePortales) {
		this.servicePortales = servicePortales;
	}



	
	
	
}
