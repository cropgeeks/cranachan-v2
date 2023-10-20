package jhi.cranachan.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jakarta.servlet.ServletContext;

import jhi.cranachan.DatabaseUtils;
import jhi.cranachan.data.Refseq;


public class RefseqDAO {
  private static final String ALL_REFSEQS = "SELECT * FROM refseqs";
  private static final String REFSEQ_BY_ID = "SELECT * FROM refseqs WHERE id = ?";
  private static final String REFSEQ_BY_REFSEQSET = "SELECT * FROM refseqs WHERE refseqset_id = ?";

  private ServletContext context;
  ArrayList<Refseq> refseqs = new ArrayList<Refseq>();

  public RefseqDAO(ServletContext context) {
    this.context = context;
   }

  public ArrayList<Refseq> getAllRefseqs() {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = con.prepareStatement(ALL_REFSEQS);
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          Refseq refseq = setRefseq(resultSet);          
          refseqs.add(refseq);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}

    return refseqs;
  }

  public Refseq getRefseqByID(int id) {
    DatabaseUtils.init(context);
    Refseq refseq = new Refseq();

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, REFSEQ_BY_ID, Integer.toString(id));
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          refseq = setRefseq(resultSet);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return refseq;
  }

  public ArrayList<Refseq> getRefseqByRefseqSet(int refseqSetID) {
    DatabaseUtils.init(context);

    try (Connection con = DatabaseUtils.getConnection();
			PreparedStatement stmt = DatabaseUtils.createPreparedStatement(con, REFSEQ_BY_REFSEQSET, Integer.toString(refseqSetID));
			ResultSet resultSet = stmt.executeQuery()) {
			
        while (resultSet.next()) {
          Refseq refseq = setRefseq(resultSet);          
          refseqs.add(refseq);
			  }
		  }

		catch (SQLException e) {
			e.printStackTrace();
		}
       
    return refseqs;
  }

  private Refseq setRefseq(ResultSet resultSet) throws SQLException {
    Refseq refseq = new Refseq();

    refseq.setId(resultSet.getInt("id"));
    refseq.setDoi(resultSet.getString("doi"));
    refseq.setName(resultSet.getString("name"));
    refseq.setRefseqSetID(resultSet.getInt("refseqset_id"));
    refseq.setLength(resultSet.getLong("length"));
    refseq.setType(resultSet.getString("type"));

    return refseq;
  }


}