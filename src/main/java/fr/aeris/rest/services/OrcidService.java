package fr.aeris.rest.services;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import uk.bl.odin.orcid.client.OrcidPublicClient;
import uk.bl.odin.orcid.schema.messages.onepointone.OrcidProfile;



@Path("/orcid")
@Api(value = "/orcid")
public class OrcidService  {

	private static Logger log = LoggerFactory.getLogger(OrcidService.class);
	
	private static Map<String,String> lastKnownValues = new HashMap<>();
	
	private static LoadingCache<String,String> cache = CacheBuilder.newBuilder()
		    .maximumSize(100)
		    .expireAfterWrite(1, TimeUnit.MINUTES)
		    .build(new CacheLoader<String, String>() {
		        @Override
		        public String load(String componentName){
		        	String url = "https://api.github.com/repos/aeris-data/"+componentName+"/tags?fakeparam="+UUID.randomUUID();
		        	String content="";
		        	try {
		        		content = IOUtils.toString(new URL(url),Charset.defaultCharset());
		        	}
		        	catch (Exception e) {
		        		log.warn("Impossible de trouver les versions du composants: "+componentName +" url : "+url + " Exception "+ExceptionUtils.getFullStackTrace(e));
		        	}
		        	return content;
		        }
		    });


	@GET
	@Path("/isalive")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Tests the availability of the whole service")
	public Response isAlive() {
		String answer = "Yes";
		return Response.status(Response.Status.OK).entity(answer).build();
	}
	
	@GET
	@Path("/name/{orcid}")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Compute name from Orcid")
	public Response nameFromOrcid(@PathParam("orcid") String orcid) {
		try {
		OrcidPublicClient client = new OrcidPublicClient();
		OrcidProfile bio = client.getOrcidBio(orcid);
		if ((bio != null) && (bio.getOrcidBio() != null)) {
			String result = bio.getOrcidBio().getPersonalDetails().getGivenNames()+" "+bio.getOrcidBio().getPersonalDetails().getFamilyName();
			return Response.status(Response.Status.OK).entity(StringUtils.trimToEmpty(result)).build();	
		}
		else {
			return Response.status(Response.Status.NOT_FOUND).entity("Not found").build();
		}
		} catch (Exception e) {
			throw new WebApplicationException("Impossible to connect to github"); 
		}
	}
	
	@GET
	@Path("/versions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response versions(@ApiParam(name = "component", value = "The name of the component ", required = true) @QueryParam("component") String component) {
		
		try {
			String aux = cache.get(component);
			String result ="";
			if (StringUtils.isEmpty(aux)) {
				result = lastKnownValues.get(component);
			}
			else {
				lastKnownValues.put(component, aux);
				result = aux;
			}
			
			if (!StringUtils.isEmpty(result)) {
				return Response.status(Response.Status.OK).entity(result).build();
			} 
			else {
				return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Impossible to compute latest available version for "+component).build();
			}
		} catch (Exception e) {
			throw new WebApplicationException("Impossible to connect to github");			
		}
		
	}
	
	


}
