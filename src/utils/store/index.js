import Vue from 'vue'
import Vuex from 'vuex'
import user from './module/user'
import mutations from './mutations'
import state from './states'
import getters from './getters'
import actions from './actions'
import SearchInfo from './module/SearchInfo'
import image_store from './module/image_store'
import chat from './module/chat'
Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user,
    SearchInfo,
    image_store,
    chat
  },
  mutations,
  state,
  getters,
  actions,

})

export default store
