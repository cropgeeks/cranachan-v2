<template>
  <div class="container">
    <form class="text-light">
      <fieldset>
        <legend>Setup Extraction</legend>

        <div class="form-group">
          <label for="chromosome" class="form-label mt-4">Pick chromosome: </label>
          <select class="form-select" id="chromosome" v-model="selectedChromosome">
            <option v-for="chromosome in chromosomeList" :value="chromosome" v-bind:key="chromosome.id" >{{ chromosome.name }}</option>
          </select>
        </div>

        
        <div class="row">
          <div class="col">
            <label for="start" class="form-label mt-4">Start:</label>
            <input type="number" min="1" max="768075024" class="form-control" id="start" v-model="start">
          </div>
          <div class="col">
            <label for="end" class="form-label mt-4">End:</label>
            <input type="number" min="1" max="768075024" class="form-control" id="end" v-model="end">
          </div>
        </div>

        <div class="form-group">
          <label for="quality" class="form-label mt-4">Include SNPs with quality scores above:</label>
          <input type="number" class="form-control" id="quality" min="0" aria-describedby="qualityHelp" v-model="quality" >
          <small id="qualityHelp" class="form-text text-muted">Note: The overall quality score for a SNP is a Phred-like score. A value of 20 corresponds to a 1 in 100 chance of the SNP call being wrong. A value of 30 corresponds to a 1 in 1000 chance of the SNP call being wrong.</small>
        </div>

        <div class="form-group">
          <label for="sample" class="form-label mt-4">Samples to extract SNPs for:</label>
          <select class="form-select" id="sample" v-model="selectedSample">
            <option v-for="sample in sampleList" :value="sample" v-bind:key="sample.id" >{{ sample.name }}</option>
          </select>
        </div>
        
        <div class="form-group">
          <button v-if="!jobRunning" type="submit" v-on:click="onSubmit()" class="btn btn-primary mt-4">Extract</button>
          <button v-else class="btn btn-primary" type="button" disabled> 
            <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            Loading...
          </button>
        </div>
      </fieldset>
    </form>
  </div>
</template>

<script>
  import axios from 'axios';

export default {
  name: "searchPosition",

  data() {
    return {
      chromosomeList: null,
      datasetID: null,
      selectedChromosome: {},
      start: 1,
      end: 768075024,
      quality: 20,
      sampleList: null,
      selectedSample: {},
      id: null,
      interval: null,
      jsonData: null,
      jobRunning: false,
      dataset: null

    }
  },

  mounted() {
    this.datasetID = this.$route.params.dataset.id;
    axios.get("/samplelists",
      {
 
        headers: {"Content-Type":"application/json"}
      }).then(
          function(response) {
            this.sampleList = response.data;
            this.selectedSample = this.sampleList[0];
          }.bind(this)
        )
        .catch(error => {
          console.log(error)
          this.errorMsg = "Unable to retrieve a list of samplesets.";
        });

    axios.get("/refseqs",
      {
        params: {
          datasetID: this.datasetID
        },
        headers: {"Content-Type":"application/json"}
      }).then(
          function(response) {
            this.chromosomeList = response.data;
            this.selectedChromosome = this.chromosomeList[0];
          }.bind(this)
        )
        .catch(error => {
          console.log(error)
          this.errorMsg = "Unable to retrieve a list of chromosomes.";
        });
      },

  methods: {
    /*checkProgress: function() {
      axios.get("/submitPosition/" + this.id,
      {
        headers: {"Content-Type": "application/json"}
      }).then(
        function(response) {
          this.jsonData = response.data;
          
          if (this.jsonData.isRunning == false)
          {
            this.jobRunning = false;
            clearInterval(this.interval);
            //add router push here to send to results page with folder path from json
            this.$router.push({ name: 'Results', params: { jsonData: response.data }})
          }

          else {
            this.jobRunning = true;
          }
        }.bind(this)
        )
        .catch(error => {
          clearInterval(this.interval);
          console.log(error);
        });
    },*/

    onSubmit: function() {
      event.preventDefault();
      axios.get("/submitPosition",
      {
        params: {
          chromosome: this.selectedChromosome.id,
          start: this.start,
          end: this.end,
          quality: this.quality,
          sample: this.selectedSample.id,
          datasetID: this.datasetID
        },
        headers: {"Content-Type":"application/json"}
      }).then(
          function(response) {
            this.jsonData = response.data;
            this.$router.push({ name: 'Results', params: { jsonData: this.jsonData } })
           // this.checkProgress();
            //this.interval = setInterval(this.checkProgress, 5000);
          }.bind(this)
        )
        .catch(error => {
          console.log(error)
          this.errorMsg = "Unable to submit search.";
        });
    },

    
  },
  beforeDestroy() {
    clearInterval(this.interval)
  },
}
</script>