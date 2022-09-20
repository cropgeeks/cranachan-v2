package jhi.cranachan;

import jakarta.servlet.*;
import java.sql.*;
import java.util.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import com.google.gson.*;

import jhi.cranachan.data.*;

@Path("/chromosomes")
public class GetChromosomes
{
	private static final String ALL_CHROMOSOMES = "SELECT * FROM refseqs;";
	private static final String CHROMOSOME_BY_ID = "SELECT * FROM refseqs WHERE ID = ?;";

	public GetChromosomes()
	{
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Chromosome> getChromosomes(@Context ServletContext context)
	{
		DatabaseUtils.init(context);

		List<Chromosome> chromosomes = new ArrayList<>();

		try (Connection con = DatabaseUtils.getConnection();
			 PreparedStatement stmt = con.prepareStatement(ALL_CHROMOSOMES);
			 ResultSet resultSet = stmt.executeQuery())
		{
			while (resultSet.next())
			{
				Chromosome chromosome = getChromosome(resultSet);
				chromosomes.add(chromosome);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return chromosomes;
	}

	@GET
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
	}

	private Chromosome getChromosome(ResultSet resultSet)
		throws SQLException
	{
		Chromosome chromosome = new Chromosome();

		chromosome.setId(resultSet.getInt("id"));
		chromosome.setDoi(resultSet.getString("doi"));
		chromosome.setName(resultSet.getString("name"));
		chromosome.setLength(resultSet.getInt("length"));
		chromosome.setCreatedOn(resultSet.getDate("created_on"));
		chromosome.setUpdatedOn(resultSet.getDate("updated_on"));

		return chromosome;
	}
}