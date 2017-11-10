package fr.aeris.actris.validation.rest.dao;

import java.util.List;

public interface AdminDao {
	
	List<String> getUuidByOrcid(String orcid);

}
