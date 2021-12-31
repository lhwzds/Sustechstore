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

        <el-badge :value="readData.length" class="item">
          <el-popover
            placement="bottom-start"
            width="500"
            trigger="click"
            >
            <el-table :data="readData">
              <el-table-column width="100" property="avatar" label="头像" >
                  <template   slot-scope="scope">            
                      <img :src="scope.row.avatar"  min-width="100" height="100"/>
                  </template>         
              </el-table-column>
              <el-table-column width="100" property="name" label="昵称" >
              </el-table-column>
              <el-table-column width="200" property="content" label="最新消息" >
              </el-table-column>
              <el-table-column width="100" property="id" label="与他联系" >
                  <template   slot-scope="scope">            
                    <el-button type="text" size="small" @click="goChattingResponse(scope.row.id)">回复</el-button>
                  </template>  
              </el-table-column>
            </el-table>
              <!-- <el-button slot="reference" type="primary" class="header-button" id="header-button-3" icon="el-icon-message"></el-button>         -->
          </el-popover>
        </el-badge>

        <el-popover
          placement="bottom-start"
          width="500"
          trigger="click"
          >
          <el-table :data="readData">
            <el-table-column width="100" property="avatar" label="头像" >
                <template   slot-scope="scope">            
                    <img :src="scope.row.avatar"  min-width="100" height="100"/>
                </template>         
            </el-table-column>
            <el-table-column width="100" property="name" label="昵称" >
            </el-table-column>
            <el-table-column width="200" property="content" label="最新消息" >
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
          async goChattingResponse(id) {
            const that=this;
            let fileList = new FormData()
            fileList.append('id',id)
            console.log(id)
            await axios({
              url:"/root"+"/user/id",
              method:'post',
              data:fileList
            }).then(res => (this.chatInfo = res.data)) 
            that.$store.commit('SET_CHAT_INFO', JSON.stringify(this.chatInfo))
            this.$router.push('/chatresponse')
          },
          async getChatRead() {
            const that=this;
            this.mineToken=that.$store.state.user.token;          
            this.mineId = JSON.parse(that.$store.state.user.user).id;
            if(this.mineToken !== null){
              let fileList = new FormData()
              fileList.append('mineId', this.mineId)
              await axios({
                url:"/root"+"/chatread",
                method:'post',
                data:fileList
              }).then(res => (this.readData = res.data))
              // console.log(this.readData)
            }
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

          for (let i = 0; i < that.readData.length; i++) {
            console.log(i)
            let fileList =  new FormData()
            fileList.append('mineId', this.mineId)
            fileList.append('otherId', this.readData[i].id)
            await axios({
                url:"/root"+'/chatnew',
                method: 'post',
                data: fileList
            }).then(res => {
              this.readData[i].content = res.data.content
              console.log(this.readData[i].content)
            })

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