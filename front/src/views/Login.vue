<template>
  <div class="main">
    <div class="loginbox">
      <div class="loginbox-in">
        <div class="name">SUSTech Store</div>
        <el-form :model="loginForm" status-icon :rules="rules" ref="loginForm" label-width="100px" class="demo-ruleForm">
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" auto-complete="off" placeholder="请输入用户名">
              <i slot="prefix" class="el-input__icon el-icon-s-custom"
                 style="font-size: 20px"></i>
            </el-input>
          </el-form-item>
          <el-form-item prop="pass">
            <el-input type="password" v-model="loginForm.pass" auto-complete="off" placeholder="请输入密码" show-password>
              <i slot="prefix" class="el-input__icon el-icon-edit" style="font-size: 20px"></i>
            </el-input>
          </el-form-item>
        </el-form>
        <div class="tips"  style="float:left;">
          <el-row type="flex" class="dd">
            <el-col span:10 offset:1>
              <el-link type="white" @click="repassword()">忘记密码</el-link>
            </el-col>
            <el-col span:9 offset:4>
              <el-link type="white" @click="Register">还没有账号？点击注册</el-link>
            </el-col>
          </el-row>
          <el-row style="margin-left: 30px">
            <el-button  type="primary" size = 'medium' @click="submitForm('loginForm')">提交</el-button>
             <el-button  size='medium' @click="resetForm('loginForm')">重置</el-button>
          </el-row>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  data () {
    var checkName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('用户名不能为空'))
      }
      callback()
    }
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      }
      callback()
    }
    return {
      loginForm: {
        username: '',
        pass: ''
      },
      rules: {
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ],
        username: [
          { validator: checkName, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      const that = this
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          await this.$axios.post('/root'+'/login', {
            username: this.loginForm.username,
            pass: this.loginForm.pass
          }).then(function (resp) {
            if (resp.status === 200) { // resp.data表示返回的数据
              that.$store.commit('SET_TOKEN', resp.data.token)
            }
          }).catch(function (error) {
            if (error.response) {
              that.$message.error('用户名或密码错误')
            }
          })
          await this.$axios.get('/root'+'/user/basic').then(function (res) {
            if (res.status === 200) {
              that.$store.commit('SET_USER', JSON.stringify(res.data))
            }
          })
          await this.$axios.get('/root'+'/user/icon').then(function (res) {
            if (res.status === 200) {
              console.log(res.data)
              that.$store.commit('SET_USERIMG', res.data)
            }
            // eslint-disable-next-line handle-callback-err
          }).catch(function (error) {
            console.log(error)
          })
        } else {
          this.$message.warning('提交错误')
          return false
        }
        await that.$router.push('/')
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    Register () {
      this.$router.push('/register')
    },
    repassword () {
      this.$router.push('/retrieve')
    }
  }
}
</script>

<style scoped>
.loginbox{
  background:rgb(247, 187, 123);
  display:flex;
  position:absolute;
  width:500px;
  height:400px;
  top:45%;
  left:50%;
  transform:translate(-50%,-50%);
  box-shadow: 0 12px 16px 0  rgba(0,0,0,0.24), 0 17px 50px 0 #4E655D;
}
.loginbox-in{
  margin-left: 10px;
  width:500px;
  margin-right: 60px;
}
.tips{
  margin-left: 10px;
  width:400px;

}

.demo-ruleForm{
  margin-top:30px;
  margin-left:-30px;
}
.name{
  margin-top:40px;
  margin-left: 40px;
  font-weight:bold;
  font-size:30px;
  font-family: 微软雅黑,serif;
  color: #0e0101;
}
.main{
  background-image: url("./../assets/back.jpg");
  width:100%;
  height:100%;
  position:fixed;
  background-size:100% 100%
}
.dd {
  margin-bottom: 20px;
}
</style>
