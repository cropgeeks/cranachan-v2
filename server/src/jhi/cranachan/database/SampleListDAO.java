package jhi.cranachan.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jakarta.servlet.ServletContext;

import jhi.cranachan.DatabaseUtils;
import jhi.cranachan.data.SampleList;


public class SampleListDAO {
  private static final String ALL_SAMPLELISTS = "SELECT * FROM sample_lists";
  private static final String SAMPLELIST_BY_ID = "SELECT * FROM sample_listss WHERE id = ?";

  private ServletContext context;
  ArrayList<SampleList> sampleLists = new ArrayList<SampleList>();

  public SampleListDAO(ServletContext context) {
    this.context = context;
   }

  public ArrayList<SampleList> getAllSampleLists() {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = con.prepareStatement(ALL_SAMPLELISTS);
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          SampleList sampleList = setSampleList(resultSet);          
          sampleLists.add(sampleList);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}

    return sampleLists;
  }

  public SampleList getSampleListByID(int id) {
    DatabaseUtils.init(context);
    SampleList sampleList = new SampleList();

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, SAMPLELIST_BY_ID, id);
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          sampleList = setSampleList(resultSet);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return sampleList;
  }


  private SampleList setSampleList(ResultSet resultSet) throws SQLException {
    SampleList sampleList = new SampleList();

    sampleList.setId(resultSet.getInt("id"));
    sampleList.setName(resultSet.getString("name"));
    sampleList.setDescription(resultSet.getString("description"));

    return sampleList;
  }
}