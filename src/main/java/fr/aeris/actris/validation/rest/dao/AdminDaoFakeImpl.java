package fr.aeris.actris.validation.rest.dao;

import java.util.ArrayList;
import java.util.List;

import fr.aeris.actris.validation.rest.services.config.ActrisConstants;

public class AdminDaoFakeImpl implements AdminDao, ActrisConstants{

	@Override
	public List<String> getUuidByOrcid(String orcid) {
		List<String> result = new ArrayList<>();
		result.add(NEPEHELO_PIC_DU_MIDI_UUID);
		return result;
	}

}
