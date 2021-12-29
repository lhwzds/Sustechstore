<template>
  <div class="goods_main">
    <el-backtop></el-backtop>
    <HeaderBar></HeaderBar>

    <el-card id="goods-card">
      <el-row :gutter="20" style="margin-top:30px">
        <el-col :span="9" style="margin-top:53px">
          <div class="block" style="margin-top:0px">
            <el-carousel indicator-position="outside" trigger="click" height="400px" style="border-radius:0px">
              <el-carousel-item v-for="index in this.itemInfo.file.length" :key="index">
                <img :src="dictionary[id+'_'+(index-1)]" alt="image" >
              </el-carousel-item>
            </el-carousel>
          </div>
        </el-col>

        <el-col :span="9">
          <h3 class="good-title">{{itemInfo.name}}</h3>

          <el-row type="flex" align="middle">
            <el-col :span="4">
              <h3 class="good-attr">价格: </h3>
            </el-col>
            <el-col :span="20">
              <h1 style="text-align:left;color:#FF5809;margin-top:10px;margin-bottom:10px;">￥{{itemInfo.price}}</h1>
            </el-col>
          </el-row>

          <el-row type="flex" style="text-align:left;" v-if="itemInfo.type == '1'">
            <el-col :span="6">
              <h3 class="good-attr">相关标签: </h3>
            </el-col>
            <el-col :span="18" style="margin-top:10px;margin-bottom:5px;">
              <el-tag size="small" class="good-tag" v-for="tag in itemInfo.category" :key="tag">{{tag}}</el-tag>
            </el-col>
          </el-row>

          <el-row type="flex">
            <h3 class="good-attr">商品描述: </h3> 
          </el-row>
          <el-row type="flex" style="height:150px">
            <p class="good-des">{{itemInfo.description}}</p>
          </el-row>

          <el-row type="flex" align="middle" justify="center">
            <el-col :span="7">
              <el-button class="user-box-button" type="primary" icon="el-icon-s-goods" @click="buyItem" plain>购买商品</el-button>
            </el-col>
            <el-col :span="9" :offset="1">
              <el-button class="user-box-button" icon="el-icon-shopping-cart-2" type="primary" @click="addTrolley(itemInfo.id)">加入购物车</el-button>
            </el-col>
          </el-row>
        </el-col>

        <el-col :span="5" style="margin-top:53px">
          <el-descriptions direction="vertical" :column="1" border >
            <el-descriptions-item label="卖家名称">{{itemInfo.publisher}}</el-descriptions-item>
            <el-descriptions-item label="QQ">{{itemInfo.qq}}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{itemInfo.telephone}}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{itemInfo.email}}</el-descriptions-item>
          </el-descriptions>

          <el-row type="flex"><el-button class="user-box-button" icon="el-icon-chat-dot-round" type="primary" @click="goChatting">与我联系</el-button></el-row>
          <el-row type="flex"><el-button class="user-box-button" icon="el-icon-s-custom" type="primary" @click="goCenter" plain>个人主页</el-button></el-row>
        </el-col>
      </el-row>
    </el-card>

    <el-dialog title="购买商品" :visible.sync="dialogVisible" width="50%" center>
      <el-row type="flex">您想要购买的商品为: {{itemInfo.name}}</el-row>
      <el-row type="flex" style="margin-top:20px;">价格: ￥{{itemInfo.price}}</el-row>
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
import HeaderBar from '../components/HeaderBar.vue'
import FootBar from '../components/FootBar.vue'
import axios from 'axios'

    export default {
        data() 
        {
            return {
                id:'',  
                itemInfo:{'file':['']},
                screenWidth :0,
                dictionary: {},
                first: false,
                money:null,
                dialogVisible: false,
                innerVisible: false,
                location:'',
                answer: null,
                result_txt: '',
                button_text: '',
            };
        },
        
        components: {
          HeaderBar,
          FootBar
        },
        methods:{
          setSize() {
            // 通过浏览器宽度(图片宽度)计算高度
            this.bannerHeight = 400 / 1920 * this.screenWidth;
          },
          goPost() {
            this.$router.push('/postInfo')
          },
          goCenter() {
            this.$store.commit('SET_SELLER_INFO', this.itemInfo)
            this.$router.push('/saler')
            // console.log(this.$store.state.image_store.seller_info)
          },
          goChatting() {
            // console.log('iteminfo is'+this.itemInfo)

            this.$store.commit('SET_SELLER_INFO', this.itemInfo)
            this.$router.push('/chat')
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
          async getItemInfo() {
            this.id = this.$store.state.SearchInfo.item_id
            this.first = this.$store.state.image_store.reload
            let fileList = new FormData()
            fileList.append('itemID', this.id)
            await axios({
              url:"/root"+"/items/detail",
              method:'post',
              data:fileList
            }).then(res => (this.itemInfo = res.data))
          },
          async getImages() {
            for(var i = 0;i<this.itemInfo.file.length;i++){
              let pic_name = this.itemInfo.file[i]
              await axios({
                url:"/root"+"/items/picture/"+pic_name,
                method:"get"
              }).then(res => (
                this.dictionary[this.id+'_'+i] = res.data
              ))
            }
          },
          async buyItem() {
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
              if(this.money < parseFloat(this.itemInfo.price)) {
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
          }
        },
        async created() {
          // 首次加载时,需要调用一次
          this.screenWidth =  window.innerWidth;
          this.setSize();
          // 窗口大小发生改变时,调用一次
          window.onresize = () =>{
            this.screenWidth =  window.innerWidth;
            this.setSize();
          }
          await this.getItemInfo()
          console.log(this.itemInfo)
          await this.getImages()
          this.$store.commit('SET_ADD_ITEM', this.dictionary)
          
          if(this.first){
            this.$router.push('/blank')
            this.$store.commit('SET_RELOAD', false)
          }  
        },
        updated() {
          this.dictionary = this.$store.state.image_store.item_image
        }
    }
</script>

<style>
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
  }
  .user-box-button {
    width: 100%;
    margin-top: 10px;
  }
  img {
      width:100%;
      height:inherit;
    }
  #goods-card {
    width: 75%;
    border-radius: 15px;
    margin: auto;
    margin-top:20px;
    height: 600px;
  }
  .good-title {
    margin-top:0px;
    margin-bottom: 5px;
    font-size:20px;
    text-align:left;
  }
  .good-tag {
    margin-right:5px;
    text-align:center;
    margin-bottom:5px;
  }
  .good-attr {
    text-align:left;
    margin-top:10px;
    margin-bottom:10px;
  }
  .good-des {
    text-align:left;
    margin-top:10px;
    font-size: 16px;
    color: darkgray;
    margin-bottom: 10px;
  }
  
</style>
