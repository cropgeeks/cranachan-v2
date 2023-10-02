package jhi.cranachan.data;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Dataset
{
	private int id;
	private String doi;
	private String name;
	private String version;
	private String filepath;
	private int refseqSetId;
	private DatasetDescription description;	

	public Dataset() {
	}

	public int getId()
		{ return id; }

	public void setId(int id)
		{ this.id = id; }

	public String getDoi()
		{ return doi; }

	public void setDoi(String doi)
		{ this.doi = doi; }

	public String getName()
		{ return name; }

	public void setName(String name)
		{ this.name = name; }

	public String getVersion()
		{ return version; }

	public void setVersion(String version)
		{ this.version = version; }

	public String getFilepath()
		{ return filepath; }

	public void setFilepath(String filepath)
		{ this.filepath = filepath; }

	public int getRefseqSetId()
		{ return refseqSetId; }

	public void setRefSeqSetId(int refseqSetId)
		{ this.refseqSetId = refseqSetId; }

	public DatasetDescription getDescription()
		{ return description; }

	public void setDescription(String descString) { 
		if (descString != null)
		{
			GsonBuilder builder = new GsonBuilder();
			builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
			Gson gson = builder.create();
			description = gson.fromJson(descString, DatasetDescription.class);
		} 
	}
}