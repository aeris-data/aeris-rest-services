package fr.aeris.rest.services.redirection;

import java.util.ArrayList;

public interface RedirectionDao {
	
	ArrayList<Redirection> findAll() throws Exception;
	
	Redirection save(Redirection redirection) throws Exception;
	
	void delete(Long redirectionId) throws Exception;
	
	String getUrlByAerisSuffix(String aerisSuffix) throws Exception;
	
}
