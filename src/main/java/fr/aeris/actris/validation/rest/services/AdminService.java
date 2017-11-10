package fr.aeris.actris.validation.rest.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.aeris.actris.validation.rest.dao.AdminDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Component
@Path("/admin")
@Api(value = "/admin")
public class AdminService  {

	public AdminService() {
	}

	@Autowired
	AdminDao adminDao;
	
	@GET
	@Path("/isalive")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Tests the availability of the whole service")
	public Response isAlive() {
		String answer = "Yes";
		return Response.status(Response.Status.OK).entity(answer).build();
	}

	@GET
	@Path("/uuids")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUuidByOrcid(@QueryParam("orcid") String orcid) {
		List<String> uuidByOrcid = adminDao.getUuidByOrcid(orcid);
		if (uuidByOrcid == null) {
			uuidByOrcid = new ArrayList<>();
		}
		return Response.ok(uuidByOrcid).type(MediaType.APPLICATION_JSON).build();
	}

	




}
