package jhi.cranachan.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import jakarta.servlet.ServletContext;

import jhi.cranachan.DatabaseUtils;
import jhi.cranachan.SubmitGene;
import jhi.cranachan.data.Gene;

public class GeneDAO {
  private static final String ALL_GENES = "SELECT * FROM genes";
  private static final String GENE_BY_ID = "SELECT * FROM genes WHERE id = ?";
  private static final String GENEIDS_BY_NAME = "SELECT id FROM genes WHERE name = ?";
  private static final String GENES_BY_ID_AND_REFSEQID = "SELECT * FROM genes_to_refseqs WHERE gene_id = ? AND refseq_id = ?";
  private static final String GENEIDS_BY_POSITION = "SELECT * FROM genes_to_refseqs WHERE refseq_id = ? AND position_end >= ? AND position_start <= ?";
  private Logger LOG = Logger.getLogger(GeneDAO.class.getName());

  
  private ServletContext context;
  ArrayList<Gene> genes = new ArrayList<Gene>();

  public GeneDAO(ServletContext context) {
    this.context = context;
   }

  public ArrayList<Gene> getAllGenes() {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = con.prepareStatement(ALL_GENES);
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          Gene gene = setGene(resultSet);          
          genes.add(gene);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}

    return genes;
  }

  public Gene getGeneByID(int id) {
    DatabaseUtils.init(context);
    Gene gene = null;

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, GENE_BY_ID, Integer.toString(id));
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          gene = setGene(resultSet);          
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return gene;
  }

  public ArrayList<Gene> getGenesByRefseq(int refseqID, long start, long end) {
    DatabaseUtils.init(context);
    ArrayList<String> parameters = new ArrayList<String>();
    parameters.add(Integer.toString(refseqID));
    parameters.add(Long.toString(start));
    parameters.add(Long.toString(end));

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, GENEIDS_BY_POSITION, parameters);
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          int geneID = resultSet.getInt("gene_id");
          Gene gene = getGeneByID(geneID);
          gene.setStart(resultSet.getLong("position_start"));      
          gene.setEnd(resultSet.getLong("position_end")); 
          genes.add(gene);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return genes;
  }

  /* Gene gene = getGeneByID(geneID);
          gene.setStart(resultSet.getLong("position_start"));      
          gene.setEnd(resultSet.getLong("position_end")); 
          genes.add(gene);
			  }*/

   public ArrayList<Gene> getGeneIDsByName(String name, int refseqID) {
    DatabaseUtils.init(context);
    ArrayList<Integer> geneIDs = new ArrayList<Integer>();

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmtName = DatabaseUtils.createPreparedStatement(con, GENEIDS_BY_NAME, name);
			ResultSet resultSet = stmtName.executeQuery()) {
        while (resultSet.next()) {
          geneIDs.add(resultSet.getInt("id"));
  
        } 
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
    
    filterGenesByRefseq(geneIDs, refseqID);

    return genes;
  }

  private void filterGenesByRefseq(ArrayList<Integer> geneIDs, int refseqID) {
    DatabaseUtils.init(context);

    for(int geneID : geneIDs) {
      ArrayList<String> parameters = new ArrayList<String>();
      parameters.add(Integer.toString(geneID));
      parameters.add(Integer.toString(refseqID));

      try (Connection con = DatabaseUtils.getConnection();
        PreparedStatement stmtName = DatabaseUtils.createPreparedStatement(con, GENES_BY_ID_AND_REFSEQID, parameters);
        ResultSet resultSet = stmtName.executeQuery()) {
          
          while (resultSet.next()) {
            Gene gene = getGeneByID(geneID);
            gene.setStart(resultSet.getLong("position_start"));      
            gene.setEnd(resultSet.getLong("position_end")); 
            gene.setRefseq(refseqID);
            genes.add(gene);
            genes.add(setGene(resultSet));
          } 
        }

      catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private Gene setGene(ResultSet resultSet) throws SQLException {
    Gene gene = new Gene();

    gene.setId(resultSet.getInt("id"));
    gene.setDoi(resultSet.getString("doi"));
    gene.setName(resultSet.getString("name"));
    gene.setGeneSetId(resultSet.getInt("geneset_id"));

    return gene;
  }
}