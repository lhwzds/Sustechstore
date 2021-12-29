<template>
  <div>
    <el-row  class="row-header" type="flex" justify="center" >
      <el-col :span="3" :offset="1">
        <el-button type="primary" icon="el-icon-s-home" class="header-button" id="header-button-1" @click="backHome"></el-button>
      </el-col>
      <el-col :span="10" :offset="2">
        <h2 class='header-h2'>{{ slogan }}</h2>
      </el-col>
      <el-col :span="3">
        <el-popover
          placement="bottom"
          width="250"
          trigger="click"
          >
          <el-table :data="readData">
            <el-table-column width="100" property="avatar" label="头像" >
                <template   slot-scope="scope">            
                    <img :src="scope.row.avatar"  min-width="100" height="100"/>
                </template>         
            </el-table-column>
            <el-table-column width="50" property="id" label="id" >
            </el-table-column>
            <el-table-column width="100" property="id" label="与他联系" >
                <template   slot-scope="scope">            
                  <el-button type="text" size="small" @click="goChattingResponse(scope.row.id)">回复</el-button>
                </template>  
            </el-table-column>
          </el-table>
          <el-button slot="reference" type="primary" class="header-button" id="header-button-3" icon="el-icon-message"></el-button>
        </el-popover>
      </el-col>
      <el-col :span="5">
        <el-button type="primary" class="header-button" id="header-button-2" @click="goLog">{{btn_text}}</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import axios from 'axios';
    export default {
        data() 
        {
            return {
              sloganList: ['Welcome to Sustech Store!',
                        'Did You Find What You Want?',
                        'Sell Your Own Merchandise!',
                        'Want to Buy Them? Do It Now!',
                        'That Looks Good, Right?',
                        'Are you satisfied with them?'],
              slogan_index: 0,
              slogan: 'Welcome to Sustech Store!',
              btn_text: '请登录',
              loggin: false,
              pop_text:'消息',
              mineId:'',
              readData: [],
              inf: [],
              chatInfo:[],
              id:'',
              mineToken:null
            };
        },
        methods: {
          backHome(){
            this.$router.push('/')
            this.$store.commit('SET_SLO_IND', 0)
          },
          goLog(){
            if(!this.loggin){
              this.$router.push('/login')
            }
            else{
              this.$router.push('/center')
            }
          },
          goChattingResponse(id) {
            // console.log('id'+id)
            const that=this;
            this.getChatInfo(id)
            that.$store.commit('SET_CHAT_INFO', JSON.stringify(this.chatinfo))
            // console.log(this.chatInfo)
            // console.log(that.$store.state.chat.chatter_info)
            this.$router.push('/chatresponse')
          },
          async getChatRead() {
            const that=this;
            this.mineToken=that.$store.state.user.token;          
            this.id = JSON.parse(that.$store.state.user.user).id;
            if(this.mineToken !== null){
              // console.log(this.mineToken)
              // console.log(this.id)
              let fileList = new FormData()
              fileList.append('mineId', this.id)
              await axios({
                url:"/root"+"/chatread",
                method:'post',
                data:fileList
              }).then(res => (this.readData = res.data))
              // console.log(this.readData)
            }
          }, 
          async getChatInfo(id) {
            let fileList = new FormData()
            fileList.append('id',id.toString())
            await axios({
              url:"/root"+"/user/id",
              method:'post',
              data:fileList
            }).then(res => (this.chatInfo = res.data,
            console.log('res'+this.chatInfo))
            )   
            console.log('getChatInfoqq'+this.chatInfo.qq)
          }, 
          async getChatNew(id) {
            let fileList = new FormData()
            fileList.append('id',id)
            await axios({
              url:"/root"+"/user/id",
              method:'post',
              data:fileList
            }).then(res => (this.chatInfo = res.data))
          },
        },
        computed: {
          ...mapState(['user'])
        },
        async created() {
          await this.getChatRead()
          const that = this
          for (let i = 0; i < that.readData.length; i++) {
            console.log(i)
            let imgName = that.readData[i].avatar
            if (imgName.length > 0) {
              // console.log(imgName)
              await that.$axios.get('/root' + '/items/picture/' + imgName).then(function (res) {
                if (res.status === 200) {
                  that.readData[i].avatar = res.data
                }
              })
            } else {
                  that.readData[i].avatar = '../assets/logo.png'
            }
          }
        },
        mounted() {
          this.slogan_index = this.$store.state.SearchInfo.slogan_index
          this.slogan = this.sloganList[this.slogan_index]
          if(this.user.user == null){
            this.btn_text = '请登录'
            this.loggin = false
          }
          else{
            this.btn_text = JSON.parse(this.user.user).nick_name
            this.loggin = true
          }
        }
    }
</script>

<style>
  .row-header {
    background:#FF5809;
    height: 65px;
  }
  .header-h2 {
    color:white ;
  }
  .header-h2:hover {
    color:#fff ;
  }
  .header-button {
    font-weight: bold;
    height: 65px;
  }
  #header-button-1 {
    font-size: 30px;
  }
  #header-button-2 {
    font-size: 20px;
  }
  #header-button-3 {
    font-size: 30px;
    background:#FF5809;
    border:#FF5809;
  }

</style>