package jhi.cranachan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jhi.cranachan.bcftools.BcfToolsStats;
import jhi.cranachan.bcftools.BcfToolsView;
import jhi.cranachan.bcftools.FlapjackCreateProject;
import jhi.cranachan.data.*;
import jhi.cranachan.database.DatasetDAO;
import jhi.cranachan.database.GeneDAO;
import jhi.cranachan.database.RefseqDAO;
import jhi.flapjack.io.cmd.VcfToFJTabbedConverter;

@Path("/submitGene")
public class SubmitGene {/*
  private DatasetDAO datasetDAO;
  private GeneDAO geneDAO;
  private RefseqDAO refseqDAO;
  private SampleWorker sampleWorker;

  private Dataset dataset; 
  private String datasetPath = "";
	private File tmp = new File(System.getProperty("java.io.tmpdir"));
	private File vcfFile;
	private File statsFile;
	private String name;
  private File outputDir;
	private String flapJackFileName = "";
	private File flapJackFile = null;
	private List<Result> results = new ArrayList<Result>();
	private Result result = new Result();
  private Refseq refseq = new Refseq();


  // **************** BORKED, BASED STRUCTURE ON HAVING REFSEQID, THIS ISN'T ON THE FORM, WILL ONLY HAVE DATASETID, RESTRUCTURE NEEDED ****************
  @GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Result> submitPosition(@Context ServletContext context, @QueryParam("geneList") String geneList, @QueryParam("extendRegion") int extendRegion, @QueryParam("quality") int quality, @QueryParam("sampleSet") int sampleSet, @QueryParam("datasetID") int datasetID) {
  
  datasetDAO = new DatasetDAO(context);
  geneDAO = new GeneDAO(context);
  refseqDAO = new RefseqDAO(context);

  dataset = datasetDAO.getDatasetByID(datasetID);
  sampleWorker = new SampleWorker(context, sampleSet, datasetID);

  String[] geneNames = geneList.split(";");

  if(geneNames.length == 1) {
    flapJackFileName = geneNames[0] + ".flapjack";
  }
  else {
    flapJackFileName = dataset.getName() + ".flapjack";
  }

  for (String geneName : geneNames) {
    ArrayList<Gene> genes = geneDAO.getGeneIDsByName(geneName, datasetID);

    for(Gene gene : genes) {

    }
  }

  return results;

  }

  private void runBcfToolsView() {
		name = ge.getName() + "-" + start + "-" + end;
		long currentTime = System.currentTimeMillis();
		outputDir = new File(tmp + "/" + currentTime);
		outputDir.mkdir();
		vcfFile = new File(outputDir, name + ".vcf");
		try {
			result.setRunning(true);
			result.setChromosome(chromosome.getName());
			result.setStart(start);
			result.setEnd(end);

			BcfToolsView view = new BcfToolsView(new File(datasetPath))
				.withOnlySNPs()
				.withVCFOutput()
				.withRegions(chromosome.getName(), start, end)
				.withOnlySamples(sampleWorker.outputSampleNames())
				.withQualityFilter(quality)
				.withOutputFile(vcfFile);

			view.run("BCFTOOLS", outputDir.getAbsolutePath());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		result.setVcfName(vcfFile.getName());
		result.setVcfPath(vcfFile.getAbsolutePath());
		result.setRunning(false);
	}

	private void runBcfToolsStats() {
		result.setRunning(true);
		statsFile = new File(outputDir, name + ".stats");
		try {
			BcfToolsStats stats = new BcfToolsStats(vcfFile)
				.withOutputFile(statsFile);

			stats.run("BCFTOOLS", outputDir.getAbsolutePath());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		result.setNoSNPs(getSnpCount());
		result.setRunning(false);
	}


	private long getSnpCount() {
		long snpCount = 0;

		try (BufferedReader reader = new BufferedReader(new FileReader(statsFile))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("SN")) {
					String[] tokens = line.split("\t");
					System.out.println(String.join(",", tokens));
					if (tokens[2].equalsIgnoreCase("number of snps:")) {
						snpCount = Integer.parseInt(tokens[3]);
						return snpCount;
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return snpCount;
	}

	private String changeFileExtension(String filename, String extension) {
		String newFileName = filename;
		if (newFileName.indexOf(".") > 0)
			newFileName = newFileName.substring(0, newFileName.lastIndexOf(".")) + extension;

		return newFileName;
	}

		private void writeQtlFile(File file, List<Gene> genes)
	{
		if (!genes.isEmpty())
		{
			try(PrintWriter writer = new PrintWriter(new FileWriter(file)))
			{
				writer.println("# fjFile = QTL");
				writer.println("Name\tChromosome\tPosition\tPos-Min\tPos-Max\tTrait\tExperiment");
				for (Gene gene : genes)
				{
					long pos = gene.getStart() + ((gene.getEnd()-gene.getStart()) / 2);
					writer.println(gene.getName()+ "\t" + chromosome.getName() + "\t" + pos + "\t" + gene.getStart() + "\t" + gene.getEnd() + "\t" + "Trait\tExperiment");
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void getFJFiles(ServletContext context) {
		GeneDAO geneDAO = new GeneDAO(context);
		String mapFileName = changeFileExtension(vcfFile.getName(), ".map");
		File mapFile = new File(outputDir, mapFileName);

		String genotypeFileName = changeFileExtension(vcfFile.getName(), ".dat");
		File genotypeFile = new File(outputDir, genotypeFileName);

		VcfToFJTabbedConverter vcfConverter = new VcfToFJTabbedConverter(vcfFile, mapFile, genotypeFile);
		vcfConverter.convert();

		String qtlFileName = changeFileExtension(vcfFile.getName(), ".qtl");
		File qtlFile = new File(outputDir, qtlFileName);
		
		List<Gene> genes = geneDAO.getGenesByRefseq(chromosome.getId(), start, end);
		writeQtlFile(qtlFile, genes);

		String projectFileName = changeFileExtension(vcfFile.getName(), ".flapjack");
		flapJackFile = new File(outputDir, projectFileName);

		String flapjackPath = flapJackFile.getAbsolutePath();

		try {
			FlapjackCreateProject createProject = new FlapjackCreateProject(genotypeFile, flapJackFile)
				.withMapFile(mapFile)
				.withQtlFile(qtlFile);
			createProject.run(flapjackPath, outputDir.getAbsolutePath());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		result.setFJName(projectFileName);
		result.setFJPath(flapjackPath);
	}

  //Hard coded for testing api & front end, needs to read ResultSet from DB call
	/*private Result getResult()
  {
		Result result = new Result();

		result.setChromosome(chromosome.getName());
		result.setStart(start);
		result.setEnd(end);
		result.setNoSNPs(getSnpCount());
		result.setVcfName(vcfFile.getName());
    result.setVcfPath(vcfFile.getAbsolutePath());
		
		return result;
	}*/
}
