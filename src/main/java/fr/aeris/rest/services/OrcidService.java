package fr.aeris.rest.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
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
		    .expireAfterWrite(100, TimeUnit.MINUTES)
		    .build(new CacheLoader<String, String>() {
		        @Override
		        public String load(String orcid){
		        	try {
		        		return getNameFromOrcid(orcid);
		        	}
		        	catch (Exception e) {
		        		e.printStackTrace();
		        		return "";
		        	}
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
	
	
	private static String getNameFromOrcid(String orcid) {
	try {
	OrcidPublicClient client = new OrcidPublicClient();
	OrcidProfile bio = client.getOrcidBio(orcid);
	if ((bio != null) && (bio.getOrcidBio() != null)) {
		String result = bio.getOrcidBio().getPersonalDetails().getGivenNames()+" "+bio.getOrcidBio().getPersonalDetails().getFamilyName();
		return result;	
	}
	else {
		return "";
		}
	
	}
	catch (Exception e) {
		return "";
	}
	}
	
		    
	
	@GET
	@Path("/name/{orcid}")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Compute name from Orcid")
	public Response nameFromOrcid(@PathParam("orcid") String orcid) {
		try {
			//return Response.status(Response.Status.OK).entity(StringUtils.trimToEmpty(cache.get(orcid))).build();
			return Response.status(Response.Status.OK).entity(StringUtils.trimToEmpty(getNameFromOrcid(orcid))).build();
		}
		catch (Exception e) {
			return Response.status(Response.Status.OK).entity("").build();
		}
	}
	
	
	


}
