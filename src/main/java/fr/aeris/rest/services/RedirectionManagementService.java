package fr.aeris.rest.services;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.aeris.rest.services.redirection.Redirection;
import fr.aeris.rest.services.redirection.RedirectionDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Component
@Path("/redirection")
@Api(value = "/redirection")
public class RedirectionManagementService  {

	@Autowired
	RedirectionDao dao;

	@GET
	@Path("/isalive")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Tests the availability of the whole service")
	public Response isAlive() {
		String answer = "Yes";
		return Response.status(Response.Status.OK).entity(answer).build();
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		ArrayList<Redirection> findAll;
		try {
			findAll = dao.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			findAll = new ArrayList<>();
		}
		if (findAll == null) {
			findAll = new ArrayList<>();
		}
		return Response.status(Response.Status.OK).entity(findAll).build();
	}
	
	@GET
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(Long redirectionId) {
		try {
			dao.delete(redirectionId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException("An error has occured while deleting", Status.INTERNAL_SERVER_ERROR);
		}
		return Response.status(Response.Status.OK).entity("OK").build();
	}
	
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response save(Redirection redirection) {
		Redirection result;
		try {
			result = dao.save(redirection);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException("An error has occured while saving", Status.INTERNAL_SERVER_ERROR);
		}
		return Response.status(Response.Status.OK).entity(result).build();
	}


}
