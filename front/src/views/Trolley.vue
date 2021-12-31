<template>
  <div class="trolley-main"  style="position:absolute;z-index:1;width:100%">

    <el-backtop></el-backtop>

    <HeaderBar></HeaderBar>

    <el-card id="trolley-card">
      <el-row type="flex" justify="center">
        <h2 id="trolley-title">购物车</h2>
      </el-row>

      <el-row type="flex" justify="center" align="middle">
        <el-col :span="1" :offset="1">
          <el-checkbox label="全选" v-model="all_selected" @change="check_select"></el-checkbox>
        </el-col>
        <el-col :span="7">
          <h3>商品信息</h3>
        </el-col>
        <el-col :span="4">
          <h3>卖家</h3>
        </el-col>
        <el-col :span="4">
          <h3>价格</h3>
        </el-col>
        <el-col :span="7">
          <h3>操作</h3>
        </el-col>
      </el-row>

      <el-divider></el-divider>

      <el-row type="flex" justify="center" v-if="isEmpty" style="height:600px">
        <h1 style="line-height:600px;color:darkgrey;">购物车中暂无商品</h1>
      </el-row>
      
      <el-row type="flex" justify="center">
        <el-col>
          <el-card shadow="never" class="item-card" v-for="item in items" :key=item.id>
            <el-row type="flex" justify="center" align="middle"> 
              <el-col :span="1">
                <el-checkbox v-model="selected[item.id]" class="select_button" @change="count_sum"></el-checkbox>  
              </el-col>

              <el-col :span="4">
                <img :src="dictionary[item.id]" class="trolley-img" @click="goItem(item.id)"> 
              </el-col>

              <el-col :span="4">
                <p style="text-align:left;cursor: pointer;" @click="goItem(item.id)">{{item.name}}</p>
              </el-col>

              <el-col :span="4">
                <p>{{item.publisher}}</p>
              </el-col>

              <el-col :span="4" style="color:#FF5809;">
                <h3>￥{{item.price}}</h3>
              </el-col>

              <el-col :span="7">
                <el-row style="margin-bottom:10px">
                  <el-button @click="goItem(item.id)">查看详情</el-button>  
                </el-row>
                <el-row>
                  <el-button @click="single_delete(item.id)">删除商品</el-button>
                </el-row>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <el-dialog title="购买商品" :visible.sync="dialogVisible" width="50%" :modal-append-to-body = "false" @close='clear_list' center>
      <el-row type="flex">您想要购买的商品为: </el-row>
      <el-row type="flex" v-for="id in buy_list" :key="id" style="margin-top:20px;">
        {{items[id].name}}
      </el-row>
      <el-row type="flex" style="margin-top:20px;">价格: ￥{{sum}}</el-row>
      <el-row type="flex" style="margin-top:20px;">交易地点(请先和卖家商量好哦!):</el-row>
      <el-input maxlength="150" type="textarea" autosize :rows="2" placeholder="请输入地点" v-model="location"></el-input>
      <el-dialog width="30%" title="购买结果" :visible.sync="innerVisible" :modal-append-to-body = "false" @close='clear_list' append-to-body>
        <el-row type="flex">{{result_txt}}</el-row>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="innerVisible = false">{{button_text}}</el-button>
        </span>
      </el-dialog>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible=false">取消购买</el-button>
        <el-button type="primary" @click="sendBuyInfo">确定购买</el-button>
      </span>
    </el-dialog>

    <FootBar></FootBar>
    <el-row type="flex" justify="left">
      <el-col :span="18" :offset="3">
        <el-row  class="count-bar" type="flex" justify="center" align="middle">
          <el-col :span="2" :offset="0.5">
            <el-checkbox label="全选" v-model="all_selected" @change="check_select"></el-checkbox>
          </el-col>
          <el-col :span="2">
            <el-button class="count-button" @click="multiple_delete">删除商品</el-button>
          </el-col>
          <el-col :span="4" :offset="8">
            <h4>已选商品{{merchan_count}}件</h4>
          </el-col>
          <el-col :span="4">
            <h4>合计: {{sum}}元</h4>
          </el-col>
          <el-col :span="3.5">
            <el-button class="count-button" style="width:100px;background:#FF5809" type="primary" @click="buyItems">
              <h3 style="color:white;margin-top:0;margin-bottom:0;">结算</h3>  
            </el-button>
          </el-col>
        </el-row>  
      </el-col>
    </el-row>
  </div>
</template>

