import Vue from 'vue'
import App from './App.vue'
import router from './router'
import BootstrapVue from 'bootstrap-vue'
import Axios from 'axios'

import "bootswatch/dist/superhero/bootstrap.min.css"

Vue.use(BootstrapVue)

// Set base URL based on environment
let baseUrl = "https://ics.hutton.ac.uk/cranachan2/api/"
Axios.defaults.baseURL = (baseUrl)

Vue.config.productionTip = false

new Vue({
  router,
  baseUrl,
  render: function (h) { return h(App) }
}).$mount('#app')
