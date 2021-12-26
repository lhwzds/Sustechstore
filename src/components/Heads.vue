<template>
  <section class="top">
    <img class="head" :src="otherImg" />
    <span class="name">{{otherName}}</span>
    <span class="voice"><i class="fa" aria-hidden="true" :class="vociceOpen?'fa-volume-up':'fa-volume-down'" v-on:click="vociceOpen=vociceOpen?false:true"></i></span>
    <!--fa fa-volume-off-->
  </section>
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
  created(){
    this.otherName = JSON.parse(this.$store.state.image_store.seller_info).publisher
    this.otherImgName = JSON.parse(this.$store.state.image_store.seller_info).avatar
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
  
  .top .head {
    width: 50px;
    height: 50px;
    float: left;
    border-radius: 100%;
  }
  
  .top .name {
    font-size: 18px;
    color: #444;
    margin-left: 10px;
  }
  
  .top .voice {
    width: 30px;
    height: 50px;
    float: right;
    text-align: center;
  }
</style>