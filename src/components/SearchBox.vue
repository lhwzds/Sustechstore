<template>
  <div style="background:#eeeeee">
    <el-row type="flex" justify="center" style="height:80px">
      <el-col :span="10" class="center" >
        <el-input
          v-model="search"
          @focus="focus"
          @blur="blur"
          @keyup.enter.native="searchHandler"
          placeholder="你想买什么，你想卖什么？"
        >
          <el-button slot="append" icon="el-icon-search" id="search" @click="searchHandler">搜索</el-button>
        </el-input>
        <!---设置z-index优先级,防止被走马灯效果遮挡-->
        <el-card
          @mouseenter="enterSearchBoxHanlder"
          v-if="isSearch"
          class="box-card"
          id="search-box"
          style="z-index:20"
        >
          <dl v-if="isHistorySearch">
            <dt class="search-title" v-if="history">历史搜索</dt>
            <el-button class="remove-history" v-if="history" @click="removeAllHistory" icon="el-icon-delete">
              清空历史记录
            </el-button>
            <el-tag
              v-for="search in historySearchList"
              :key="search.id"
              closable
              :type="search.type"
              @close="closeHandler(search)"
              style="margin-right:5px; margin-bottom:5px; cursor:pointer;"
              @click.native="inputHistory(search.name)"
            >{{search.name}}</el-tag>
            <dt class="search-title">热门搜索</dt>
            <dd v-for="search in hotSearchList" :key="search.id">{{search}}</dd>
          </dl>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import StoreInfo from "../utils/StoreInfo";
export default {
  data() {
    return {
      search: "", //当前输入框的值
      isFocus: false, //是否聚焦
      hotSearchList: ["暂无热门搜索"], //热门搜索数据
      historySearchList: [], //历史搜索数据
      history: false
    };
  },
  inject: ["reload"],
  methods: {
    focus() {
      this.isFocus = true;
      this.historySearchList =
        StoreInfo.loadHistory() == null ? [] : StoreInfo.loadHistory();
      this.history = this.historySearchList.length == 0 ? false : true;
    },
    blur() {
      var self = this;
      this.searchBoxTimeout = setTimeout(function() {
        self.isFocus = false;
      }, 300);
    },
    enterSearchBoxHanlder() {
      clearTimeout(this.searchBoxTimeout);
    },
    searchHandler() {
      let exist =
        this.historySearchList.filter(value => {
          return value.name == this.search;
        }).length == 0
          ? false
          : true;
      if (!exist) {
        this.historySearchList.push({ name: this.search, type: "info" });
        StoreInfo.saveHistory(this.historySearchList);
      }
      this.history = this.historySearchList.length == 0 ? false : true;
      this.$store.commit('SET_KEY', this.search)
      this.$store.commit('SET_SLO_IND', 1)
      this.$router.push('/overview')
      this.reload()
    },
    closeHandler(search) {
      this.historySearchList.splice(this.historySearchList.indexOf(search), 1);
      StoreInfo.saveHistory(this.historySearchList);
      clearTimeout(this.searchBoxTimeout);
      if (this.historySearchList.length == 0) {
        this.history = false;
      }
    },
    removeAllHistory() {
      StoreInfo.removeAllHistory();
    },
    inputHistory(name) {
      this.search = name;
    }
  },
  computed: {
    isHistorySearch() {
      return this.isFocus && !this.search;
    },
    isSearch() {
      return this.isFocus && !this.search;
    }
  }
};
</script>

<style>
.center {
  margin-top: 20px;
  margin-bottom: 20px;
}
#search {
  background-color: #FF5809;
  border-radius: 0%;
  color: white;
}
.search-title {
  color: #bdbaba;
  font-size: 15px;
  margin-bottom: 5px;
}
.remove-history {
  color: #bdbaba;
  font-size: 15px;
  float: right;
  margin-top: -22px;
}
#search-box {
  width: 100%;
  height: 300px;
  margin-top: 0px;
  padding-bottom: 20px;
  text-align: left;
}
</style>