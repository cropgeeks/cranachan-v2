package jhi.cranachan.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jakarta.servlet.ServletContext;

import jhi.cranachan.DatabaseUtils;
import jhi.cranachan.data.Dataset;


public class DatasetDAO {
  private static final String ALL_DATASETS = "SELECT * FROM datasets";
  private static final String DATASET_BY_ID = "SELECT * FROM datasets WHERE id = ?";
  private static final String DATASETS_BY_REFSEQSET = "SELECT * FROM datasets WHERE refseqset_id = ?";

  private ServletContext context;
  ArrayList<Dataset> datasets = new ArrayList<Dataset>();

  public DatasetDAO(ServletContext context) {
    this.context = context;
   }

  public ArrayList<Dataset> getAllDatasets() {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = con.prepareStatement(ALL_DATASETS);
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          Dataset dataset = setDataset(resultSet);          
          datasets.add(dataset);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}

    return datasets;
  }

  public Dataset getDatasetByID(int id) {
    DatabaseUtils.init(context);
    Dataset dataset = new Dataset();

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, DATASET_BY_ID, id);
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          dataset = setDataset(resultSet);         
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return dataset;
  }

  public ArrayList<Dataset> getDatasetByRefseqSet(int refseqSetID) {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, DATASETS_BY_REFSEQSET, refseqSetID);
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          Dataset dataset = setDataset(resultSet);          
          datasets.add(dataset);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return datasets;
  }

  private Dataset setDataset(ResultSet resultSet) throws SQLException {
    Dataset dataset = new Dataset();

    dataset.setId(resultSet.getInt("id"));
    dataset.setDoi(resultSet.getString("doi"));
    dataset.setName(resultSet.getString("name"));
    dataset.setVersion(resultSet.getString("version"));
    dataset.setFilepath(resultSet.getString("filepath"));
    dataset.setRefSeqSetId(resultSet.getInt("refseqset_id"));
    dataset.setDescription(resultSet.getString("description"));

    return dataset;
  }


}