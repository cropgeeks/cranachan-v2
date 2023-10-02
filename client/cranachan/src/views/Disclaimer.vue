<template>
  <div class="container text-light">
    <h1>Cranachan - Data Usage Disclaimer</h1>

    <p>Putting in description until disclaimer is part of dataset!</p>

    <p v-if="this.dataset.description.description != null && this.dataset.description.description != undefined">{{this.dataset.description.description}}</p>

    <div class="form-check">
      <input type="checkbox" class="form-check-input" id="disclaimerCheck" v-model="disclaimerCheck">
      <label class="form-check-label" for="disclaimerCheck">I acknowledge having read and accepted the publication conditions above</label>
    </div>

    <button type="button" class="btn btn-primary" disabled v-if="!disclaimerCheck">Proceed</button>
    <button type="button" class="btn btn-primary" @click="disclaimerAgreed()" v-else>Proceed</button>
  </div>
</template>


<script>
  import axios from 'axios'
  
  export default {
    name: "disclaimer",

    

    data() {
      return {
        dataset: {
          description: {
            description: null
          }
        },
        disclaimerCheck: false
      }
    },
  
    mounted() {
      this.dataset = this.$route.params.dataset

      if(this.dataset == null){
        this.$router.push({ name: 'Datasets' })
      }
      else if(this.dataset.description.description == null) {
        this.$router.push({ name: 'Search', params: { dataset: dataset } })
      }
    
      },

    methods: {
      disclaimerAgreed: function() {
        this.$router.push({ name: 'Search', params: { dataset: this.dataset } })
      }
    }
  }

    


</script>