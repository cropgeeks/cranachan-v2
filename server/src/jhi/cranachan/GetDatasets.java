package jhi.cranachan;

import jakarta.servlet.*;
import java.sql.*;
import java.util.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.google.gson.*;

import jhi.cranachan.data.*;

@Path("/datasets")
public class GetDatasets
{
	private static final String ALL_DATASETS = "SELECT * FROM datasets;";
	private static final String DATASET_BY_ID = "SELECT * FROM datasets WHERE id = ?;";

	public GetDatasets()
	{
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Dataset> getDatasets(@Context ServletContext context)
	{
		DatabaseUtils.init(context);

		List<Dataset> datasets = new ArrayList<>();

		try (Connection con = DatabaseUtils.getConnection();
			 PreparedStatement stmt = con.prepareStatement(ALL_DATASETS);
			 ResultSet resultSet = stmt.executeQuery())
		{
			while (resultSet.next())
			{
				Dataset dataset = getDataset(resultSet);
				datasets.add(dataset);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return datasets;
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Dataset getDataset(@Context ServletContext context, @PathParam("id") String id)
	{
		DatabaseUtils.init(context);

		try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createWithId(con, DATASET_BY_ID, id);
			ResultSet resultSet = stmt.executeQuery())
		{
			if (resultSet.next())
				return getDataset(resultSet);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private Dataset getDataset(ResultSet resultSet)
		throws SQLException
	{
		Dataset dataset = new Dataset();

		dataset.setId(resultSet.getInt("id"));
		dataset.setDoi(resultSet.getString("doi"));
		dataset.setName(resultSet.getString("name"));
		dataset.setVersion(resultSet.getString("version"));
		dataset.setFilepath(resultSet.getString("filepath"));
		dataset.setRefSeqSetId(resultSet.getInt("refseqset_id"));
		dataset.setCreatedOn(resultSet.getDate("created_on"));
		dataset.setUpdatedOn(resultSet.getDate("updated_on"));

		String descString = resultSet.getString("description");
		if (descString != null)
		{
			GsonBuilder builder = new GsonBuilder();
			builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
			Gson gson = builder.create();
			dataset.setDescription(gson.fromJson(descString, DatasetDescription.class));
		}

		return dataset;
	}
}