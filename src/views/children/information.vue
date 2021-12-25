<template>
  <div class="user-detail">
    <div class="user-detail-top">基本信息</div>
    <div class="user-detail-group">
      <div class="user-item">
        <span>头像</span>
        <span>
          <a class="avatar-brand">
              <img v-if="image" width="50px" height="50px" :src="image">
              <img v-else src="../../assets/logo.png">
          </a>
        </span>
      </div>
      <div class="user-item" >
        <span>手机</span>
        <span>{{JSON.parse(this.$store.state.user.user).telephone || '暂无'}} </span>
      </div>
      <div class="user-item">
        <span>邮箱</span>
        <span>{{ JSON.parse(this.$store.state.user.user).email || '暂无'}}</span>
      </div>
      <div class="user-item">
        <span>昵称</span>
        <span>{{ JSON.parse(this.$store.state.user.user).nick_name || '暂无' }}</span>
      </div>
      <div class="user-item">
        <span>性别</span>
        <span>{{ JSON.parse(this.$store.state.user.user).gender||'暂无' }}</span>
      </div>
      <div class="user-item">
        <span>生日</span>
        <span>{{JSON.parse(this.$store.state.user.user).birthday || '暂无' }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import {mapState} from 'vuex'

export default {
  data () {
    return {
      image: ''
    }
  },
  created () {
    this.image = this.user.img || ''
  },
  computed: {
    ...mapState(['user'])
  }
}
</script>

<style scoped>
.user-detail{
  width: 70%;
  height: 100%;
  margin: 20px auto;
}
.user-detail-top{
  width: 100%;
  height: 60px;
  line-height: 60px;
  padding-left: 10px;
  font-weight: bold
}
.user-detail-group .user-item{
  height: 50px;
  padding: 0 10px;
  background-color: #fff;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center
}
.avatar-brand{
  position: relative;
  width: 100px;
  height: 100px;
}
.avatar-brand /deep/ .el-upload.el-upload--text {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  cursor: pointer;
  overflow: hidden;
}
</style>
