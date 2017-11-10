package fr.aeris.actris.validation.rest.services;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import fr.sedoo.commons.util.SeparatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Component
@Path("/quality")
@Api(value = "/quality")
public class QualityCodeService  {

	public QualityCodeService() {
		System.out.println("Init QCService");
	}
	
	@Autowired
	private ActrisValidationConfig config;
	
	private static Logger log = LoggerFactory.getLogger(QualityCodeService.class);
	private static String NOT_FOUND = "NOT_FOUND";
	
	private LoadingCache<String,QualityCodeList> cache = CacheBuilder.newBuilder()
		    .maximumSize(100)
		    .expireAfterWrite(10, TimeUnit.MINUTES)
		    .build(new CacheLoader<String, QualityCodeList>() {
		        @Override
		        public QualityCodeList load(String key){
		        	
		        	if (key.compareToIgnoreCase(NOT_FOUND)==0) {
		        		return EbasUtil.getDefaultCodeList();
		        	}
		        	
		        	String[] split = key.split(SeparatorUtil.AROBAS_SEPARATOR);
		        	String instrument = split[0];
		        	String level = split[1];
		        	return EbasUtil.getCodeListByInstrumentAndLevel(instrument, level);
		        }
		    });
	
	private LoadingCache<String,String> metadataCache = CacheBuilder.newBuilder()
		    .maximumSize(100)
		    .expireAfterWrite(10, TimeUnit.MINUTES)
		    .build(new CacheLoader<String, String>() {
		        @Override
		        public String load(String key){
		        	return getInstrumentAndLevelByUuid(key);
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
	@Path("/flags/{uuid}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Ebas quality codes for an uuid")
	public Response getFlagByUuid(@PathParam("uuid") String uuid) {
			QualityCodeList result;
			try {
			String key = metadataCache.get(uuid);
			result = cache.get(key);
			}
			catch (Exception e) {
				result = EbasUtil.getDefaultCodeList();
			}
			return Response.status(Response.Status.OK).entity(result).build();
        
        			
        }
        	
        	
        private String getInstrumentAndLevelByUuid(String uuid) {
        
        	String requestUrl = config.getMetadataService()+"/id/"+uuid;
    		if (config.getMetadataService().endsWith("/")) {
    			requestUrl = config.getMetadataService()+"id/"+uuid;
    		}
    		
    		Client client = null;
            if (requestUrl.toLowerCase().startsWith("https")) {
                    client = ClientBuilder.newBuilder().sslContext(SslUtils.getSslContext()).build();
                }                else {
                    client = ClientBuilder.newClient();
                }

        WebTarget webTarget = client.target(requestUrl);
        Response response = webTarget.request().get();
        if (response.getStatus() != HttpStatus.SC_OK) {
        	return NOT_FOUND;
        }
        
       else {
        	JSONParser parser = new JSONParser();
        	try {
        		Object obj = parser.parse(response.readEntity(String.class));
        		JSONObject jsonObject = (JSONObject) obj;
        		String level = (String) jsonObject.get("dataLevel");
        		System.out.println(level);
        		return EbasConstants.NEPHELOMETER+SeparatorUtil.AROBAS_SEPARATOR+level;
        	}
        	catch (Exception e) {
        		return NOT_FOUND;
        	}
       }
    }
	
	
	@GET
	@Path("/flags/{instrument}/{level}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get Ebas quality codes for an instrument and a specific level")
	public Response getFlag(@PathParam("instrument") String instrument, @PathParam("level") String level) {
		
		String key = instrument+SeparatorUtil.AROBAS_SEPARATOR+level;
		
		QualityCodeList result = null;
		
		try {
			result = cache.get(key);
		}
		catch (Exception e) {
			result = EbasUtil.getDefaultCodeList();
		}
		
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
//	@GET
//	@Path("/versions")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response versions(@ApiParam(name = "component", value = "The name of the component ", required = true) @QueryParam("component") String component) {
//		
//		try {
//			String aux = cache.get(component);
//			String result ="";
//			if (StringUtils.isEmpty(aux)) {
//				result = lastKnownValues.get(component);
//			}
//			else {
//				lastKnownValues.put(component, aux);
//				result = aux;
//			}
//			
//			if (!StringUtils.isEmpty(result)) {
//				return Response.status(Response.Status.OK).entity(result).build();
//			} 
//			else {
//				return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Impossible to compute latest available version for "+component).build();
//			}
//		} catch (Exception e) {
//			throw new WebApplicationException("Impossible to connect to github");			
//		}
//		
//	}
	
	


}
