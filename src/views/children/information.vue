<template>
  <div class="user-detail">
    <div class="user-detail-top" style="font-size:25px">基本信息</div>
    <div class="user-detail-group">
      <div class="user-item">
        <span style="font-size:25px">头像</span>
        <span>
          <div class="avatar-brand">
              <img v-if="image" :src="image" style="height:65px;width:65px;">
              <img v-else src="src\assets\logo.png" style="height:65px;width:65px;">
          </div>
        </span>
      </div>
      <div class="user-item" >
        <span style="font-size:25px">手机</span>
        <span style="font-size:25px">{{JSON.parse(this.$store.state.user.user).telephone || '暂无'}}</span>
      </div>
      <div class="user-item">
        <span style="font-size:25px">邮箱</span>
        <span style="font-size:25px">{{ JSON.parse(this.$store.state.user.user).email || '暂无'}}</span>
      </div>
      <div class="user-item">
        <span style="font-size:25px">昵称</span>
        <span style="font-size:25px">{{ nick_name || '暂无' }}</span>
      </div>
      <div class="user-item">
        <span style="font-size:25px">性别</span>
        <span style="font-size:25px">{{ JSON.parse(this.$store.state.user.user).gender||'暂无' }}</span>
      </div>
      <div class="user-item">
        <span style="font-size:25px">生日</span>
        <span style="font-size:25px">{{JSON.parse(this.$store.state.user.user).birthday || '暂无' }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import {mapState} from 'vuex'

export default {
  data () {
    return {
      nick_name: '',
      image: ''
    }
  },
  created () {
    this.image = this.user.img || ''
    if (JSON.parse(this.$store.state.user.user).nick_name === '用户未设昵称') {
      this.nick_name = ''
    } else {
      this.nick_name = JSON.parse(this.$store.state.user.user).nick_name
    }
    console.log(this.image)
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
  height: 75px;
  padding: 0 10px;
  background-color: #fff;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size:50px;
}
.avatar-brand{
  position: relative;
  border-radius: 50px;
}
</style>
