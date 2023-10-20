<template>
  <div class="container">
    <form class="text-light">
      <fieldset>
        <legend>Setup SNP Extraction</legend>

        <div class="form-group">
          <label for="genes" class="form-label mt-4">List of Genes: </label>
          <textarea class="form-control" id="genes" rows="2" aria-describedby="geneHelp" v-model="genes"></textarea>
          <small id="geneHelp" class="form-text text-muted">Separate each gene with a semicolon ; </small>
        </div>
      
        <div class="form-group">
          <label for="extendRegion" class="form-label mt-4">Extend region upstream/downstream by (kb):</label>
          <input type="number" class="form-control" id="extendRegion" v-model="extendRegion">
        </div>

        <div class="form-group">
          <label for="quality" class="form-label mt-4">Include SNPs with quality scores above:</label>
          <input type="number" class="form-control" id="quality" min="0" aria-describedby="qualityHelp" v-model="quality" >
          <small id="qualityHelp" class="form-text text-muted">Note: The overall quality score for a SNP is a Phred-like score. A value of 20 corresponds to a 1 in 100 chance of the SNP call being wrong. A value of 30 corresponds to a 1 in 1000 chance of the SNP call being wrong.</small>
        </div>

        <div class="form-group">
          <label for="sample" class="form-label mt-4">Samples to extract SNPs for:</label>
          <select class="form-select" id="sample" v-model="selectedSample">
            <option v-for="sample in sampleList" v-bind:key="sample.id" >{{ sample.name }}</option>
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
  name: "searchGene",

  data() {
    return {
      datasetID: null,
      genes: "",
      geneArray: null,
      extendRegion: 0,
      quality: 20,
      sampleList: null,
      selectedSample: {},
      jobRunning: false,
      completeFile: null,
      jsonData: null
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
            this.selectedSample = this.sampleList[0].name;
          }.bind(this)
        )
        .catch(error => {
          console.log(error)
          this.errorMsg = "Unable to retrieve a list of samples.";
        });
      },

  methods: {
    checkProgress: function() {
      axios.get("/submitGene/checkPosition/",
      {
        params: {
          path: this.completeFile,
        },
        headers: {"Content-Type": "application/json"}
      }).then(
        function(response) {
          if (response.data == true) {
            this.jobRunning = false;
            clearInterval(this.interval);
            this.getResults();
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
    },

    getResults: function() {
      axios.get("/submitGene/getResults/",
      {
        params: {
          path: this.completeFile,
        },
        headers: {"Content-Type": "application/json"}
      }).then(
        function(response) {
          this.jsonData = response.data;
          //add router push here to send to results page with folder path from json
          this.$router.push({ name: 'Results', params: { jsonData: response.data }})
        }.bind(this)
      )
      .catch(error => {
        clearInterval(this.interval);
        console.log(error);
      });
    },

    onSubmit: function() {
      event.preventDefault();
      axios.get("/submitGene",
      {
        params: {
          geneList: this.genes,
          extendRegion: this.extendRegion,
          quality: this.quality,
          sample: this.selectedSample.id,
          datasetID: this.datasetID
        },
        headers: {"Content-Type":"application/json"}
      }).then(
          function(response) {
            this.completeFile = response.data;
            this.checkProgress();
            this.interval = setInterval(this.checkProgress, 5000);
          }.bind(this)
        )
        .catch(error => {
          console.log(error)
          this.errorMsg = "Unable to submit search.";
        });
    },

    splitGenes: function() {
      this.geneArray = this.genes.split(";");
      for(let gene in this.geneArray) {
        gene = gene.trim();
      }
    },

    extract: function() {
      this.splitGenes();
    }
  }
}
</script>