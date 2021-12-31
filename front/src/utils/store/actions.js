import {
  USER_INFO
} from './mutations'

export default {
  async getUserInfo ({commit}) {
    let userInfo = {}
    const result = await this.$axios.get('/b').then(function (res) {
      if (res.status === 200) {
        this.email = res.data.email
        this.user_nickname = res.data.nick_name
        this.user_phone = res.data.telephone
      }
    })
    if (result.status === 200) {
      userInfo = result.data
      window.sessionStorage.setItem('userInfo', JSON.stringify(userInfo))
    } else {
      userInfo = JSON.parse(window.sessionStorage.getItem('userInfo'))
    }
    commit(USER_INFO, {userInfo})
  }
}
