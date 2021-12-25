<template>
  <div class="goods_main">
    <el-backtop></el-backtop>
    <HeaderBar></HeaderBar>

    <el-card id="goods-card">
      <el-row :gutter="20" style="margin-top:20px">
        <el-col :span="9">
          <div class="block" style="margin-top:0px">
            <el-carousel indicator-position="outside" trigger="click" height="400px" style="border-radius:0px">
              <el-carousel-item v-for="image in images" :key="image.id">
                <img :src="image.idView" class="image">
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

          <el-row type="flex" style="text-align:left;">
            <el-col :span="6">
              <h3 class="good-attr">相关标签: </h3>
            </el-col>
            <el-col :span="18" style="margin-top:10px;margin-bottom:5px;">
              <el-tag size="small" class="good-tag" v-for="tag in itemInfo.tag" :key="tag">{{tag}}</el-tag>
            </el-col>
          </el-row>

          <el-row type="flex" style="height:200px">
            <el-col :span="6">
              <h3 class="good-attr">商品描述: </h3>
            </el-col>
            <el-col :span="18">
              <p class="good-des">{{itemInfo.description}}</p>
            </el-col>
          </el-row>

          <el-row type="flex" align="middle" style="text-align:left">
            <el-col :span="7">
              <el-button class="user-box-button" type="primary" icon="el-icon-s-goods" plain>购买商品</el-button>
            </el-col>
            <el-col :span="9" :offset="1">
              <el-button class="user-box-button" icon="el-icon-shopping-cart-2" type="primary" @click="addTrolley">加入购物车</el-button>
            </el-col>
          </el-row>
        </el-col>

        <el-col :span="5">
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
   
    <FootBar></FootBar>
  </div>
</template>

<script>
import HeaderBar from '../components/HeaderBar.vue'
import FootBar from '../components/FootBar.vue'
import axios from 'axios';
    export default {
        data() 
        {
            return {
                id:'',
                images: [
                  {id:0, idView:require('../resources/image/merchandise-img/1.jpg')},
                  {id:1, idView:require('../resources/image/merchandise-img/2.jpg')},
                  {id:2, idView:require('../resources/image/merchandise-img/3.jpg')}
                ],    
                itemInfo:null,
                screenWidth :0
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
            this.$router.push('/center')
          },
          goChatting() {
            this.$router.push('/chat')
          },
          addTrolley() {
            this.$confirm('加入购物车成功! 是否前往购物车查看？', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'success'
            }).then(() => {
              this.$router.push('/trolley')
            });
          }
        },
        async mounted() {
          // 首次加载时,需要调用一次
          this.screenWidth =  window.innerWidth;
          this.setSize();
          // 窗口大小发生改变时,调用一次
          window.onresize = () =>{
            this.screenWidth =  window.innerWidth;
            this.setSize();
          }
          this.id = this.$store.state.SearchInfo.item_id
          let formdata = new FormData()
          formdata.append('itemID', this.id)
          await axios({
            url:"/root"+"/items/detail",
            method:'post',
            data:formdata,
          }).then(res => (this.itemInfo = res.data))
          console.log(this.itemInfo)
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
