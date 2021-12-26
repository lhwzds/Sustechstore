const image_store = {
    state: {
        images: {},
        reload: true,
        item_image: {}
    },

    mutations: {
        SET_IMG (state, dict) {
            for(let key in Object.keys(dict)){
                state.images[key] = dict[key]
            }
        },
        SET_RELOAD (state, bool) {
            state.reload = bool
        },
        SET_ADD_ITEM (state, dict) {
            state.item_image = dict
        },
    }
}

export default image_store