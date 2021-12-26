const image_store = {
    state: {
        images: {},
        reload: true,
        item_image: {},
        seller_info: window.sessionStorage.getItem('seller_info')
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
        SET_SELLER_INFO (state, info) {
            // state.seller_info = info
            window.sessionStorage.setItem('seller_info', info)
        }
    }
}

export default image_store