package jhi.cranachan.data;

public class Result {
  boolean isRunning;
  String chromosome;
  long start;
  long end;
  long noSNPs;
  String vcfPath;
  String vcfName;
  String FJPath;
  String FJName;
  String gene;

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

  public String getGene() {
    return gene;
  }
  public void setGene(String gene) {
    this.gene = gene;
  }
  

  
}
