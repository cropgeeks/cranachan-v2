package jhi.cranachan;

import jakarta.ws.rs.*;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/")
public class Cranachan extends ResourceConfig
{
	public Cranachan()
	{
		packages("jhi.cranachan");
	}
}