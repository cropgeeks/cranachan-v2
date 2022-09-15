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
}