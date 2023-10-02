<template>
  <div class="card-group">
    <div class="card text-white border-secondary mb-3" style="margin: 5px" v-for="dataset in datasets" v-bind:key="dataset.description.id">
      <div class="card-header">
        <h5 class="card-title">{{ dataset.description.project }}</h5>
      </div>
      <div class="card-body">
        <p class="card-text"><b>Description:</b> {{ dataset.description.description }}</p>
        <p class="card-text"><b>Samples:</b> {{ dataset.description.samples.toLocaleString() }}</p>
        <p class="card-text"><b>Markers:</b> {{ dataset.description.markers.toLocaleString() }}</p>
        <p class="card-text"><b>Published?</b> {{ dataset.description.publicationStatus}}</p>
        <p class="card-text"><b>Data Owner:</b> {{ dataset.description.dataOwner[0]}}</p>
        <p class="card-text"><b>Bioinformatics Contact:</b> {{ dataset.description.bioinformaticsContact}}</p>
      </div>
      <div class="card-footer">
        <button type="button" class="btn btn-primary" @click="datasetSelected(dataset)">Select Dataset</button>
      </div>
    </div>
  </div>

</template>


<script>
  import axios from 'axios'
  
  export default {
    name: "datasets",

    data() {
      return {
        datasets: null
      }
    },
  
    mounted() {
    axios.get("/datasets",
      {
        headers: {"Content-Type":"application/json"}
      }).then(
          function(response) {
            this.datasets = response.data;
          }.bind(this)
        )
        .catch(error => {
          console.log(error)
          this.errorMsg = "Unable to retrieve a list of datasets.";
        });
      },

    methods: {
      datasetSelected: function(dataset) {
        this.$router.push({ name: 'Disclaimer', params: { dataset: dataset } })
      }
  }
    }

    


</script>