package jhi.cranachan.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jakarta.servlet.ServletContext;

import jhi.cranachan.DatabaseUtils;
import jhi.cranachan.data.Sample;


public class SampleDAO {
  private static final String ALL_SAMPLES = "SELECT * FROM samples";
  private static final String SAMPLE_BY_ID = "SELECT * FROM samples WHERE id = ?";
  private static final String SAMPLE_BY_SAMPLELIST = "SELECT samples_id FROM sample_lists_to_samples WHERE projects_id = ?";
  private static final String SAMPLE_BY_DATASET = "SELECT sample_id FROM samples_to_datasets WHERE dataset_id = ?";

  private ServletContext context;
  ArrayList<Sample> samples = new ArrayList<Sample>();
  ArrayList<Integer> IDs = new ArrayList<Integer>();

  public SampleDAO(ServletContext context) {
    this.context = context;
   }

  public ArrayList<Sample> getAllSamples() {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = con.prepareStatement(ALL_SAMPLES);
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          Sample sample = setSample(resultSet);          
          samples.add(sample);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}

    return samples;
  }

  public Sample getSampleByID(int id) {
    DatabaseUtils.init(context);
    Sample sample = new Sample();

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, SAMPLE_BY_ID, Integer.toString(id));
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          sample = setSample(resultSet);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return sample;
  }

    public ArrayList<Integer> getSampleIDsBySampleList(int sampleListID) {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, SAMPLE_BY_SAMPLELIST, Integer.toString(sampleListID));
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          IDs.add(resultSet.getInt("samples_id"));
          
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return IDs;
  }

  public ArrayList<Sample> getSamplesBySampleList(int sampleListID) {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, SAMPLE_BY_SAMPLELIST, Integer.toString(sampleListID));
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          int sampleID = resultSet.getInt("samples_id");
          Sample sample = getSampleByID(sampleID);          
          samples.add(sample);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return samples;
  }

  public ArrayList<Integer> getSampleIDsByDataset(int datasetID) {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, SAMPLE_BY_DATASET, Integer.toString(datasetID));
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          IDs.add(resultSet.getInt("sample_id"));
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return IDs;
  }

  public ArrayList<Sample> getSamplesByDataset(int datasetID) {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, SAMPLE_BY_DATASET, Integer.toString(datasetID));
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          int sampleID = resultSet.getInt("sample_id");
          Sample sample = getSampleByID(sampleID);          
          samples.add(sample);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return samples;
  }

  private Sample setSample(ResultSet resultSet) throws SQLException {
    Sample sample = new Sample();

    	sample.setId(resultSet.getInt("id"));
      sample.setDoi(resultSet.getString("doi"));
      sample.setName(resultSet.getString("name"));
      sample.setPublished(resultSet.getInt("published"));

    return sample;
  }
}