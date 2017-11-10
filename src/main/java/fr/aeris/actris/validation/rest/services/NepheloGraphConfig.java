package fr.aeris.actris.validation.rest.services;
import java.util.Date;

import fr.aeris.actris.validation.rest.services.GraphConfig;

public class NepheloGraphConfig extends GraphConfig{
	
	private String timeXmin;
	private String timeXmax;
	
	public String getTimeXmin() {
		return timeXmin;
	}

	public void setTimeXmin(String timeXmin) {
		this.timeXmin = timeXmin;
	}

	public String getTimeXmax() {
		return timeXmax;
	}

	public void setTimeXmax(String timeXmax) {
		this.timeXmax = timeXmax;
	}

	@Override
	public String generate(Date dayDate, String rootUrl) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
