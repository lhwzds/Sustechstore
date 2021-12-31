<template>
  <!-- <section class="top">
    <img class="head" :src="otherImg" />
    <span class="name">{{otherName}}</span>
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
  name: 'heads',
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
    const that=this
    this.otherName = that.$store.state.image_store.seller_info.publisher
    this.otherImgName = that.$store.state.image_store.seller_info.avatar
  },
  async mounted() {
    const that=this;
    await this.$axios.get('/root' + '/items/picture/' + that.otherImgName).then(function (res) {
    if (res.status === 200) {
        that.otherImg = res.data
        console.log(this.otherImg)
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
  
  .top .head {
    width: 50px;
    height: 50px;
    float: left;
    border-radius: 100%;
  }
   .head {
    width: 60px;
    height: 60px;
    float: left;
    border-radius: 100%;
    margin-top: 5px;
  }
  .top .name {
    width: 500px;
    font-size: 18px;
    color: #444;
    margin-left: 10px;
  }

  .top .home {
    font-size: 18px;
    color: #444;
    margin-right: 100px;
  }
</style>