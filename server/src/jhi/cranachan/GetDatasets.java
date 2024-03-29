package jhi.cranachan;

import jakarta.servlet.*;
import java.util.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import jhi.cranachan.data.*;
import jhi.cranachan.database.DatasetDAO;

@Path("/datasets")
public class GetDatasets
{
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Dataset> getDatasets(@Context ServletContext context)
	{
		DatasetDAO datasetDAO = new DatasetDAO(context);

		List<Dataset> datasets = datasetDAO.getAllDatasets();

		return datasets;
	}

	/*@GET
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
	}*/
}