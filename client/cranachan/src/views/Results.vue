<template>
  <div class="container text-light">
    <b-row>
      <h1 class="p-0">Results</h1>
    </b-row>
    <b-row>
      <table v-if="this.jsonData != []">
        <b-tr>
          <b-th>Chromosome</b-th>
          <b-th>Start</b-th>
          <b-th>End</b-th>
          <b-th># SNPs</b-th>
          <b-th>VCF</b-th>
        </b-tr>
        <b-tr v-for="result in this.jsonData" v-bind:key="result.chromosome">
          <b-td>{{result.chromosome}}</b-td>
          <b-td>{{result.start.toLocaleString()}}</b-td>
          <b-td>{{result.end.toLocaleString()}}</b-td>
          <b-td>{{result.noSNPs.toLocaleString()}}</b-td>
          <b-td><a :href="result.vcfPath">{{result.vcfName}}</a></b-td>
        </b-tr>
      </table>
    </b-row>
  </div>
</template>

<script>
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
  }

}
</script>

<style>

</style>