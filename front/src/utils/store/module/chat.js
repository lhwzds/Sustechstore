const chat = {
    state: {
        chatter_info: window.sessionStorage.getItem('chatter_info')
    },

    mutations: {

        SET_CHAT_INFO (state, info) {
            state.chatter_info=info;
            window.sessionStorage.setItem('chatter_info', info)
        }
    }
}

export default chat