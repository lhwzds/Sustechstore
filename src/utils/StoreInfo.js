const LOCAL_STORAGE_KEY = "searchHistory";

class StoreInfo { }

StoreInfo.saveHistory = (arr) => {
    localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(arr));
}

StoreInfo.loadHistory = () =>JSON.parse(localStorage.getItem(LOCAL_STORAGE_KEY))

StoreInfo.removeAllHistory=()=>{localStorage.removeItem(LOCAL_STORAGE_KEY)}


module.exports = StoreInfo