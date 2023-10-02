package jhi.cranachan;

import jakarta.servlet.*;
import java.util.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import jhi.cranachan.data.*;
import jhi.cranachan.database.SampleListDAO;

@Path("/samplelists")
public class GetSampleLists
{
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<SampleList> getSampleSets(@Context ServletContext context)
	{
		SampleListDAO sampleSetDAO = new SampleListDAO(context);

		List<SampleList> samplesets = sampleSetDAO.getAllSampleLists();
		return samplesets;
	}
}