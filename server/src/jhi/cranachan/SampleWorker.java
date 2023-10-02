package jhi.cranachan;

import java.util.ArrayList;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.core.Context;
import jhi.cranachan.data.Sample;
import jhi.cranachan.database.SampleDAO;

public class SampleWorker {
  ArrayList<Sample> samples = new ArrayList<Sample>();
  ArrayList<Integer> sampleListIDs = new ArrayList<Integer>();
  ArrayList<Integer> datasetIDs = new ArrayList<Integer>();
  ArrayList<Integer> finalIDs = new ArrayList<Integer>();
  String sampleFilePath;
  ServletContext context;
  SampleDAO sampleDAO;

  public SampleWorker (@Context ServletContext context, int listID, int datasetID) { 
    this.context = context;
    sampleDAO = new SampleDAO(context);

    getSampleIDs(listID, datasetID);
    getSamples();
  }

  public void getSampleIDs(int listID, int datasetID) {
    sampleListIDs = sampleDAO.getSampleIDsBySampleList(listID);
    datasetIDs = sampleDAO.getSampleIDsByDataset(datasetID);
    
    for(int id:sampleListIDs) {
      if(!finalIDs.contains(id) && datasetIDs.contains(id)){
        finalIDs.add(id);
      }
    }
   }

   public void getSamples() {   
    for(int id : finalIDs) {
      samples.add(sampleDAO.getSampleByID(id));
    }    
  }

  public String outputSampleNames() {
    String output = "";
    int size = samples.size();

    for (int x=0; x<size; x++) {
      output += samples.get(x).getName();
      if (x != size-1) {
        output += "\t";
      }
    }
    return output;
  }
}
