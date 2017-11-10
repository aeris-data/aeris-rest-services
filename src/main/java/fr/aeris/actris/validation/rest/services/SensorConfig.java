package fr.aeris.actris.validation.rest.services;

import java.util.Date;

public abstract class SensorConfig {

	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public abstract String generate(Date dayDate, String rootUrl);

	public abstract String generatedailydetail(Date parse, String rootUrl);
	
}
