package jhi.cranachan;

import java.io.File;
import java.io.FileInputStream;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;


@Path("/download")
public class DownloadService {
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@Context ServletContext context, @QueryParam("path") String path) {
	File file = null;
	FileInputStream fileInputStream = null;

		try {
			file = new File(path); 
			fileInputStream = new FileInputStream(file);			
		} 
		catch (Exception e) {
		}

		Response.ResponseBuilder responseBuilder = Response.ok(fileInputStream);
		//add length?
		responseBuilder.header("Content-Disposition","attachment; filename = " + file.getName());

		return responseBuilder.build();
	}
}
