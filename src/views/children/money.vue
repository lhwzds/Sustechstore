<template>
    <div class="money">
        <el-row class="name">
            <span style="font-size:50px">充值</span>
        </el-row>
        <el-divider></el-divider>
        <div class="choice">
        <el-radio style="zoom: 150%" v-model="ratio" value="50" label="50">50元</el-radio>
        <el-radio style="zoom: 150%" v-model="ratio" value="100" label="100">100元</el-radio>
        <el-radio style="zoom: 150%" v-model="ratio" value="150" label="150">150元</el-radio>
        <el-radio style="zoom: 150%" v-model="ratio" value="200" label="200">200元</el-radio>
        </div>
        <el-row>
        <el-button type="primary" size = 'medium' @click="post()" style="margin-left:100px">提交</el-button>
        </el-row>
    </div>
</template>

<script>
export default {
  data()
  {
      return {
          ratio: ''
      };
  },
  methods: {
      post(){
          const that = this
          let list = new FormData()
          list.append('ratio',that.ratio)
          this.$axios.post('/root'+'/charge',list).then(function (res) {
              if (res.status === 200) {
                  that.$notify.success('充值成功')
              }
          })
      }
  }
}
</script>

<style scoped>
.money {
    width:1000px;
    height:500px;
    margin-left: 400px;
    margin-right: 200px;
    background: white;
    margin-top: 60px;
}
.choice {
    width: 80%;
    height:200px;
    font-size:25px;
    margin-left: 130px;
    margin-top: 150px
}
.name {
    margin-left: 65px;
}
</style>