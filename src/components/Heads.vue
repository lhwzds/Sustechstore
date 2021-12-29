<template>
  <section class="top">
    <img class="head" :src="otherImg" />
    <span class="name">{{otherName}}</span>
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