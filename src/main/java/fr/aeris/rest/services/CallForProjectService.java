package fr.aeris.rest.services;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.aeris.commons.metadata.util.json.JsonUtils;
import fr.aeris.rest.services.callforproject.CallForProject;
import fr.aeris.rest.services.callforproject.PrintUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Path("/callforproject")
@Api(value = "/callforproject")
public class CallForProjectService {
	
	private static Logger log = LoggerFactory.getLogger(CallForProjectService.class);
	
	
	private static JsonUtils getJsonUtil() {
		JsonUtils jsonUtils = new JsonUtils("fr.aeris");
		return jsonUtils;
	}
	
	@GET
	@Path("/isalive")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Tests the availability of the whole service")
	public Response isAlive() {
		String answer = "Yes";
		return Response.status(Response.Status.OK).entity(answer).build();
	}
	
	@POST
	@Path("/sendmail") 
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Send mail of the CFP to board and applicant")
	public Response sendMail(@ApiParam(value="Content of the application form as a json string") String body, @DefaultValue("en") @QueryParam("lang") String lang) {
		String result = "ok";
		
		// read body
		ObjectMapper mapper = new ObjectMapper();
		CallForProject callForProjectFromString = new CallForProject();
		try {
			callForProjectFromString = mapper.readValue(body, CallForProject.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("mail: " + callForProjectFromString.getApplicantEmail());
		
		// print it into file
		// TODO ecriture date, logo (où est-il ?); alignement 
		PrintUtils printUtils = new PrintUtils();
		try {
			printUtils.printCallForProject(callForProjectFromString, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// create mail and attach the file
		
		// send
		
		// TODO param texte de réponse
		return Response.ok(result).type(MediaType.TEXT_PLAIN).build();
	}
	
	
}
