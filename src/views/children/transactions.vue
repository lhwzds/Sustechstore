<template>
  <div v-if="showcard">
    <div class="transactions">
      <el-row>
        <el-col>
          <el-card class="item-card" shadow="never" v-for="item of Sale_items" :key=item.name>
            <el-row type="flex" justify="center" align="middle">
              <el-col span:1>
                <img :src="item.img" style="height:100px;width:100px;" @click="goGoods(item.id)">
              </el-col>
              <el-col span:6 @click="goGoods(item.id)">
                {{item.name}}
              </el-col>
              <el-col span:16 :offset=1 @click="goGoods(item.id)">
                {{item.description}}
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'transactions',
  data () {
    return {
      inf: [],
      Sale_items: [],
      showcard: false
    }
  },
  methods: {
    go_path () {
      this.$router.push('/center')
    },
    goGoods(id) {
            this.$store.commit('SET_RELOAD', true)
            this.$store.commit('SET_ITEM_ID', id)
            this.$store.commit('SET_SLO_IND', 4)
            this.$router.push('/goods')
    }
  },
  async created () {
    const that = this
    await that.$axios.post('/root'+'/items/query').then(function (res) {
      if (res.status === 200) {
        that.inf = res.data
      }
    })
    for (let i = 0; i < that.inf.length; i++) {
      let imgName = that.inf[i].fileList
      if (imgName.length > 0) {
        await that.$axios.get('/root' + '/items/picture/' + imgName[0]).then(function (res) {
          if (res.status === 200) {
            let ob = {}
            ob.name = that.inf[i].name
            ob.description = that.inf[i].description
            ob.img = res.data
            ob.id = that.inf[i].id
            that.Sale_items[i] = ob
          }
        })
      } else {
        let ob = {}
        ob.name = that.inf[i].name
        ob.description = that.inf[i].description
        ob.img = './image/logo.png'
        that.Sale_items[i] = ob
      }
    }
    that.showcard = true
  }
}
</script>

<style scoped>
.transactions {
  width: 75%;
  height: 100%;
  margin-left: 150px;
  margin-top: 20px;
}
.item-card {
  margin-left: 80px;
  margin-right: 80px;
  height: 10%;
  border-radius:20px;
}
</style>
