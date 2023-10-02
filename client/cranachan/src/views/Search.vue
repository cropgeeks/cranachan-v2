<template>
  <div class="container">
    <b-row>
      <b-col class="col-8">
        <b-card no-body>
          <b-tabs pills card>
            <b-tab title="By Gene" active>
              <b-card-text>
                <SearchGene/>
              </b-card-text>
            </b-tab>
                
            <b-tab title="By Position">
              <b-card-text>
                <SearchPosition/>
              </b-card-text>
            </b-tab>
          </b-tabs>
        </b-card>
      </b-col>

      <b-col class="col-4">
        <ChromosomeTable/>
      </b-col>

    </b-row>
    
  </div>
</template>

<script>
import ChromosomeTable from '../components/ChromosomeTable.vue';
import SearchGene from '../components/SearchGene.vue';
import SearchPosition from '../components/SearchPosition.vue';

export default {
  components: { SearchGene, ChromosomeTable, SearchPosition },
  data() {
      return {
        selectedTab: "geneSearch"
      }
    },

}
</script>

<style>

</style>