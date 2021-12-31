import Vue from 'vue'
import ElementUI from 'element-ui'
import './assets/element-#FF5809/index.css';
// import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue'
import axios from 'axios'
import router from './router.js'
import Vuex from 'vuex'
import store from './utils/store'
import MintUI from 'mint-ui'

Vue.config.productionTip = false
Vue.prototype.$axios = axios
Vue.use(ElementUI)
Vue.use(Vuex)
Vue.use(MintUI)

axios.interceptors.request.use(
  config => {
    if (sessionStorage.getItem('token')) {
      config.headers.token = sessionStorage.getItem('token')
      console.log(sessionStorage.getItem('token'))
    }
    return config
  },
  error => {
    return Promise.reject(error)
  })

axios.defaults.headers.common['token'] = store.state.token


new Vue({
  el: '#app',
  store,
  components: { App },
  template: '<App/>',
  router,
  render: h => h(App),
}).$mount('#app')
