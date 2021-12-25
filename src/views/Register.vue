<template>
  <div class="main">
    <div class="loginbox">
      <div class="loginbox-in">
        <div class="name">SUSTech Store</div>
        <div class="content">
        <el-form :model="RegisterForm" status-icon :rules="rules" ref="RegisterForm" label-width="170px" size="mini" class="demo-ruleForm">
          <el-form-item label="昵称" prop="nick_name">
            <el-row :span="21">
            <el-input v-model="RegisterForm.nick_name" auto-complete="off" sytle="height=30%"></el-input>
            </el-row>
          </el-form-item>
          <el-form-item label="用户名" prop="user_name">
            <el-input type="username" v-model="RegisterForm.user_name" auto-complete="off" ></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input type="email" v-model="RegisterForm.email" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-input type="phone" v-model="RegisterForm.phone" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="pass" v-model="RegisterForm.password"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPass">
            <el-input type="checkpass" v-model="RegisterForm.checkPass"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button  type="primary" size = 'medium' @click="submitForm('RegisterForm')">注册</el-button>
            <el-button size="medium" @click="Login()">登录</el-button>
          </el-form-item>
        </el-form></div>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  data () {
    let checkName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('名称不能为空'))
      }
      callback()
    }
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (this.RegisterForm.checkPass !== '') {
          if (value !== this.RegisterForm.checkPass) {
            callback(new Error('密码不相同'))
          }
        }
        callback()
      }
    }
    let validateCheckpass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.RegisterForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    let validateCheckemail = (rule, value, callback) => {
      if (value === '' && this.RegisterForm.email === '') {
        return callback(new Error('邮箱不能为空'))
      }
      let regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
      if (!regEmail.test(value)) {
        return callback(new Error('邮箱不合法'))
      }
      callback()
    }
    let validateCheckphone = (rule, value, callback) => {
      if (value === '' && this.RegisterForm.email === '') {
        return callback(new Error('手机号码不能为空'))
      }
      let regPhone = /^1[34578]\d{9}$/
      if (!regPhone.test(value)) {
        return callback(new Error('手机号码不合法'))
      }
      callback()
    }
    return {
      RegisterForm: {
        nick_name: '',
        user_name: '',
        password: '',
        checkPass: '',
        email: '',
        phone: ''
      },
      rules: {
        password: [
          { validator: validatePass, trigger: 'blur' }
        ],
        user_name: [
          { validator: checkName, trigger: 'blur' }
        ],
        checkPass: [
          {
            validator: validateCheckpass, trigger: 'blur'
          }
        ],
        nick_name: [
          {
            validator: checkName, trigger: 'blur'
          }
        ],
        phone: [
          {
            validator: validateCheckphone, trigger: 'blur'
          }
        ],
        email: [
          {
            validator: validateCheckemail, trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      const that = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/a', {
            nick_name: this.RegisterForm.nick_name,
            user_name: this.RegisterForm.user_name,
            password: this.RegisterForm.password,
            email: this.RegisterForm.email,
            telephone: this.RegisterForm.phone
          }).then(function (resp) {
            if (resp.status === 200) { // resp.data表示返回的数据
              that.$router.push('/')
            }
          }).catch(function (error) {
            if (error.response) {
              that.$message.error('用户名重复')
            }
          })
        } else {
          this.$message.warning('提交错误')
          return false
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    Login () {
      this.$router.back()
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
  width:800px;
}
.content{
  left:20%;
  width:700px;
  height:600px;
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
