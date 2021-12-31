const SearchInfo = {
    state: {
        Keyword: '',
        slogan_index: 0,
        item_id: ''
    },

    mutations: {
        SET_KEY (state, keyword) {
            state.Keyword = keyword
        },
        SET_SLO_IND (state, index) {
            state.slogan_index = index
        },
        SET_ITEM_ID (state, id) {
            state.item_id = id
        }
    }
}

export default SearchInfo