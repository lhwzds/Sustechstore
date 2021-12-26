<template>
  <div>
    <el-row class="row-header" type="flex" justify="center">
      <el-col :span="3">
        <el-button icon="el-icon-s-home" @click="go_home()" class="header-button" id="header-button-1">首页</el-button>
      </el-col>
      <el-col :span="3" :offset="11">
        <el-button @click="logout()" class="header-button" id="header-button-2">退出登录</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'InfHead',
  methods: {
    go_home () {
      this.$router.push('/')
    },
    logout () {
      this.$confirm('您确定退出登录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '退出成功!'
        })
        this.$router.replace('/')
        this.$store.commit('LOGOUT', this.$store.state.token)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出'
        })
      })
    }
  }
}
</script>

<style scoped>
.text {
  top: 15px
}
.row-header {
  background:#FF5809;
  height: 65px;
}
.header-button {
  color:white;
  background:#FF5809;
  border: #FF5809;
  font-weight: bold;
  height: 65px;
}
#header-button-1 {
  font-size: 30px;
}
#header-button-2 {
  font-size: 20px;
}
.el-dropdown-link {
  height: 65px;
  text-align: center;
}
.drop {
  top:14px
}
</style>
