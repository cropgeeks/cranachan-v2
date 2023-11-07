<template>
  <div class="container text-light">
    <b-row>
      <h1 class="p-0">Results</h1>
    </b-row>
    <b-row>
      <table v-if="this.jsonData != []">
        <b-tr>
          <b-tr v-if="this.jsonData[0].gene != undefined">Gene</b-tr>
          <b-th>Chromosome</b-th>
          <b-th>Start</b-th>
          <b-th>End</b-th>
          <b-th># SNPs</b-th>
          <b-th>VCF</b-th>
        </b-tr>
        <b-tr v-for="result in this.jsonData" v-bind:key="result.vcfName">
          <b-tr v-if="result.gene != undefined">{{result.gene}}</b-tr>
          <b-td>{{result.chromosome}}</b-td>
          <b-td>{{result.start.toLocaleString()}}</b-td>
          <b-td>{{result.end.toLocaleString()}}</b-td>
          <b-td>{{result.noSNPs.toLocaleString()}}</b-td>
          <b-td><button v-on:click="getFile(result.vcfPath, result.vcfName)"> Get {{result.vcfName}}</button></b-td>
          <b-td></b-td>
        </b-tr>
      </table>
    </b-row>
    <p>Flapjack file download: <button v-on:click="getFile(this.jsonData[0].FJPath)"> Get {{this.jsonData[0].FJName}}</button></p>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      jsonData: []
    }
  },

  mounted() {
    if(this.$route.params.jsonData !== undefined && this.$route.params.jsonData !== null) {
      this.jsonData = this.$route.params.jsonData;
    }
  },

  methods: {
  async getFile(path, name) {
    try {
    const response = axios.get("/download",
      {
        params: {
          path: path
        },
        responseType: 'blob'
      });

      if(response.status === 200) {
          const blob = new Blob([response.data]);
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = name;
          document.body.appendChild(a);
          a.click();
          window.URL.revokeObjectURL(url);
        }
      }
      catch(error) {
          console.log(error)
          this.errorMsg = "Unable to retrieve file.";
        };
  }
}

}
</script>

<style>

</style>