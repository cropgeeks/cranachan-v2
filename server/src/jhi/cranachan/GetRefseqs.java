package jhi.cranachan;

import jakarta.servlet.*;
import java.util.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import jhi.cranachan.data.*;
import jhi.cranachan.database.DatasetDAO;
import jhi.cranachan.database.RefseqDAO;

@Path("/refseqs")
public class GetRefseqs
{
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Refseq> getRefseqs(@Context ServletContext context, @QueryParam("datasetID") int datasetID)
	{
		DatasetDAO datasetDAO = new DatasetDAO(context);
		RefseqDAO refseqDAO = new RefseqDAO(context);

		Dataset dataset = datasetDAO.getDatasetByID(datasetID);
		ArrayList<Refseq> refseqs = refseqDAO.getRefseqByRefseqSet(dataset.getRefseqSetId());

		return refseqs;
	}

	/*@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Chromosome getChromosome(@Context ServletContext context, @PathParam("id") String id)
	{
		DatabaseUtils.init(context);

		try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createWithId(con, CHROMOSOME_BY_ID, id);
			ResultSet resultSet = stmt.executeQuery())
		{
			if (resultSet.next())
				return getChromosome(resultSet);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}*/
}