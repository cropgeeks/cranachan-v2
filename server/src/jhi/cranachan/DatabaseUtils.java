package jhi.cranachan;

import java.sql.*;

import jakarta.servlet.*;

import org.apache.commons.dbcp2.*;

public class DatabaseUtils
{
	private static BasicDataSource ds;

	public static void init(ServletContext context)
	{
		String username = context.getInitParameter("mysql.username");
		String password = context.getInitParameter("mysql.password");
		String url = context.getInitParameter("mysql.url");

		if (ds == null)
		{
			ds = new BasicDataSource();
			ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);
		}
	}

	public static Connection getConnection()
		throws SQLException
	{
		return ds.getConnection();
	}

	public static PreparedStatement createWithId(Connection con, String query, String id)
		throws SQLException
	{
		// Prepare statement with ID
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, id);

		return statement;
	}

	public static PreparedStatement createPreparedStatement(Connection con, String query, Object... values)
			throws SQLException
	{
		PreparedStatement stmt = con.prepareStatement(query);

		if(values != null)
		{
			int i = 1;
			for (Object value : values)
			{
				stmt.setString(i++, value.toString());
			}
		}

		return stmt;
	}

	public static PreparedStatement createWithTwoIds(Connection con, String query, String id1, String id2)
		throws SQLException
	{
		// Prepare statement with ID
		PreparedStatement statement = con.prepareStatement(query);
		statement.setString(1, id1);
		statement.setString(2, id2);

		return statement;
	}
}