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
          <h3>价格</h3>
        </el-col>
        <el-col :span="4">
          <h3>卖家</h3>
        </el-col>
        <el-col :span="7">
          <h3>操作</h3>
        </el-col>
      </el-row>

      <el-divider></el-divider>
      
      <el-row type="flex" justify="center">
        <el-col>
          <el-card shadow="never" class="item-card" v-for="item in items" :key=item.id>
            <el-row type="flex" justify="center" align="middle"> 
              <el-col :span="1">
                <el-checkbox v-model="selected[item.id]" class="select_button" @change="count_sum"></el-checkbox>  
              </el-col>

              <el-col :span="4">
                <img :src="item.idImg" class="trolley-img"> 
              </el-col>

              <el-col :span="4">
                <p style="text-align:left;">{{item.name}}</p>
              </el-col>

              <el-col :span="4">
                <p>{{item.idSeller}}</p>
              </el-col>

              <el-col :span="4">
                <h3>￥{{item.price}}</h3>
              </el-col>

              <el-col :span="7">
                <el-row style="margin-bottom:10px">
                  <el-button>查看详情</el-button>  
                </el-row>
                <el-row>
                  <el-button @click="single_delete(item.id)">删除商品</el-button>
                </el-row>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>

      <el-pagination layout="prev, pager, next" :total="500"></el-pagination>
    </el-card>

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
            <h4>合计：{{sum}}元</h4>
          </el-col>
          <el-col :span="3.5">
            <el-button class="count-button" style="width:100px;background:#FF5809" type="primary">
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
                selected: [false, false, false, false, false, false],
                items:[
                  {id:0, idName:"商品1", idImg:require('../resources/image/merchandise-img/1.jpg'), idPrice: 123, idSeller:'卖家1'},
                  {id:1, idName:"商品2", idImg:require('../resources/image/merchandise-img/2.jpg'), idPrice: 123, idSeller:'卖家2'},
                  {id:2, idName:"商品3", idImg:require('../resources/image/merchandise-img/3.jpg'), idPrice: 123, idSeller:'卖家3'},
                  {id:3, idName:"商品4", idImg:require('../resources/image/merchandise-img/4.jpg'), idPrice: 123, idSeller:'卖家4'},
                  {id:4, idName:"商品5", idImg:require('../resources/image/merchandise-img/5.jpg'), idPrice: 123, idSeller:'卖家5'},
                  {id:5, idName:"商品6", idImg:require('../resources/image/merchandise-img/6.jpg'), idPrice: 123, idSeller:'卖家6'},
                ],
            };
        },
        components: {
          HeaderBar,
          FootBar
        },
        methods: {
          check_select() {
            let i = 0
            if(this.all_selected){
                for(i = 0; i < this.items.length; i++){
                    this.selected[i] = true
                } 
            }
            else{
                for(i = 0; i < this.items.length; i++){
                    this.selected[i] = false
                } 
            }
            this.count_sum()
          },
          count_sum() {
            let all = 0
            let count = 0
            let i = 0
            let items = this.items
            let selected = this.selected
            for(i = 0; i < this.items.length; i++){
                if(selected[i]){
                    count++
                    all += items[i].idPrice
                }
            }
            this.merchan_count = count
            this.sum = all
          },
          single_delete(id) {
            let i = 0
            for(i = 0; i < this.items.length; i++){
                if(this.items[i].id == id){
                  let id = this.items[i].id
                  console.log(id)
                  axios({
                    URL: "/proxy"+"trolley/remove",
                    method: 'post',
                    data: id
                  })
                  this.items.splice(i, 1)
                  this.selected.splice(i, 1)
                }
            }
          },
          multiple_delete() {
            let i = 0
            let list = []
            for(i = 0; i < this.items.length; i++){
                if(this.selected[i]==true){
                  list.push(this.items[i].id)
                  this.items.splice(i, 1)
                  this.selected.splice(i, 1)
                  i--
                }
            }
            axios({
              URL: "/proxy"+"trolley/remove",
              method: 'post',
              data: list
            })
          }
        },
        async mounted() {
          await this.$axios.get("/proxy"+"/trolley")
          .then((result) => {
            this.items = result.data
          })
          console.log(this.items)
          console.log(this.items)
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