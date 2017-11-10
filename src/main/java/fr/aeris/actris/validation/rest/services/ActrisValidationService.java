package fr.aeris.actris.validation.rest.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Component
@Path("/validation")
@Api(value = "/validation")
public class ActrisValidationService  {

	public ActrisValidationService() {
	}

	
	@Autowired 
	private SensorConfigList configList;
	
	@Autowired
	private ActrisValidationConfig config;
	
	 @Context
	 UriInfo uriInfo;

	private static Logger log = LoggerFactory.getLogger(ActrisValidationService.class);

	@GET
	@Path("/isalive")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Tests the availability of the whole service")
	public Response isAlive() {
		String answer = "Yes";
		return Response.status(Response.Status.OK).entity(answer).build();
	}

	@GET
	@Path("/daily")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFlagByUuid(@QueryParam("uuid") String uuid, 
			@QueryParam("day")  String dayDate
			) {
		String result = null;
		try {
			
		SensorConfig sensorConfig = configList.findByUuid(uuid);
		
		if (sensorConfig != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String aux = uriInfo.getBaseUri().toString();
			int index=aux.lastIndexOf('/');
			aux = aux.substring(0,index);
			index=aux.lastIndexOf('/');
			
			String rootUrl = aux.substring(0,index);
			result = sensorConfig.generate(format.parse(dayDate), rootUrl);
		}
		
		}
		catch (Exception e) {
			
		}
		if (result != null) {
			return Response.status(Response.Status.OK).entity(result).build();
		}
		else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("").build();
		}
	}
	
	@GET
	@Path("/dailydetail")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInstrumentDetail(@QueryParam("uuid") String uuid, @QueryParam("day")  String dayDate) {
		String result = null;
		try {
			
		SensorConfig sensorConfig = configList.findByUuid(uuid);
		
		if (sensorConfig != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String aux = uriInfo.getBaseUri().toString();
			int index=aux.lastIndexOf('/');
			aux = aux.substring(0,index);
			index=aux.lastIndexOf('/');
			
			String rootUrl = aux.substring(0,index);
			result = sensorConfig.generatedailydetail(format.parse(dayDate), rootUrl);
		}
		
		}
		catch (Exception e) {
			
		}
		if (result != null) {
			return Response.status(Response.Status.OK).entity(result).build();
		}
		else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("").build();
		}
	}

	private void fetchFeed(InputStream is, File tmpFile) throws Exception {
		byte[] byteArray = IOUtils.toByteArray(is);
		FileOutputStream fos = new FileOutputStream(tmpFile);
		fos.write(byteArray);
		fos.flush();
		fos.close();
	}




}
