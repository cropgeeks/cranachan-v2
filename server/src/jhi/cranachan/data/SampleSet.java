package jhi.cranachan.data;

import java.sql.*;

public class SampleSet
{
	private int id;
	private String name;
	private Date createdOn;
	private Date updatedOn;
	private String description;

	public SampleSet()
	{
	}

	public int getId()
		{ return id; }

	public void setId(int id)
		{ this.id = id; }

	public String getName()
		{ return name; }

	public void setName(String name)
		{ this.name = name; }

	public Date getCreatedOn()
		{ return createdOn; }

	public void setCreatedOn(Date createdOn)
		{ this.createdOn = createdOn; }

	public Date getUpdatedOn()
		{ return updatedOn; }

	public void setUpdatedOn(Date updatedOn)
		{ this.updatedOn = updatedOn; }

	public String getDescription()
		{ return description; }

	public void setDescription(String description)
		{ this.description = description; }
}