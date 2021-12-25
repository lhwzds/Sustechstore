<template>
  <div class="main">
    <div class="loginbox">
      <div class="loginbox-in">
        <div class="name">SUSTech Store</div>
        <el-form :model="loginForm" status-icon :rules="rules" ref="loginForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="loginForm.username" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="pass">
            <el-input type="password" v-model="loginForm.pass" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button  type="primary" size = 'medium' @click="submitForm('loginForm')">提交</el-button>
            <el-button  size='medium' @click="resetForm('loginForm')">重置</el-button>
          </el-form-item>
          <el-form-item>
            <el-button size="medium" @click="Register()">注册</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="background">
        <div class="title">Welcome to SUSTech Store</div>
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
          await this.$axios.get('/b').then(function (res) {
            if (res.status === 200) {
              console.log(res.data)
              that.$store.commit('SET_USER', JSON.stringify(res.data))
            }
          })
          await this.$axios.get('/icon').then(function (res) {
            if (res.status === 200) {
              console.log(res.data)
              that.$store.commit('SET_IMG', res.data)
            }
          })
        } else {
          this.$message.warning('提交错误')
          return false
        }
        await that.$router.push('/center')
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    Register () {
      this.$router.push('/register')
    }
  }
}
</script>

<style scoped>
.loginbox{
  display:flex;
  position:absolute;
  width:800px;
  height:400px;
  top:45%;
  left:50%;
  transform:translate(-50%,-50%);
  box-shadow: 0 12px 16px 0  rgba(0,0,0,0.24), 0 17px 50px 0 #4E655D;
}
.loginbox-in{
  background-image: url('./../assets/b.jpg');
  width:250px;
}
.background{
  width:570px;
  background-image:url('./../assets/b.jpg');
  background-size:560px 450px;
  font-family:Helvetica Neue,serif;
}
.title{
  margin-top:75px;
  font-weight:bold;
  font-size:20px;
  color:#4E655D;
}

.demo-ruleForm{
  margin-top:30px;
  margin-left:-30px;
}
.name{
  margin-top:40px;
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
</style>
