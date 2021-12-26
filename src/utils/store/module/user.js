const user = {
  state: {
    user: window.sessionStorage.getItem('user'),
    token: window.sessionStorage.getItem('token'),
    img: window.sessionStorage.getItem('img'),
    sale: []
  },

  mutations: {
    // 将token保存到sessionStorage里，token表示登陆状态
    SET_TOKEN: (state, data) => {
      state.token = data
      window.sessionStorage.setItem('token', data)
    },
    SET_USER: (state, data) => {
      state.user = data
      window.sessionStorage.setItem('user', data)
    },
    SET_USERIMG: (state, data) => {
      state.img = data
      window.sessionStorage.setItem('img', data)
    },
    SET_SALE: (state, data) => {
      state.sale = data
    },
    LOGOUT: (state) => {
      // 登出的时候要清除token
      state.token = null
      state.user = null
      window.sessionStorage.removeItem('token')
      window.sessionStorage.removeItem('user')
      window.sessionStorage.removeItem('img')
    }
  }

}

export default user
