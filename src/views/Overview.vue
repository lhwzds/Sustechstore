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
                <img :src="item.idImg" class="merchandise-img" @click="goGoods"> 
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
                    <el-button style="height:100%" plain>加入购物车</el-button>
                  </el-col>
                  <el-col :span="4">
                    <el-button style="height:100%">立即购买</el-button>
                  </el-col>
                </el-row>
                 
              </el-col>

            </el-row>
          </el-card>
        </el-col>
      </el-row>

      <el-pagination layout="prev, pager, next" :total="500"></el-pagination>
    </el-card>

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
                items:[
                  {id:0, idName:"商品1", idImg:require('../resources/image/merchandise-img/1.jpg'), idPrice: 123, idSeller:'卖家1', idText:'lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'},
                  {id:1, idName:"商品2", idImg:require('../resources/image/merchandise-img/2.jpg'), idPrice: 123, idSeller:'卖家2', idText:'lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'},
                  {id:2, idName:"商品3", idImg:require('../resources/image/merchandise-img/3.jpg'), idPrice: 123, idSeller:'卖家3', idText:'lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'},
                  {id:3, idName:"商品4", idImg:require('../resources/image/merchandise-img/4.jpg'), idPrice: 123, idSeller:'卖家4', idText:'lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'},
                  {id:4, idName:"商品5", idImg:require('../resources/image/merchandise-img/5.jpg'), idPrice: 123, idSeller:'卖家5', idText:'lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'},
                  {id:5, idName:"商品6", idImg:require('../resources/image/merchandise-img/6.jpg'), idPrice: 123, idSeller:'卖家6', idText:'lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'},
                ],
            };
        },
        methods: {
          goGoods(id) {
            this.$store.commit('SET_SLO_IND', 4)
            this.$store.commit('SET_ITEM_ID', id)
            this.$router.push('/goods')
          }
        },
        components: {
          SearchBox,
          HeaderBar,
          FootBar
        },
        async mounted() {
          this.search_name = this.$store.state.SearchInfo.Keyword
          let formdata = new FormData()
          formdata.append("searchText", this.search_name)
          await axios({
            url:"/root"+"/search",
            method:'post',
            data:formdata,
          }).then(res => (this.items = res.data))
          console.log(this.items)
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