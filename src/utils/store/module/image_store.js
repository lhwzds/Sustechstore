const image_store = {
    state: {
        images: {},
        reload: true
    },

    mutations: {
        SET_IMG (state, dict) {
            state.images = dict
        },
        SET_RELOAD (state) {
            state.reload = false
        }
    }
}

export default image_store