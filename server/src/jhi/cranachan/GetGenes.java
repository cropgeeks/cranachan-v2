package jhi.cranachan;

public class GetGenes {
  private String CHROMOSOME_BY_ID = "SELECT id FROM refseqs WHERE name = ?";
  private String GENES_TO_REFSEQS = "SELECT gene_id FROM genes_to_refseqs WHERE refseq_id = ? AND position_end >= ? AND position_start <= ?";
  
}
