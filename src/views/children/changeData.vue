<template>
  <div class="user-detail">
    <div class="user-detail-top">基本信息</div>
    <div class="user-detail-group">
      <div class="user-icon">
        <span>头像</span>
        <el-upload
          class="avatar-uploader"
          action=""
          :auto-upload="false"
          :show-file-list="false"
          accept="image/jpg,image/png"
          :on-change="handleAvatarChange"
        >
          <img v-if="user_avatar_imgUrl" width="100px" height="100px" :src="user_avatar_imgUrl">
          <img v-else-if="image" width="100px" height="100px" :src="image" >
          <img v-else src="../../assets/logo.png">
          <i class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </div>
      <div class="user-item">
        <span>手机</span>
        <span>{{ user_phone || "暂未设置" }}</span>
      </div>
      <div class="user-item">
        <span>邮箱</span>
        <span>{{ user_email|| "暂未设置" }}</span>
      </div>
      <div class="user-item">
        <span>昵称</span>
        <el-input
          type="text"
          placeholder="请输入内容"
          v-model="user_nickname"
          maxlength="15"
          show-word-limit
          clearable
          style="width:200px"
        >
        </el-input>
      </div>
      <div class="user-item">
        <span>QQ</span>
        <el-input
          type="text"
          placeholder="请输入内容"
          v-model="user_qq"
          maxlength="20"
          show-word-limit
          clearable
          style="width:200px"
        >
        </el-input>
      </div>
      <div class="user-item">
        <span>微信</span>
        <el-input
          type="text"
          placeholder="请输入内容"
          v-model="user_wechat"
          maxlength="20"
          show-word-limit
          clearable
          style="width:200px"
        >
        </el-input>
      </div>
      <div class="user-item">
        <span>性别</span>
        <el-select v-model="user_sex" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="user-item">
        <span>生日</span>
        <el-date-picker
          v-model="user_birthday"
          type="date"
          placeholder="选择日期"
          value-format="yyyy-MM-dd"
          :picker-options="allowedDate">
        </el-date-picker>
      </div>
      <el-button type="danger" @click="saveUserInfo()">修改</el-button>
    </div>
  </div>
</template>

<script>
import {mapState} from 'vuex'

export default {
  data () {
    return {
      user_nickname: '',
      user_sex: '',
      user_phone: '',
      user_birthday: '',
      user_email: '',
      user_avatar_imgUrl: '',
      user_qq: '',
      user_wechat: '',
      user_avatar: null,
      file: null,
      image: '',
      upload: new FormData(),
      options: [{
        value: '男',
        label: '男'
      }, {
        value: '女',
        label: '女'
      }],
      allowedDate: {
        disabledDate (time) {
          return time.getTime() > Date.now() - 8.64e6
        }
      }
    }
  },
  mounted () {
    this.user_nickname = JSON.parse(this.user.user).nick_name || ''
    this.user_sex = JSON.parse(this.user.user).gender || ''
    this.user_phone = JSON.parse(this.user.user).telephone || ''
    this.user_birthday = JSON.parse(this.user.user).birthday || ''
    this.user_email = JSON.parse(this.user.user).email || ''
    this.user_qq = JSON.parse(this.user.user).qq || ''
    this.user_wechat = JSON.parse(this.user.user).wechat || ''
    this.image = this.user.img || ''
  },
  computed: {
    ...mapState(['user'])
  },
  methods: {
    handleAvatarChange (file) {
      this.user_avatar_imgUrl = URL.createObjectURL(file.raw)
      this.upload.append('avatar', file.raw)
      var reader = new FileReader()
      reader.readAsDataURL(file.raw)
      reader.onload = () => {
        this.user_avatar = reader.result
      }
      reader.onerror = function (error) {
        console.log('Error: ', error)
      }
    },
    // 修改用户信息
    saveUserInfo () {
      const that = this
      that.upload.append('nick_name', this.user_nickname)
      that.upload.append('sex', this.user_sex)
      that.upload.append('qq', that.user_qq)
      that.upload.append('wechat', that.user_wechat)
      if (this.user_birthday !== '') {
        console.log(this.user_birthday)
        that.upload.append('birthday', this.user_birthday)
      }
      that.upload.get('avatar')
      that.$axios.post('/root'+'/user/update', that.upload).then(function (res) {
        if (res.status === 200) {
          if (that.upload.get('avatar')!==null){
            that.image = that.user_avatar
          }
          that.$store.commit('SET_USERIMG', that.image)
          that.upload = new FormData
          that.$axios.get('/root'+'/user/basic').then(function (res) {
            if (res.status === 200) {
              that.$store.commit('SET_USER', JSON.stringify(res.data))
            }
          })
        }
      })
    }
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
  font-weight: bold;
}
.user-detail-group .user-icon{
  height: 150px;
  padding: 0 10px;
  background-color: #fff;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.user-detail-group .user-item{
  height: 50px;
  padding: 0 10px;
  background-color: #fff;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.user-detail-group input{
  border: 1px solid #ccc;
  outline: none;
  text-align: right;
  width: 200px;
}
.user-detail-group button{
  margin: 60px 0;
  width: 15%;
  font-size: 16px;
  border-radius: 10px;
  border: none;
  outline: none;
}
.right-title-color{
  color: #999;
  font-size: 14px;
}
.avatar-uploader{
  position: relative;
  width: 100px;
  height: 100px;
}
.avatar-uploader /deep/ .el-upload.el-upload--text {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  cursor: pointer;
  overflow: hidden;
}
.avatar-uploader-icon {
  position: absolute;
  left: 0;
  top: 20px;
  font-size: 28px;
  color: #8c939d;
  width: 100%;;
  height: 100%;
  line-height: 56px;
  text-align: center;
  opacity: 0;
}
.avatar-uploader-icon:hover{
  opacity: 1;
}
.avatar {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
</style>
