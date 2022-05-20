package jhi.cranachan;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/test")
public class Test
{
	public class TestObject
	{
		public String name = "test object";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TestObject getTestObject()
	{
		return new TestObject();
	}
}