import Vue from 'vue'
import App from './App'
import './assets/styles/reset.css'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import VueParticles from 'vue-particles'
import md5 from 'js-md5';

Vue.prototype.$md5 = md5;
Vue.use(ElementUI);
Vue.use(VueParticles)
Vue.prototype.$http = axios
axios.defaults.baseURL = '/api'


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
