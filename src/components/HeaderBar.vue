<template>
  <div>
    <el-row  class="row-header" type="flex" justify="center" >
      <el-col :span="3" :offset="2">
        <el-button type="primary" icon="el-icon-s-home" class="header-button" id="header-button-1" @click="backHome"></el-button>
      </el-col>
      <el-col :span="10" :offset="2">
        <h2 class='header-h2'>{{ slogan }}</h2>
      </el-col>
      <el-col :span="6" :offset="1">
        <el-button type="primary" class="header-button" id="header-button-2" @click="goLog">{{btn_text}}</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapState } from 'vuex';

    export default {
        data() 
        {
            return {
              sloganList: ['Welcome to Sustech Store!',
                        'Did You Find What You Want?',
                        'Sell Your Own Merchandise!',
                        'Want to Buy Them? Do It Now!',
                        'That Looks Good, Right?'],
              slogan_index: 0,
              slogan: 'Welcome to Sustech Store!',
              btn_text: '请登录',
              loggin: false
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
          }
        },
        computed: {
          ...mapState(['user'])
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
    

</style>