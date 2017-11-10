package fr.aeris.actris.validation.rest.services;

import java.util.ArrayList;

public class SensorConfigList extends ArrayList<SensorConfig> {
	
	public SensorConfig findByUuid(String uuid) {
		for (SensorConfig current : this) {
			if (current.getUuid().equalsIgnoreCase(uuid)) {
				return current;
			}
		}
		return null;
	}

}
