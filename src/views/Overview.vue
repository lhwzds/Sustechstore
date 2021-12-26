<template>
  <div class="overview-main">

    <el-backtop></el-backtop>

    <HeaderBar></HeaderBar>

    <SearchBox style="z-index:20;position:absolute;width:100%"></SearchBox> 

    <el-card id="overview-card">
      <el-row type="flex" justify="center">
        <el-col>
          <h3 id="overview-title">以下为商品: “{{search_name}}”搜索结果:</h3>
        </el-col>
      </el-row>

      <el-row type="flex" justify="center">
        <el-col>
          <el-card id="select-card">
            <el-row type="flex" justify="left" class="select-line">
                <h5 style="margin-top:10px;margin-bottom:0px">分类筛选: </h5>
            </el-row>

            <el-row type="flex" justify="center" class="select-line">
              <el-col :span="4">
                价格: <br>
              </el-col>
              <el-col :span="20">
                <el-radio v-model="price_radio" label="1">100元以内</el-radio>
                <el-radio v-model="price_radio" label="2">100~500元</el-radio>
                <el-radio v-model="price_radio" label="3">500~1000元</el-radio>
                <el-radio v-model="price_radio" label="4">1000元以上</el-radio>
              </el-col>
            </el-row>

            <!-- <el-divider></el-divider>

            <el-row type="flex" justify="center" class="select-line">
              <el-col :span="4">
                尺码大小: <br>
              </el-col>
              <el-col :span="20">
                <el-radio v-model="size_radio" label="1">S</el-radio>
                <el-radio v-model="size_radio" label="2">M</el-radio>
                <el-radio v-model="size_radio" label="3">L</el-radio>
                <el-radio v-model="size_radio" label="4">XL</el-radio>
                <el-radio v-model="size_radio" label="5">XXL</el-radio>
                <el-radio v-model="size_radio" label="6">XXXL</el-radio>
                <el-radio v-model="size_radio" label="7">均码</el-radio>
              </el-col>
            </el-row>

            <el-divider></el-divider> -->

            <el-row type="flex" justify="center" class="select-line">
              <!-- <el-col :span="4">
                物品状态: <br>
              </el-col>
              <el-col :span="16">
                <el-radio v-model="quaility_radio" label="1">一手商品</el-radio>
                <el-radio v-model="quaility_radio" label="2">崭新出厂</el-radio>
                <el-radio v-model="quaility_radio" label="3">略有磨损</el-radio>
                <el-radio v-model="quaility_radio" label="4">差强人意</el-radio>
              </el-col> -->
              <el-col :span="4" :offset="20">
                <el-button>启用筛选</el-button>  
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row type="flex" justify="center">
        <el-col>
          <el-card shadow="never" class="item-card" v-for="item in items" :key=item.id>
            <el-row type="flex" justify="center" style="margin-top:20px">

              <el-col :span="6">
                <img :src="dictionary[item.id]" class="merchandise-img" @click="goGoods"> 
              </el-col>

              <el-col :span="18">
                <el-row type="flex" justify="center">
                  <el-col>
                    <h3 class="merchandise-name" @click="goGoods(item.id)">{{item.name}}</h3> 
                  </el-col>
                </el-row>

                <el-row>
                  <el-col>
                    <h3 class="merchandise-name">卖家: {{item.publisher}}</h3>
                  </el-col>
                </el-row>

                <p class="merchandise-text">商品简介: {{item.description}}</p>

                <el-row type="flex" justify="center" style="height:40px" align="middle">
                  <el-col :span="16">
                    <h3 class="merchandise-price">￥{{item.price}}</h3> 
                  </el-col>
                  <el-col :span="4">
                    <el-button style="height:100%" plain @click="addTrolley(item.id)">加入购物车</el-button>
                  </el-col>
                  <el-col :span="4">
                    <el-button style="height:100%" @click="buyItem(item.id)">立即购买</el-button>
                  </el-col>
                </el-row>
                 
              </el-col>

            </el-row>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <el-dialog title="购买商品" :visible.sync="dialogVisible" width="50%" center>
      <el-row type="flex">您想要购买的商品为: {{item_buy.name}}</el-row>
      <el-row type="flex" style="margin-top:20px;">价格: ￥{{item_buy.price}}</el-row>
      <el-row type="flex" style="margin-top:20px;">交易地点(请先和卖家商量好哦!):</el-row>
      <el-input maxlength="150" type="textarea" autosize :rows="2" placeholder="请输入地点" v-model="location"></el-input>
      <el-dialog width="30%" title="购买结果" :visible.sync="innerVisible" append-to-body>
        <el-row type="flex">{{result_txt}}</el-row>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="innerVisible = false">{{button_text}}</el-button>
        </span>
      </el-dialog>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消购买</el-button>
        <el-button type="primary" @click="sendBuyInfo">确定购买</el-button>
      </span>
    </el-dialog>

    <FootBar></FootBar>
  </div>
</template>

