<template>
  <table class="table text-center" v-if="this.chromosomeList != null">
    <thead>
      <tr>
        <th>Chromosome</th>
        <th>Length</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="chromosome in this.chromosomeList" v-bind:key="chromosome.id">
        <td>{{ chromosome.name }}</td>
        <td>{{ chromosome.length.toLocaleString() }}</td>
      </tr>
      
    </tbody>
    
  </table>
</template>

<script>
  import axios from 'axios';

export default {
  data() {
    return {
      chromosomeList: null,
      datasetID: null
    }
  },
mounted() {
  this.datasetID = this.$route.params.dataset.id;
  axios.get("https://ics.hutton.ac.uk/cranachan2/api/refseqs",
    {
      params: {
          datasetID: this.datasetID
        },
      headers: {"Content-Type":"application/json"}
    }).then(
        function(response) {
          this.chromosomeList = response.data;
        }.bind(this)
      )
      .catch(error => {
        console.log(error)
        this.errorMsg = "Unable to retrieve a list of chromosomes.";
      });
    },
}
</script>

<style>

</style>