package jhi.cranachan;

import jakarta.servlet.*;
import java.sql.*;
import java.util.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import jhi.cranachan.data.*;

@Path("/samplesets")
public class GetSampleSets
{
	private static final String ALL_SAMPLESETS = "SELECT * FROM sample_lists ORDER BY name;";

	public GetSampleSets()
	{
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<SampleSet> getSampleSets(@Context ServletContext context)
	{
		DatabaseUtils.init(context);

		List<SampleSet> samplesets = new ArrayList<>();

		try (Connection con = DatabaseUtils.getConnection();
			 PreparedStatement stmt = con.prepareStatement(ALL_SAMPLESETS);
			 ResultSet resultSet = stmt.executeQuery())
		{
			while (resultSet.next())
			{
				SampleSet sampleset = getSampleSet(resultSet);
				samplesets.add(sampleset);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return samplesets;
	}

	private SampleSet getSampleSet(ResultSet resultSet)
		throws SQLException
	{
		SampleSet sampleset = new SampleSet();

		sampleset.setId(resultSet.getInt("id"));
		sampleset.setName(resultSet.getString("name"));
		sampleset.setCreatedOn(resultSet.getDate("created_on"));
		sampleset.setUpdatedOn(resultSet.getDate("updated_on"));
		sampleset.setDescription(resultSet.getString("description"));

		return sampleset;
	}
}