<script>
import SearchBox  from '../components/SearchBox.vue'
import HeaderBar from '../components/HeaderBar.vue'
import FootBar from '../components/FootBar.vue'
import axios from 'axios'

    export default {
        data() 
        {
            return {
                search_name: "",
                price_radio: '',
                size_radio: '',
                quaility_radio: '',
                items: null,
                dictionary: {},
                first: false,
                money:null,
                dialogVisible: false,
                innerVisible: false,
                location:'',
                answer: null,
                result_txt: '',
                button_text: '',
                id: '0',
                item_buy: {'name':'','price':'','id':''},
            };
        },
        methods: {
          goGoods(id) {
            this.$store.commit('SET_RELOAD', true)
            this.$store.commit('SET_SLO_IND', 4)
            this.$store.commit('SET_ITEM_ID', id)
            this.$router.push('/goods')
          },
          async addTrolley(id) {
            if(this.$store.state.user.user == null) {
              this.$confirm("您还未登录，请先登录再将商品加入购物车", '警告', {
              confirmButtonText: '确定',
              type: 'warning'})
            }
            else {
              let fileList = new FormData()
              fileList.append('itemId', id)
              await axios({
                url:"/root"+'/trolley/add',
                method: 'post',
                data: fileList
              }).then(res => (console.log(res)))
              this.$confirm('加入购物车成功! 是否前往购物车查看？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'success'
              }).then(() => {
                this.$router.push('/trolley')
              })
            }
          },
          async buyItem(id) {
            this.id = id
            if(this.$store.state.user.user == null) {
              this.$confirm("您还未登录，请先登录再购买商品", '警告', {
              confirmButtonText: '确定',
              type: 'warning'})
            }
            else {
              await axios({
                url:"/root"+'/getmoney',
                method: 'get'
              }).then(res => (
                this.money = parseFloat(res.data) 
              ))
              console.log(this.money)
              for(let i=0;i<this.items.length;i++) {
                if(this.items[i].id == id) {
                  this.item_buy = this.items[i]
                  break
                }
              }
              if(this.money < parseFloat(this.item_buy.price)) {
                this.$confirm("您的账户余额不足，请充值后再进行购买! ", '警告', {
                  confirmButtonText: '确定',
                  type: 'warning'})
              }
              else {
                this.dialogVisible = true
              }
            }
          },
          async sendBuyInfo() {
            let fileList =  new FormData()
            fileList.append('itemID', this.id)
            fileList.append('location', this.location)
            await axios({
                url:"/root"+'/trolley/buy',
                method: 'post',
                data: fileList
            }).then(res => {
              this.answer = res.data
              this.innerVisible = true
              this.dialogVisible = false
            })
            if (this.answer == "1"){
              this.result_txt = "购买成功!一封确认邮件已经发至您的邮箱!"
              this.button_text = "太棒了!"
            }
            else {
              this.result_txt = "购买失败，好像出了点小问题..."
              this.button_text = "啊这..."
            }
          }
        },
        components: {
          SearchBox,
          HeaderBar,
          FootBar
        },
        async mounted() {
          this.search_name = this.$store.state.SearchInfo.Keyword
          this.first = this.$store.state.image_store.reload
          let formdata = new FormData()
          formdata.append("searchText", this.search_name)
          await axios({
            url:"/root"+"/search",
            method:'post',
            data:formdata,
          }).then(res => (this.items = res.data))
          let i = 0
          for(i;i<this.items.length;i++){
            if(this.items[i].fileList.length > 0){
              let pic_name = this.items[i].fileList[0]
              let item_id = this.items[i].id
              await this.$axios.get("/root"+"/items/picture/"+pic_name)
              .then((result) => {
                this.dictionary[item_id] = result.data
              })
            }
          }
          await this.$store.commit('SET_IMG', this.dictionary)
          if(this.first){
            this.$router.push('/blank')
            this.$store.commit('SET_RELOAD', false)
          }
        },
        updated() {
          this.dictionary = this.$store.state.image_store.images
        }
    }
</script>

<style>
  #overview-card {
    width: 75%;
    border-radius: 15px;
    margin: auto;
    position: absolute;
    left: 12.5%;
    margin-top: 80px;
    z-index: 5;
  }
  #overview-title {
    text-align: left;
    margin-left: 40px;
  }
  #select-card {
    border-color: darkgray;
    margin-bottom:20px;
    border-radius: 0px;
  }
  .el-card__body {
    padding-top: 0px;
    padding-bottom: 10px;
  }
  .select-line {
    margin-bottom:10px;
    margin-top:10px;
    text-align:left;
  }
  .item-card{
    background:#fbfbfb;
    width: 100%;
    display: inline-block;
    margin: 0.3em;
    border-radius:20px;
  }
  .merchandise-text{
    text-align: left;
    color: darkgrey;
    margin-top: 0px;
    height:60px
  }
  .el-divider {
    margin-top: 10px;
    margin-bottom: 10px;
  }
  .merchandise-img {
    cursor: pointer;
  }
  .merchandise-name{
    font-size:20px;
    text-align:left;
    font-weight:bold;
    margin-top:0px;
    margin-bottom: 10px;
    cursor: pointer;
  }
  .merchandise-price{
    text-align:left;
    color:#FF5809;
    margin-top:20px;
    margin-bottom:20px;
    font-weight: bold;
    font-size: 24px;
    cursor: pointer;
  }
</style>