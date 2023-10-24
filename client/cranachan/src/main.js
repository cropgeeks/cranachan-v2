import Vue from 'vue'
import App from './App.vue'
import router from './router'
import BootstrapVue from 'bootstrap-vue'
import Axios from 'axios'

import "bootswatch/dist/superhero/bootstrap.min.css"

Vue.use(BootstrapVue)

// Set base URL based on environment
let baseUrl = process.env.VUE_APP_BASE_URL
console.log(baseUrl)
Axios.defaults.baseURL = ("https://ics.hutton.ac.uk/cranachan2/api/")

console.log("Axios URL " + axiosDefaults.baseURL)
Vue.config.productionTip = false

new Vue({
  router,
  baseUrl,
  render: function (h) { return h(App) }
}).$mount('#app')
