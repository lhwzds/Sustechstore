import Vue from 'vue'
import Router from 'vue-router'
import home from './views/Home.vue'
import overview from './views/Overview.vue'
import post from './views/Post.vue'
import trolley from './views/Trolley.vue'
import Login from './views/Login.vue'
import Register from './views/Register.vue'
import center from './views/Center.vue'
import information from './views/children/information.vue'
import changeData from './views/children/changeData'
import Goods from './views/Goods.vue'
import Chatting from './views/Chatting.vue'
import blank from './views/blank.vue'
 
Vue.use(Router)

const originalPush = Router.prototype.push

Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}
 
export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'HomePage',
      component: home
    },
    {
      path: '/overview',
      name: 'OverviewPage',
      component: overview
    },
    {
      path: '/postInfo',
      name: 'PostPage',
      component: post
    },
    {
      path: '/trolley',
      name: 'TrolleyPage',
      component: trolley
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    },
    {
      path: '/center',
      component: center,
      children: [
        {path: 'information', component: information},
        {path: '/center', redirect: '/center/information'},
        {path: 'changeData', component: changeData}
      ]
    },
    {
      path: '/goods',
      name: 'Goods',
      component: Goods
    },
    {
      path: '/chat',
      name: 'Chatting',
      component: Chatting
    },
    {
      path: '/blank',
      name: '',
      component: blank
    }
  ]
})
