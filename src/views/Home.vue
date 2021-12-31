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
            <el-row><el-button class="user-box-button" type="primary" plain style="margin-top:40px" @click="goTrolley">购物车</el-button></el-row>
            <el-row><el-button class="user-box-button" type="primary" plain @click="goBought">购买记录</el-button></el-row>
            <!-- <el-row><el-button class="user-box-button" type="primary" plain>聊天工具</el-button></el-row> -->
            <el-row><el-button class="user-box-button" type="primary" plain @click="goPost">发布商品</el-button></el-row>
            <el-row><el-button class="user-box-button" type="primary" plain @click="goCenter">个人主页</el-button></el-row>
          </el-card>
        </el-col>
      </el-row>
    
      <el-row type="flex" justify="center">
        <el-radio-group v-model="radio_type">
          <el-radio-button label="商品"></el-radio-button>
          <el-radio-button label="跑腿"></el-radio-button>
          <el-radio-button label="求购"></el-radio-button>
        </el-radio-group>
      </el-row>

      <el-row type="flex" justify="center" style="margin-top:0px" align="middle" v-if="radio_type == '商品'">
        <el-col style="text-align:left">
          <el-card shadow="never" class="content-card"  v-for="item in itemInfo" :key=item.id>
            <img :src="dictionary[item.id]" @click="goGoods(item.id)" class="item-pic" alt="查看商品">
            <p class="item-name" @click="goGoods(item.id)">{{item.name}}</p>
            <p class="item-price" @click="goGoods(item.id)">￥{{item.price}}</p>
            <p class="item-seller" >发布用户: {{item.publisher}}</p>
          </el-card>
        </el-col>
      </el-row>

      <el-row type="flex" justify="center" style="margin-top:0px" align="middle" v-if="radio_type == '跑腿'">
        <el-col style="text-align:left">
          <el-card shadow="never" class="content-card"  v-for="item in errand" :key=item.id>
            <img :src="erand_img" @click="goGoods(item.id)" class="item-pic" alt="查看商品">
            <p class="item-name" @click="goGoods(item.id)">{{item.name}}</p>
            <p class="item-price" @click="goGoods(item.id)">￥{{item.price}}</p>
            <p class="item-seller" >发布用户: {{item.publisher}}</p>
          </el-card>
        </el-col>
      </el-row>

      <el-row type="flex" justify="center" style="margin-top:0px" align="middle" v-if="radio_type == '求购'">
        <el-col style="text-align:left">
          <el-card shadow="never" class="content-card"  v-for="item in need" :key=item.id>
            <img :src="need_img" @click="goGoods(item.id)" class="item-pic" alt="查看商品">
            <p class="item-name" @click="goGoods(item.id)">{{item.name}}</p>
            <p class="item-price" @click="goGoods(item.id)">￥{{item.price}}</p>
            <p class="item-seller" >发布用户: {{item.publisher}}</p>
          </el-card>
        </el-col>
      </el-row>

    </el-card>

    <div class="weather_info" v-if="hasWeather">
      <h3 class="weather_txt" style="top:100px;">深圳市天气预报</h3>
      <div v-if="weather_day == '明天'">
        <img :src="weather.showapi_res_body.f1.day_weather_pic" alt="天气预报" class="weather_img">
        <h3 class="weather_txt" style="top:250px;">天气情况: {{weather.showapi_res_body.f1.day_weather}}</h3>
        <h3 class="weather_txt" style="top:285px;">气温: {{weather.showapi_res_body.f1.night_air_temperature}} ~ {{weather.showapi_res_body.f1.day_air_temperature}} °C</h3>
      </div>
      <div v-if="weather_day == '后天'">
        <img :src="weather.showapi_res_body.f2.day_weather_pic" alt="天气预报" class="weather_img">
        <h3 class="weather_txt" style="top:250px;">天气情况: {{weather.showapi_res_body.f2.day_weather}}</h3>
        <h3 class="weather_txt" style="top:285px;">气温: {{weather.showapi_res_body.f2.night_air_temperature}} ~ {{weather.showapi_res_body.f2.day_air_temperature}} °C</h3>
      </div>
      <div v-if="weather_day == '大后天'">
        <img :src="weather.showapi_res_body.f3.day_weather_pic" alt="天气预报" class="weather_img">
        <h3 class="weather_txt" style="top:250px;">天气情况: {{weather.showapi_res_body.f3.day_weather}}</h3>
        <h3 class="weather_txt" style="top:285px;">气温: {{weather.showapi_res_body.f3.night_air_temperature}} ~ {{weather.showapi_res_body.f3.day_air_temperature}} °C</h3>
      </div>
      <el-radio-group class="weather_button" v-model="weather_day">
          <el-radio-button label="明天"></el-radio-button>
          <el-radio-button label="后天"></el-radio-button>
          <el-radio-button label="大后天"></el-radio-button>
      </el-radio-group>
    </div>
    

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
                itemInfo: [],
                errand: [],
                need: [],
                screenWidth: 0,
                dictionary: {},
                first:false,
                radio_type:'商品',
                erand_img:require('../assets/run.jpg'),
                need_img:require('../assets/need.jpg'),
                weather:{},
                hasWeather:false,
                weather_day:"明天"
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
          async goPost() {
            if(this.$store.state.user.user == null) {
              this.$confirm("您还未登录，不能发布商品!\n请登录之后再进行操作!", '警告', {
              confirmButtonText: '确定',
              type: 'warning'})
            }
            else {
              await axios({
                url:"/root"+'/getcredit',
                method: 'get'}).then(res => {
                  console.log(res.data)
                  if(parseInt(res.data)<50){
                    alert('您的信用积分过低，不能发布商品!')
                  }
                  else{
                    this.$store.commit('SET_SLO_IND', 2)
                    this.$router.push('/postInfo')
                  }
                })
            }
          },
          goTrolley() {
            if(this.$store.state.user.user == null) {
              this.$confirm("您还未登录，请先登录再查看购物车!", '警告', {
              confirmButtonText: '确定',
              type: 'warning'})
            }
            else {
              this.$store.commit('SET_RELOAD', true)
              this.$store.commit('SET_SLO_IND', 3)
              this.$router.push('/trolley')
            }
          },
          goBought() {
            if(this.$store.state.user.user == null) {
              this.$confirm("您还未登录，请先登录再查看购买记录!", '警告', {
              confirmButtonText: '确定',
              type: 'warning'})
            }
            else {
              this.$store.commit('SET_RELOAD', true)
              this.$store.commit('SET_SLO_IND', 5)
              this.$router.push('/bought')
            }
          },
          goCenter(id) {
            if(this.$store.state.user.user == null) {
              this.$confirm("您还未登录，请先登录再前往个人中心!", '警告', {
              confirmButtonText: '确定',
              type: 'warning'})
            }
            else {
              console.log(id)
              this.$router.push('/center')
            }
          },
          goGoods(id) {
            this.$store.commit('SET_RELOAD', true)
            this.$store.commit('SET_ITEM_ID', id)
            this.$store.commit('SET_SLO_IND', 4)
            this.$router.push('/goods')
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
          this.first = this.$store.state.image_store.reload
          await this.$axios.get("/root"+"/items/all")
          .then(response => (this.itemInfo = response.data))
          .catch(function (error) { // 请求失败处理
            console.log(error);
          });
          await this.$axios.get("/root"+"/items/allerrand")
          .then(response => (this.errand = response.data))
          .catch(function (error) { // 请求失败处理
            console.log(error);
          });
          await this.$axios.get("/root"+"/items/allneed")
          .then(response => (this.need = response.data))
          .catch(function (error) { // 请求失败处理
            console.log(error);
          });
          for(let i=0;i<this.itemInfo.length;i++){
            if(this.itemInfo[i].fileList.length > 0){
              let pic_name = this.itemInfo[i].fileList[0]
              let item_id = this.itemInfo[i].id
              await this.$axios.get("/root"+"/items/picture/"+pic_name)
              .then((result) => {
                this.dictionary[item_id] = result.data
              })
            }
          }
          for(let i=0;i<this.errand.length;i++){
            if(this.errand[i].fileList.length > 0){
              let pic_name = this.errand[i].fileList[0]
              let item_id = this.errand[i].id
              await this.$axios.get("/root"+"/items/picture/"+pic_name)
              .then((result) => {
                this.dictionary[item_id] = result.data
              })
            }
          }
          for(let i=0;i<this.need.length;i++){
            if(this.need[i].fileList.length > 0){
              let pic_name = this.need[i].fileList[0]
              let item_id = this.need[i].id
              await this.$axios.get("/root"+"/items/picture/"+pic_name)
              .then((result) => {
                this.dictionary[item_id] = result.data
              })
            }
          }
          this.$store.commit('SET_IMG', this.dictionary)

          await this.$axios.get("/root"+"/weather")
          .then(response => {
            this.weather = response.data
            this.hasWeather = true
            console.log(this.weather)
          })
          .catch(function (error) { // 请求失败处理
            console.log(error);
          });
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
  .content-card{
    background:#fbfbfb;
    width: 300px;
    display: inline-block;
    margin: 10px;
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
    height: 195px;
  }
  .weather_img {
    position:fixed;
    z-index:50;
    width:100px;
    left:75px;
    top:150px;
  }
  .weather_txt {
    position:fixed;
    z-index:20;
    left:70px;
  }
  .weather_button {
    position:fixed;
    z-index:20;
    left:25px;
    top: 340px;
  }
</style>
