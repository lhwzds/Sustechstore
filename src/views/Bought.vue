<template>
  <div class="bought-main"  style="position:absolute;z-index:1;width:100%">

    <el-backtop></el-backtop>

    <HeaderBar></HeaderBar>

    <el-card id="bought-card">
      <el-row type="flex" justify="center">
        <h2 id="bought-title">购买记录</h2>
      </el-row>

      <el-row type="flex" align="middle">
        <el-col :span="5" :offset="1">
          <h3>商品信息</h3>
        </el-col>
        <el-col :span="4">
          <h3>购买时间</h3>
        </el-col>
        <el-col :span="4">
          <h3>卖家</h3>
        </el-col>
        <el-col :span="4">
          <h3>价格</h3>
        </el-col>
        <el-col :span="5">
          <h3>操作</h3>
        </el-col>
      </el-row>

      <el-divider></el-divider>

      <el-row type="flex" justify="center" v-if="isEmpty" style="height:600px">
        <h1 style="line-height:600px;color:darkgrey;">暂无购买记录</h1>
      </el-row>
      
      <el-row type="flex" justify="center" v-if="!isEmpty">
        <el-col>
          <el-card shadow="never" class="item-card" v-for="item in items" :key=item.id>
            <el-row type="flex" justify="center" align="middle"> 
              <el-col :span="3">
                <img :src="dictionary[item.itemid]" class="bought-img"> 
              </el-col>
            
              <el-col :span="3">
                <p style="text-align:left;">{{item.itemname}}</p>
              </el-col>

              <el-col :span="4">
                <p>{{item.createtime}}</p>
              </el-col>

              <el-col :span="4">
                <p>{{item.publisher}}</p>
              </el-col>

              <el-col :span="4" style="color:#FF5809;">
                <h3>￥{{item.itemprice}}</h3>
              </el-col>

              <el-col :span="6">
                <el-row type="flex" justify="center" v-if="item.consumerfirm == 1">已收货</el-row>
                <el-row v-else>
                    <el-button @click="haveReceived(item)">确认收货</el-button>
                </el-row>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <el-dialog title="购买商品" :visible.sync="dialogVisible" width="50%" :modal-append-to-body = "false" @close="closeDialog" v-if="received" center>
      <el-row type="flex">
        您确认已经收到商品: {{received_item.name}}
      </el-row>
      <el-row type="flex" style="margin-top:20px;">
        该商品来自卖家: {{received_item.publisher}}
      </el-row>
      <el-row type="flex" style="margin-top:20px;">
        请为本次购买体验打分: 
      </el-row>
      <el-rate v-model="rate_value" :colors="rate_colors"></el-rate>
      <el-row style="margin-top:20px;">
        <p>请您慎重进行打分，您的打分会对卖家的信用值造成影响，以下为信用规则:</p>
        <p>一星: 信用值 -5</p>
        <p>二星: 信用值 -3</p>
        <p>三星: 信用值 +1</p>
        <p>四星: 信用值 +3</p>
        <p>五星: 信用值 +5</p>
        <p>初始信誉积分为100分，低于50分将不能进行商品交易! </p>
      </el-row>
      

      <el-dialog width="30%" title="提示" :visible.sync="innerVisible" :modal-append-to-body = "false" @close="closeDialog" center>
        <el-row type="flex">{{result_txt}}</el-row>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="innerVisible = false">{{button_text}}</el-button>
        </span>
      </el-dialog>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible=false">取消评价</el-button>
        <el-button type="primary" @click="sendReceivedInfo">完成评价</el-button>
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
                items:[{'name':''}],
                isEmpty: true,
                dictionary: {},
                dialogVisible: false,
                innerVisible: false,
                result_txt: '',
                button_text: '',
                first: false,
                received_item: {},
                rate_value: 5,
                rate_colors: ['#99A9BF', '#F7BA2A', '#FF9900'],  // 等同于 { 2: '#99A9BF', 4: { value: '#F7BA2A', excluded: true }, 5: '#FF9900' }
                received: false,
                answer: null,
            };
        },
        components: {
          HeaderBar,
          FootBar
        },
        methods: {
            haveReceived(item) {
                console.log(item)
                this.received_item['name'] = item.itemname
                this.received_item['publisher'] = item.publisher
                this.received_item['transaction'] = item.id
                this.dialogVisible = true
                this.received = true
            },
            closeDialog() {
                this.received_item = {}
                this.received = false
            },
            async sendReceivedInfo() {
                let fileList = new FormData()
                fileList.append('rateValue', this.rate_value)
                fileList.append('id', this.received_item.transaction)
                await axios({
                    url:"/root"+'/items/confirm',
                    method: 'post',
                    data: fileList
                }).then(res => {
                    this.answer = res.data
                    console.log(this.answer)
                    this.innerVisible = true
                    this.dialogVisible = false
                })
                if (this.answer == "1"){
                    this.result_txt = "已确认收货，感谢您的评价! "
                    this.button_text = "好的"
                }
                else {
                    this.result_txt = "确认收货失败，好像出了点小问题..."
                    this.button_text = "啊这..."
                }
                this.$router.push('/blank')
              }
        },
        async mounted() {
          this.first = this.$store.state.image_store.reload
          await axios.get("/root"+"/buyhistory")
          .then((result) => {
            this.items = result.data
          })
          if(this.items.length > 0){
            this.isEmpty = false
          }
          this.dictionary = this.$store.state.image_store.images
          if(this.first){
            this.$router.push('/blank')
            this.$store.commit('SET_RELOAD', false)
          }
          console.log(this.items)
          for(let i=0; i<this.items.length; i++) {
            let timestamp = this.items[i].createtime
            let date = new Date(timestamp)
            let year = date.getFullYear()+'年'
            let month = (date.getMonth()+1)+'月'
            let day = date.getDate()+'日'
            this.items[i].createtime = [year, month, day].join('')
          }
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
  #bought-card {
    width: 75%;
    border-radius: 15px;
    margin: auto;
    margin-top: 20px;
  }
  #bought-title {
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
  .bought-img {
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