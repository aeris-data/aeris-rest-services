package fr.aeris.rest.services;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.aeris.rest.services.redirection.RedirectionDao;
import fr.sedoo.commons.util.StringUtil;

@Path("/redirect")
public class RedirectService {

	Logger logger = LoggerFactory.getLogger(RedirectService.class);
	
	@Autowired
	private RedirectionDao dao;

	private Response isAlive() {
		logger.debug("Début traitement isAlive");
		String answer = "Yes";
		return Response.status(200).entity(answer).build();
	}
	
	
	@GET
	@Path("{aerisSuffix:.+}")
	public Response redirect(@PathParam("aerisSuffix") String aerisSuffix) {
		
		String aux = StringUtil.trimToEmpty(aerisSuffix);
		if (aux.compareToIgnoreCase("isalive")==0) {
			return isAlive();
		}
		
		else {
			try {
				String url = dao.getUrlByAerisSuffix(aerisSuffix);
				
				return Response.temporaryRedirect(new URI(url)).build();

			}
		catch (URISyntaxException e) {
			return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity("Bad redirection").build();
		}
			catch (Exception e) {
				return Response.status(HttpStatus.SC_NOT_FOUND).entity("Not found").build();
		}
		}

	}

}
