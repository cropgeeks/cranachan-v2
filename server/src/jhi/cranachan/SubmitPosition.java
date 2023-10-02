package jhi.cranachan;

import jakarta.servlet.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;


import jhi.cranachan.bcftools.*;
import jhi.cranachan.data.*;
import jhi.cranachan.database.*;
import jhi.flapjack.io.cmd.*;

//need to change get with search params to write values to a file and create unique id 
//change get with id to return current results as json, if still running just return empty with jobRunning true

@Path("/submitPosition")
public class SubmitPosition
{
  private Refseq chromosome;
  private int start;
  private int end;
	private SampleWorker sampleWorker;
  private int quality;
  private int sampleSet;
	private int datasetID;
	private String datasetPath = "";
	private File tmp = new File(System.getProperty("java.io.tmpdir"));
	private File vcfFile;
	private File statsFile;
	private String name;
	private String flapJackFileName = "";
	private File flapJackFile = null;
	private List<Result> results = new ArrayList<Result>();
	private Result result = new Result();
  
	private File outputDir;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Result> submitPosition(@Context ServletContext context, @QueryParam("chromosome") int chromosomeID, @QueryParam("start") int start, @QueryParam("end") int end, @QueryParam("quality") int quality, @QueryParam("sample") int sampleSet, @QueryParam("datasetID") int datasetID) {
		RefseqDAO refseqDAO = new RefseqDAO(context);

    this.chromosome = refseqDAO.getRefseqByID(chromosomeID);
    this.start = start;
    this.end = end;
    this.quality = quality;
    this.sampleSet = sampleSet;
		this.datasetID = datasetID;    
    
		sampleWorker = new SampleWorker(context, sampleSet, datasetID);

		DatasetDAO datasetDAO = new DatasetDAO(context);
		Dataset dataset = datasetDAO.getDatasetByID(datasetID);
		datasetPath = dataset.getFilepath();

		runBcfToolsView();
		runBcfToolsStats();

	/*	Error seems to be caused by FJ jar not being found but is in war file?
		if(getSnpCount() >0) {
			result.setRunning(true);
			getFJFiles(context);
			result.setRunning(false);
		}
		*/
		results.add(result);
		return results;
	}

	private void runBcfToolsView() {
		name = chromosome.getName() + "-" + start + "-" + end;
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

		flapJackFileName = changeFileExtension(vcfFile.getName(), ".flapjack");
		flapJackFile = new File(outputDir, flapJackFileName);

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

		result.setFJName(flapJackFileName);
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
