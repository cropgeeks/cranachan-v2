package jhi.cranachan.data;

public class Refseq {
  private int id;
  private String doi;
  private String name;
  private int refseqSetID;
  private long length;
  private String type;

  public Refseq() {
    
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getDoi() {
    return doi;
  }
  public void setDoi(String doi) {
    this.doi = doi;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public int getRefseqSetID() {
    return refseqSetID;
  }
  public void setRefseqSetID(int refseqSetID) {
    this.refseqSetID = refseqSetID;
  }
  public long getLength() {
    return length;
  }
  public void setLength(long length) {
    this.length = length;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
}
