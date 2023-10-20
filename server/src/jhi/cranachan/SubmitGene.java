package jhi.cranachan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import org.jfree.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import jhi.cranachan.database.RefseqSetDAO;
import jhi.flapjack.io.cmd.VcfToFJTabbedConverter;

@Path("/submitGene")
public class SubmitGene {
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
  List<Refseq> refseqs = new ArrayList<Refseq>();
  private int extendRegion;
  private int quality;
	private String[] geneNames;
	private File completeFile;
	private int sampleSet;
	private int datasetID;
	Logger LOG = Logger.getLogger(SubmitGene.class.getName());
	private Gson gson = new Gson();
	private ArrayList<Gene> genes;

  @GET
	@Produces(MediaType.APPLICATION_JSON)
	public String submitPosition(@Context ServletContext context, @QueryParam("geneList") String geneList, @QueryParam("extendRegion") int extendRegion, @QueryParam("quality") int quality, @QueryParam("sampleSet") int sampleSet, @QueryParam("datasetID") int datasetID) {
  
  	this.quality = quality;
		this.extendRegion = extendRegion;
		this.sampleSet = sampleSet;
		this.datasetID = datasetID;
		geneNames = geneList.split("\\s*;\\s*");

		long currentTime = System.currentTimeMillis();
		outputDir = new File(tmp + "/" + currentTime);
		outputDir.mkdir();
		completeFile = new File(outputDir, "complete.txt");		

		new Thread(new Runnable() {
			public void run() {
				runSearch(context);
			}
		}).start();
		

		return completeFile.getAbsolutePath();
  }

	private void runSearch(ServletContext context) {
		datasetDAO = new DatasetDAO(context);
  	geneDAO = new GeneDAO(context);
  	refseqDAO = new RefseqDAO(context);
		dataset = datasetDAO.getDatasetByID(datasetID); 
		sampleWorker = new SampleWorker(context, sampleSet, datasetID);
		 
		datasetPath = dataset.getFilepath();
  	
  	refseqs = refseqDAO.getRefseqByRefseqSet(dataset.getRefseqSetId());
  

		if(geneNames.length == 1) {
			flapJackFileName = geneNames[0] + ".flapjack";
		}
		else {
			flapJackFileName = dataset.getName() + ".flapjack";
		}

		for (String geneName : geneNames) {
			for(Refseq refseq : refseqs) {
				genes = geneDAO.getGeneIDsByName(geneName, refseq.getId());
			}
		}

		for(Gene gene : genes) {
				result = new Result();
				runBcfToolsView(gene);
				runBcfToolsStats();
				getFJFiles(context, gene);
				results.add(result);
			}

		

		try (PrintWriter writer = new PrintWriter(new FileWriter(completeFile))){
			LOG.info("Writing results");
			writer.write(gson.toJson(results));			
		}
		catch (Exception e) { LOG.info("ERROR: " + e.getMessage());}
	}

	@Path("/checkPosition")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean checkPosition(@Context ServletContext context, @QueryParam("path") String path) {
		completeFile = new File(path);
		try {
			return(completeFile.exists());
		}
		catch (Exception e) { 
			Log.info("File Check Error"); 
			return false; 
		}
	}

	@Path("/getResults")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Result> getResults(@Context ServletContext context, @QueryParam("path") String path) {
		String fileContents = "";
		try (Scanner reader = new Scanner(new File(path))){
			while(reader.hasNext()) {
				fileContents += reader.next();
			}

			Type listOfResults = new TypeToken<ArrayList<Result>>() {}.getType();

    	Gson gson = new Gson();
    	results = gson.fromJson(fileContents, listOfResults);
		}
		catch (Exception e) {}

		return results;
	}

  private void runBcfToolsView(Gene gene) {
		Refseq refseq = refseqDAO.getRefseqByID(gene.getRefseq());
		
		name = gene.getName() + "-" + refseq.getName();
		vcfFile = new File(outputDir, name + ".vcf");		

		try {
			result.setRunning(true);
			result.setChromosome(refseq.getName());
			result.setStart(gene.getStart());
			result.setEnd(gene.getEnd());

			BcfToolsView view = new BcfToolsView(new File(datasetPath))
				.withOnlySNPs()
				.withVCFOutput()
				.withRegions(refseq.getName(), gene.getStart() - extendRegion, gene.getEnd() + extendRegion)
				.withOnlySamples(sampleWorker.outputSampleNames())
				.withQualityFilter(quality)
				.withOutputFile(vcfFile);

			view.run("BCFTOOLS", outputDir.getAbsolutePath());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		result.setGene(gene.getName());
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

	private void writeQtlFile(File file, List<Gene> genes) {
		if (!genes.isEmpty()) {
			try(PrintWriter writer = new PrintWriter(new FileWriter(file))) {
				writer.println("# fjFile = QTL");
				writer.println("Name\tChromosome\tPosition\tPos-Min\tPos-Max\tTrait\tExperiment");
				for (Gene gene : genes) {
					Refseq refseq = refseqDAO.getRefseqByID(gene.getRefseq());
					long pos = gene.getStart() + ((gene.getEnd()-gene.getStart()) / 2);
					writer.println(gene.getName()+ "\t" + refseq.getName() + "\t" + pos + "\t" + gene.getStart() + "\t" + gene.getEnd() + "\t" + "Trait\tExperiment");
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void getFJFiles(ServletContext context, Gene gene) {
		GeneDAO geneDAO = new GeneDAO(context);
		String mapFileName = changeFileExtension(vcfFile.getName(), ".map");
		File mapFile = new File(outputDir, mapFileName);

		String genotypeFileName = changeFileExtension(vcfFile.getName(), ".dat");
		File genotypeFile = new File(outputDir, genotypeFileName);

		VcfToFJTabbedConverter vcfConverter = new VcfToFJTabbedConverter(vcfFile, mapFile, genotypeFile);
		vcfConverter.convert();

		String qtlFileName = changeFileExtension(vcfFile.getName(), ".qtl");
		File qtlFile = new File(outputDir, qtlFileName);
		
		List<Gene> genes = geneDAO.getGenesByRefseq(gene.getRefseq(), gene.getStart(), gene.getEnd());
		writeQtlFile(qtlFile, genes);

		flapJackFile = new File(outputDir, flapJackFileName);

		String flapjackPath = context.getRealPath("/WEB-INF/lib/flapjack.jar");

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
}
