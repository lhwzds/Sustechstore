<template>
  <div class="saler">
    <el-backtop></el-backtop>
    <InfHead></InfHead>
    <div class="seller_info" >
      <el-row type="flex" justify="center">
        <el-card class="seller-card">
        <el-row type="flex" justify="center" class="detail_info">
        <el-col :span="10">
          <el-row style="margin-top:32px;">
            <div class="avatar-brand">
              <el-avatar :size="100" :src="img"></el-avatar>
            </div>
          </el-row>
          <el-row style="margin-top:15px;">
            <i class="item_title">{{seller_info.publisher}}</i>
          </el-row>
        </el-col>
        <el-col :span="14" style="text-align:left">
          <el-row>
            <span class="item_title">性别: </span>
            <span>{{seller_info.gender}}</span>
          </el-row>
          <el-row style="margin-top:15px;">
            <span class="item_title">邮箱: </span>
            <span>{{seller_info.email}}</span>
          </el-row>
          <el-row style="margin-top:15px;">
            <span class="item_title">联系电话: </span>
            <span>{{seller_info.telephone}}</span>
          </el-row>
          <el-row style="margin-top:15px;">
            <span class="item_title">正在售卖商品的数量: </span>
            <span>{{items.length}}</span>
          </el-row>
          <el-row style="margin-top:15px;">
            <span class="item_title">信用等级: </span>
            <span>100</span>
          </el-row>
        </el-col>
      </el-row>
      </el-card>
      </el-row>
      
      
      
      <div class="show" v-if="showCard">
      <el-row type="flex" justify="center">
        <h3>正在出售的商品</h3>
      </el-row>
      <el-row type="flex" justify="center">
        <el-col :span="24">
          <el-card class="sell_info" v-for="item in items" :key="item.id">
            <el-row type="flex" justify="center">
              <el-col :span="6">
                <img :src="item.img" @click="go_goods(item)" class="item_imgs">
              </el-col>
              <el-col :span="18">
                <el-row class="item_title" @click="go_goods(item)">
                  {{item.name}}
                </el-row>
                <el-row class="item_title" style="font-weight:normal;">
                  商品简介: 
                </el-row>
                <el-row class="item_text">
                  {{item.description}}
                </el-row>
                    </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import InfHead from './InfHead'
export default {
  name: 'saler',
  components: {InfHead},
  data () {
    return {
      image: '',
      name: '',
      birthday: '',
      id: '',
      items: [],
      inf: [],
      img: null,
      showCard: false,
      seller_info: {'publisher':'?'}
    }
  },
  methods: {
    go_goods (item) {
      this.$store.commit('SET_RELOAD', true)
      this.$store.commit('SET_ITEM_ID', item.id)
      this.$store.commit('SET_SLO_IND', 4)
      this.$router.push('/goods')
    }
  },
  async created () {
    const that = this
    this.image = this.$store.state.image_store.seller_info.avatar
    this.name = this.$store.state.image_store.seller_info.name
    this.birthday = this.$store.state.image_store.seller_info.birthday
    this.id = this.$store.state.image_store.seller_info.id
    this.seller_info = this.$store.state.image_store.seller_info
    console.log(this.seller_info)
    await this.$axios.get('/root' + '/items/picture/' + this.image).then(function (res) {
      if (res.status === 200) {
        that.img = res.data}
    }).catch(function (error) {
      console.log(error)
    })
    await this.$axios.post('/root' + '/items/query', this.id).then(function (res) {
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
            that.items[i] = ob
          }
        })
      } else {
        let ob = {}
        ob.name = that.inf[i].name
        ob.description = that.inf[i].description
        ob.img = './image/logo.png'
        that.items[i] = ob
      }
    }
    console.log(this.items)
     that.showCard = true
  },
}
</script>

<style scoped>
.avatar-brand{
  position: relative;
  border-radius: 50px;
}
.show {
  height: 400px;
  margin-left: 200px;
  margin-right: 200px;
}
.seller_info {
  margin-top: 20px;
}
.sell_info {
  margin-bottom: 10px;
  border-radius: 10px;
}
.item_title {
  margin-top:15px;
  font-size:20px;
  text-align:left;
  font-weight: bold;
  cursor: pointer;
}
.item_text {
  margin-top: 15px;
  color: darkgray;
  text-align: left;
}
.item_imgs {
  width:200px;
  height:200px;
  cursor: pointer;
}
.detail_info {
  border-color: black;
}
.seller-card {
  width: 42%;
  border-radius: 10px;
}
</style>
