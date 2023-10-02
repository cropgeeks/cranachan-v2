package jhi.cranachan.data;

public class Gene
{
	private int id;
	private String doi;
	private String name;
	private int geneSetID;
	private long start;
	private long end;

	public Gene() {
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

	public int getGeneSetID()
		{ return geneSetID; }

	public void setGeneSetId(int geneSetID)
		{ this.geneSetID = geneSetID; }

	public void setGeneSetID(int geneSetID) {
		this.geneSetID = geneSetID;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
}