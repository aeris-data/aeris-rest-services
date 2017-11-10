package fr.aeris.actris.validation.rest.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Component
@Path("/imagecrop")
@Api(value = "/imagecrop")
public class ImageCropService  {

	public ImageCropService() {
	}

	@Autowired
	private ActrisValidationConfig config;

	private static Logger log = LoggerFactory.getLogger(ImageCropService.class);

	@GET
	@Path("/isalive")
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Tests the availability of the whole service")
	public Response isAlive() {
		String answer = "Yes";
		return Response.status(Response.Status.OK).entity(answer).build();
	}

	@GET
	@Path("/png")
	public Response getFlagByUuid(@QueryParam("url") String url, 
			@QueryParam("top")  @DefaultValue("0") int top, 
			@QueryParam("bottom")  @DefaultValue("0") int bottom,
			@QueryParam("left")  @DefaultValue("0") int left,
			@QueryParam("right")  @DefaultValue("0") int right
			) {

		Client client = null;
		if (url.toLowerCase().startsWith("https")) {
			client = ClientBuilder.newBuilder().sslContext(SslUtils.getSslContext()).build();
		}                else {
			client = ClientBuilder.newClient();
		}

		WebTarget webTarget = client.target(url);
		Response response = webTarget.request().get();
		if (response.getStatus() != HttpStatus.SC_OK) {
			return null;
		}

		else {
			InputStream is = response.readEntity(InputStream.class);
			File workingDirectory = new File(config.getWorkingDirectory());
			workingDirectory.mkdirs();

			File tmpFile = new File(workingDirectory, UUID.randomUUID().toString()+".png");

			try {
				fetchFeed(is, tmpFile); 
				IOUtils.closeQuietly(is);

				BufferedImage src = ImageIO.read(tmpFile.toURI().toURL());
				
				BufferedImage tgt = src.getSubimage(left, top, src.getWidth()-right-left, src.getHeight()-bottom-top);
				
				ImageIO.write(tgt, "png", tmpFile);
				
				ResponseBuilder result = Response.ok((Object) tmpFile);
				result.header("Content-Disposition",
						"attachment; filename="+tmpFile.getName());
				return result.build();

			}
			catch (Exception e) {
				return null;
			}
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