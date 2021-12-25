<template>
  <div class="home-main">

    <el-backtop></el-backtop>

    <HeaderBar></HeaderBar>

    <SearchBox style="z-index:20;position:absolute;width:100%"></SearchBox> 

    <el-card id="main-card">
      <el-row style="margin-top:20px">
        <el-col :span="4" :offset="6">
          <h3 class="first-info">热销商品</h3>
        </el-col>
        <el-col :span="4" :offset="8">
          <h3 class="first-info">相关工具</h3>
        </el-col>
      </el-row>

      <el-row type="flex" justify="center" style="margin-bottom:20px">
        <el-col :span="16">
          <div class="block">
            <el-carousel trigger="click" height="300px" style="border-radius:10px">
              <el-carousel-item v-for="image in images" :key="image.id">
                <img :src="image.idView" alt="查看商品" class="image" style="cursor:pointer;" @click="goGoods">
              </el-carousel-item>
            </el-carousel>
          </div>
        </el-col>
        <el-col :span="8">
          <el-card shadow="never" id="user-box">
            <el-row><el-button class="user-box-button" type="primary" plain style="margin-top:20px" @click="goTrolley">购物车</el-button></el-row>
            <el-row><el-button class="user-box-button" type="primary" plain>近期浏览</el-button></el-row>
            <el-row><el-button class="user-box-button" type="primary" plain>聊天记录</el-button></el-row>
            <el-row><el-button class="user-box-button" type="primary" plain @click="goPost">发布商品</el-button></el-row>
            <el-row><el-button class="user-box-button" type="primary" plain @click="goCenter">个人主页</el-button></el-row>
          </el-card>
        </el-col>
      </el-row>
    
      <el-row type="flex" justify="center">
        <el-col>
          <h3 class="first-info">猜你喜欢</h3>
        </el-col>
      </el-row>
      <el-row type="flex" justify="center" style="margin-top:0px">
        <el-col style="text-align:left">
          <el-card shadow="never" class="content-card"  v-for="item in itemInfo" :key=item.id>
            <img :src="dictionary[item.id]" @click="goGoods(item.id)" class="item-pic" alt="查看商品">
            <p class="item-name" @click="goGoods">{{item.name}}</p>
            <p class="item-price" @click="goGoods">￥{{item.price}}</p>
            <p class="item-seller" @click="goCenter">发布用户: yjn</p>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <FootBar></FootBar>
  </div>
</template>

<script>
import SearchBox  from '../components/SearchBox.vue'
import HeaderBar from '../components/HeaderBar.vue'
import FootBar from '../components/FootBar.vue'


    export default {
        data() 
        {
            return {
                search_input:'',
                images: [
                  {id:0, idView:require('../resources/image/like-img/1.jpg')},
                  {id:1, idView:require('../resources/image/like-img/2.jpg')},
                  {id:2, idView:require('../resources/image/like-img/3.jpg')},
                  {id:3, idView:require('../resources/image/like-img/4.jpg')},
                  {id:4, idView:require('../resources/image/like-img/5.jpg')},
                  {id:5, idView:require('../resources/image/like-img/6.jpg')},
                  {id:6, idView:require('../resources/image/like-img/7.jpg')},
                ],
                itemInfo: null,
                screenWidth: 0,
                dictionary: {},
                first:false
            };
        },
        components: {
          SearchBox,
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
            this.$store.commit('SET_SLO_IND', 2)
          },
          goTrolley() {
            this.$router.push('/trolley')
            this.$store.commit('SET_SLO_IND', 3)
          },
          goCenter() {
            this.$router.push('/center')
          },
          goGoods(id) {
            this.$store.commit('SET_ITEM_ID', id)
            this.$store.commit('SET_SLO_IND', 4)
            this.$router.push('/goods')
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
          this.first = this.$store.state.image_store.reload
          await this.$axios.get("/root"+"/items/all")
          .then(response => (this.itemInfo = response.data))
          .catch(function (error) { // 请求失败处理
            console.log(error);
          });
          let i = 0
          for(i;i<this.itemInfo.length;i++){
            if(this.itemInfo[i].fileList.length > 0){
              let pic_name = this.itemInfo[i].fileList[0]
              let item_id = this.itemInfo[i].id
              await this.$axios.get("/root"+"/items/picture/"+pic_name)
              .then((result) => {
                this.dictionary[item_id] = result.data
              })
            }
          }
          await this.$store.commit('SET_IMG', this.dictionary)
          if(this.first){
            this.$router.push('/blank')
            this.$store.commit('SET_RELOAD')
          }
          console.log(this.first)
        },
        updated() {
          this.dictionary = this.$store.state.image_store.images
          console.log(this.dictionary)
        }
    }
</script>

<style>
  .content-card{
    background:#fbfbfb;
    width: 300px;
    display: inline-block;
    margin: 0.3em;
  }

  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
  }
  .el-carousel__item:nth-child(2n) {
     background-color: #99a9bf;
  }
  .el-carousel__item:nth-child(2n+1) {
     background-color: #d3dce6;
  }

  #user-box {
    border-radius: 10px;
    height: 300px;
    margin-top: 0;
    margin-left: 0.3em;
  }
  .user-box-button {
    width: 100%;
    margin-top: 10px;
    margin-left:0px;
  }
  img {
     /*设置图片宽度和浏览器宽度一致*/
      width:100%;
      height:inherit;
    }
  
  #main-card {
    width: 66.7%;
    border-radius: 15px;
    margin: auto;
    position: absolute;
    left: 16.7%;
    margin-top: 80px;
    z-index: 5;
  }
  .first-info {
    margin-top: 0px;
    margin-bottom: 10px;
  }
  .merchandise-img {
    width: 200px;
    height: 200px;
  }
  .item-name {
    font-size:20px;
    text-align:left;
    font-weight:bold;
    margin-top:10px;
    margin-bottom: 0px;
    cursor: pointer;
  }
  .item-price {
    text-align:left;
    color:#FF5809;
    margin-top:10px;
    margin-bottom:10px;
    font-weight: bold;
    font-size: 24px;
    cursor: pointer;
  }
  .item-seller {
    text-align: left;
    margin-top:0px;
    cursor: pointer;
  }
  .item-pic {
    margin-top:10px;
    cursor:pointer;
    height: 300px;
  }
</style>
