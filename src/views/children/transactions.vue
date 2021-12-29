<template>
  <div v-if="showcard">
    <div class="transactions">
      <el-row type="flex">
        <el-col :span="4" :offset="2" class="trans_title">商品图片</el-col>
        <el-col :span="4" :offset="1" class="trans_title">商品名称</el-col>
        <el-col :span="6" :offset="2" class="trans_title">商品描述</el-col>
        <el-col :span="3" :offset="2" class="trans_title">商品状态</el-col>
      </el-row>
          <el-card class="item-card" shadow="never" v-for="item of Sale_items" :key=item.id>
            <el-row type="flex" align="middle">
              <el-col :span="4" >
                <img :src="item.img" style="height:100px;width:100px;">
              </el-col>
              <el-col :span="6" style="text-align:left;">
                {{item.name}}
              </el-col>
              <el-col :span="9" style="text-align:left;">
                {{item.description}}
              </el-col>
            </el-row>
          </el-card>
        
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
.trans_title {
  font-size: 20px;
  font-weight: bold;
}
</style>
