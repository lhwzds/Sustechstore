<template>
  <!-- <section class="top">
    <img class="head" :src="otherImg" />
    <span class="name">{{otherName}}</span>
    <span class="voice"><i class="fa" aria-hidden="true" :class="vociceOpen?'fa-volume-up':'fa-volume-down'" v-on:click="vociceOpen=vociceOpen?false:true"></i></span>
  </section> -->
  <div>
    <el-row  class="row-header" type="flex" justify="center" >
     <el-col :span="1" :offset="0">
        <img class="head" :src="otherImg" />
      </el-col>
      <el-col :span="20" :offset="0">
        <h2 class='header-h2'>{{ otherName }}</h2>
      </el-col>
       <el-col :span="1" :offset="0">
        <el-button type="primary" icon="el-icon-s-home" class="header-button" id="header-button-1" @click="backHome"></el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'headsResponse',
  data () {
    return {
      otherName:'',
      otherImgName:'',
      otherImg: null,
      vociceOpen:true
    }
  },
  methods: {
    backHome(){
      this.$router.push('/')
    }
  },
  created(){
    // this.otherName='zrdyyds'
    // this.otherImgName='O1CN01l4mpgR2NXE951sUmU_!!112149972.jpg'
    console.log('chatinfo'+this.$store.state.chat.chatter_info)
    this.otherName = JSON.parse(this.$store.state.chat.chatter_info).nick_name
    this.otherImgName = JSON.parse(this.$store.state.chat.chatter_info).avatar
  },
  async mounted() {
    const that=this;
    await this.$axios.get('/root' + '/items/picture/' + that.otherImgName).then(function (res) {
    if (res.status === 200) {
        that.otherImg = res.data
        }
    }).catch(function (error) {
    console.log(error)
    })

    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .top {
    height: 50px;
    line-height: 50px;
    position: absolute;
    top: 0px;
    left: 0px;
    right: 0px;
    background-color: #F8F8F8;
    padding: 5px 10px;
  }
  
   .head {
    width: 60px;
    height: 60px;
    float: left;
    border-radius: 100%;
    margin-top: 5px;
  }
  
  .top .name {
    font-size: 18px;
    color: #444;
    margin-left: 10px;
  }
  

</style>