import Vue from 'vue'
import Router from 'vue-router'
import login from "../components/login/login";
import register from "../components/register/register";
import xieyi from "../components/register/xieyi"
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'login',
      component: login
    },
    {
      path: '/register',
      name: 'register',
      component: register
    },
    {
      path: '/xieyi',
      name: 'xieyi',
      component: xieyi
    },
  ]
})