<script>
import HeaderBar from '../components/HeaderBar.vue'
import FootBar from '../components/FootBar.vue'
import axios from 'axios'

    export default {
        data() 
        {
            return {
                all_selected: false,
                merchan_count: 0,
                sum: 0,
                selected: {},
                items:[],
                isEmpty: true,
                dictionary: {},
                money:null,
                dialogVisible: false,
                innerVisible: false,
                location:'',
                answer: null,
                result_txt: '',
                button_text: '',
                buy_list: new Array(),
                send_list: new Array(),
                first: false
            };
        },
        components: {
          HeaderBar,
          FootBar
        },
        methods: {
          check_select() {
            if(this.all_selected){
              for(let i = 0; i < this.items.length; i++){
                this.selected[this.items[i].id] = true  
              } 
            }
            else{
              for(let i = 0; i < this.items.length; i++){
                this.selected[this.items[i].id] = false  
              } 
            }
            this.count_sum()
          },
          count_sum() {
            let all = 0
            let count = 0
            for(let i = 0; i < this.items.length; i++){
                if(this.selected[this.items[i].id]){
                    count++
                    all += parseInt(this.items[i].price) 
                }
            }
            this.merchan_count = count
            this.sum = all
            let select_all = true
            for(let i = 0; i < this.items.length; i++){
              if(!this.selected[this.items[i].id]){
                select_all = false
                break
              } 
            }
            if(select_all){
              this.all_selected = true
            }
            else{
              this.all_selected = false
            }
          },
          async single_delete(id) {
            let fileList = new FormData()
            let list = new Array()
            list[0] = id
            fileList.append('itemId', list) 
            await axios({
              url: "/root"+"/trolley/remove",
              method: 'post',
              data: fileList
            }).then(res => (console.log(res)))
            for(let i = 0; i < this.items.length; i++){
                if(this.items[i].id == id){
                  this.items.splice(i, 1)
                  delete this.selected[id]
                  break
                }
            }
          },
          multiple_delete() {
            let fileList = new FormData()
            let list = []
            for(let i = 0; i < this.items.length; i++){
                if(this.selected[this.items[i].id]){
                  list.push(this.items[i].id)
                  delete this.selected[this.items[i].id]
                }
            }
            fileList.append('itemId', list)
            axios({
              url: "/root"+"/trolley/remove",
              method: 'post',
              data: fileList
            }).then(res => (console.log(res)))
            this.$router.push('/blank')
          },
          goItem(id) {
            this.$store.commit('SET_RELOAD', true)
            this.$store.commit('SET_SLO_IND', 4)
            this.$store.commit('SET_ITEM_ID', id)
            this.$router.push('/goods')
          },
          async buyItems() {
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
              if(this.money < parseFloat(this.sum)) {
                this.$confirm("您的账户余额不足，请充值后再进行购买! ", '警告', {
                  confirmButtonText: '确定',
                  type: 'warning'})
              }
              else {
                for(let i = 0; i < this.items.length; i++){
                  if(this.selected[this.items[i].id]){
                    this.buy_list.push(i)
                    this.send_list.push(this.items[i].id)
                  }
                }
                this.dialogVisible = true
              }
            }
          },
          async sendBuyInfo() {
            let fileList =  new FormData()
            fileList.append('itemID', this.send_list)
            fileList.append('location', this.location)
            await axios({
                url:"/root"+'/trolley/buy',
                method: 'post',
                data: fileList
            }).then(res => {
              this.answer = res.data
              console.log(this.answer)
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
            this.$store.push('/blank')
          },
          clear_list() {
            this.buy_list.length = 0
            this.send_list.length = 0
            this.dialogVisible = false
          }
        },
        async mounted() {
          this.first = this.$store.state.image_store.reload
          await this.$axios.get("/root"+"/trolley")
          .then((result) => {
            this.items = result.data
          })
          if(this.items.length > 0){
            this.isEmpty = false
          }

          for(let i=0;i<this.items.length;i++){
            this.selected[this.items[i].id] = false
          }
          this.dictionary = this.$store.state.image_store.images
          if(this.first){
            this.$router.push('/blank')
            this.$store.commit('SET_RELOAD', false)
          }
          console.log(this.items)
          console.log(this.selected)
        },
        updated() {
          this.dictionary = this.$store.state.image_store.images
          if(this.items.length == 0){
            this.isEmpty = true
          }
          else {
            this.isEmpty = false
          }
        }
    }
</script>

<style>
  #trolley-card {
    width: 75%;
    border-radius: 15px;
    margin: auto;
    margin-top: 20px;
  }
  #trolley-title {
    text-align: left;
    margin-left: 40px;
  }
  .el-card__body {
    padding-top: 10px;
    padding-bottom: 10px;
  }
  .item-card {
    background:#fbfbfb;
    width: 100%;
    margin: 0.3em;
    border-radius:20px;
    
  }
  .trolley-img {
    height: 100px;
    width: 100px;
    cursor: pointer;
  }
  .count-bar {
    background:lightgray;
    position:fixed; 
    bottom:0;
    width: 75%;
    z-index: 100;
    height: 50px;
  }
  .count-button {
    height: 50px;
    background-color: lightgray;
    border: lightgray;
    font: bold;
  }
  
</style>