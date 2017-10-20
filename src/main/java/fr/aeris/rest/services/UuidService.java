package fr.aeris.rest.services;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aeris.rest.services.uuid.UuidUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/uuid")
@Api(value = "/uuid")
public class UuidService {

	Logger logger = LoggerFactory.getLogger(UuidService.class);

	public Response isAlive() {
		logger.debug("Debut traitement isAlive");
		String answer = "Yes";
		return Response.status(200).entity(answer).build();
	}

	@GET
	@Path("random")
	@ApiOperation(value = "Générate random uuid")
	public Response random() {
		return Response.status(200).entity(String.valueOf(UUID.randomUUID())).build();
	}
	
	@GET
	@Path("fromstring")
	@ApiOperation(value = "Générate uuid from string")
	public Response fromstring(@QueryParam("id") String id ) {
		return Response.status(200).entity(String.valueOf(UuidUtils.getUuidFromText(id))).build();
	}

}
