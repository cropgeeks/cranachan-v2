package jhi.cranachan.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jakarta.servlet.ServletContext;

import jhi.cranachan.DatabaseUtils;
import jhi.cranachan.data.RefseqSet;


public class RefseqSetDAO {
  private static final String ALL_REFSEQSETS = "SELECT * FROM refseqsets";
  private static final String REFSEQSET_BY_ID = "SELECT * FROM refseqset WHERE id = ?";

  private ServletContext context;
  ArrayList<RefseqSet> refseqSets = new ArrayList<RefseqSet>();

  public RefseqSetDAO(ServletContext context) {
    this.context = context;
   }

  public ArrayList<RefseqSet> getAllRefseqSets() {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = con.prepareStatement(ALL_REFSEQSETS);
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          RefseqSet refseqSet = setRefseqSet(resultSet);          
          refseqSets.add(refseqSet);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}

    return refseqSets;
  }

  public ArrayList<RefseqSet> getRefseqByID(int id) {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, REFSEQSET_BY_ID, id);
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          RefseqSet refseqSet = setRefseqSet(resultSet);          
          refseqSets.add(refseqSet);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return refseqSets;
  }

  private RefseqSet setRefseqSet(ResultSet resultSet) throws SQLException {
    RefseqSet refseqSet = new RefseqSet();

    refseqSet.setId(resultSet.getInt("id"));
    refseqSet.setDoi(resultSet.getString("doi"));
    refseqSet.setName(resultSet.getString("name"));
    refseqSet.setDescription(resultSet.getString("description"));

    return refseqSet;
  }


}