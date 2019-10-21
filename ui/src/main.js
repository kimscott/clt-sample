// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import axios from 'axios'

Vue.use(Vuetify)
Vue.config.productionTip = false

Vue.prototype.$http = axios

if (process.env.NODE_ENV === 'development') {
  window.API_HOST = 'http://a5b866195f0b411e99966069ce967329-941185380.ap-northeast-2.elb.amazonaws.com:8080'
  // window.API_HOST = 'http://localhost:8080'
}

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
