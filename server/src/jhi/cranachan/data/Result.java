package jhi.cranachan.data;

public class Result {
  boolean isRunning;
  String chromosome;
  int start;
  int end;
  long noSNPs;
  String vcfPath;
  String vcfName;
  String FJPath;
  String FJName;

  public Result() {
  }

  public boolean isRunning() {
    return isRunning;
  }
  public void setRunning(boolean isRunning) {
    this.isRunning = isRunning;
  }
  public String getChromosome() {
    return chromosome;
  }
  public void setChromosome(String chromosome) {
    this.chromosome = chromosome;
  }
  public int getStart() {
    return start;
  }
  public void setStart(int start) {
    this.start = start;
  }
  public int getEnd() {
    return end;
  }
  public void setEnd(int end) {
    this.end = end;
  }
  public long getNoSNPs() {
    return noSNPs;
  }
  public void setNoSNPs(long noSNPs) {
    this.noSNPs = noSNPs;
  }
  public String getVcfPath() {
    return vcfPath;
  }
  public void setVcfPath(String vcfPath) {
    this.vcfPath = vcfPath;
  }
  public String getVcfName() {
    return vcfName;
  }
  public void setVcfName(String vcfName) {
    this.vcfName = vcfName;
  }

  public String getFJPath() {
    return FJPath;
  }

  public void setFJPath(String fJPath) {
    FJPath = fJPath;
  }

  public String getFJName() {
    return FJName;
  }

  public void setFJName(String fJName) {
    FJName = fJName;
  }
  

  
}
