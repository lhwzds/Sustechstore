<template>
<div class="main">
  <div class="retrievePass">
    <el-steps :active="active" finish-status="success">
      <el-step title="check"></el-step>
      <el-step title="retrieve"></el-step>
      <el-step title="finish"></el-step>
    </el-steps>
    <div class="steps-content">
     <div class="form-content">
      <el-form v-if="active === 0" :model="emailForm" status-icon :rules="emailRule" ref="emailForm">
        <el-form-item prop="email">
          <el-input v-model="emailForm.email" auto-complete="off" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="checkNumber">
          <el-input v-model="emailForm.checkNumber" >
            <template slot="append"><el-button plain :disabled="flag" @click="getAuthCode" slot="suffix">{{ msg }}</el-button></template>
          </el-input>
        </el-form-item>
      </el-form>
      <el-form v-if="active === 1" class="user-account-key" ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" placeholder="请设置新密码" v-model="form.newPassword"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirm">
          <el-input type="password" placeholder="请确认新密码" v-model="form.confirm"></el-input>
        </el-form-item>
        <div class="button">
          <el-button type="primary" @click="onSubmit('form')">保存</el-button>
          <el-button @click="$refs['form'].resetFields()">重置</el-button>
        </div>
      </el-form>
      <div v-if="active === 2" class="complete">
        <span>修改完成</span>
      </div>
     </div>
    </div>
    <div class="steps-action">
      <el-button v-if="active < 2" type="primary" @click="next">
        下一步
      </el-button>
      <el-button
        v-if="active === 2"
        type="primary"
        @click="go_login()"
      >
        完成
      </el-button>
      <el-button v-if="active>0" style="margin-left: 8px" @click="prev">
        上一步
      </el-button>
    </div>
  </div>
</div>
</template>
<script>
export default {
  data () {
    let validateEmail = (rule, value, callback) => {
      if (value === '' && this.emailForm.email === '') {
        return callback(new Error('邮箱不能为空'))
      }
      let regEmail = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
      if (!regEmail.test(value)) {
        return callback(new Error('邮箱不合法'))
      }
      callback()
    }
    let validateNewPassword = (rule, value, callback) => {
      if (value.length < 8) {
        return callback(new Error('密码长度太短'))
      } else {
        callback()
      }
    }
    let validateconfirm = (rule, value, callback) => {
      if (value !== this.form.newPassword) {
        return callback(new Error('与新密码不一致!'))
      } else if (value.length < 8) {
        return callback(new Error('密码长度太短'))
      } else {
        callback()
      }
    }
    return {
      active: 0,
      flag: false,
      first_check: false,
      store_check: '',
      msg: '获取验证码',
      emailForm: {
        email: '',
        checkNumber: ''
      },
      form: {},
      rules: {
        newPassword: [
          { required: true, message: '请设置新密码', trigger: 'blur' },
          { validator: validateNewPassword, trigger: 'blur' }
        ],
        confirm: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: validateconfirm, trigger: 'blur' }
        ]
      },
      emailRule: {
        email: [
          { validator: validateEmail, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    next () {
      if (this.store_check === this.emailForm.checkNumber && this.store_check !== '') {
        if (this.active === 1) {
          if (this.form.newPassword !== '' && this.form.confirm !== '') {
            this.active++
          }
        else {
          this.$message.warning('请重设密码')
        }
      } else {
        this.active++
      }
      } else {
        this.$message.warning('验证码不正确')
      }
    },
    prev () {
      this.active--
    },
    go_login () {
      this.$message.success('密码重置成功!')
      this.$router.push('/login')
    },
    getAuthCode () {
      this.$refs['emailForm'].validate(async (valid) => {
        console.log(valid)
        if (valid) {
          const that = this
          that.flag = true
          var time = 30
          var timer = null
          timer = setInterval(function () {
            if (time === 0) {
              that.msg = '重新获取验证码'
              that.flag = false
              clearInterval(timer)
            } else {
              that.msg = time + '秒后重新获取'
              time--
            }
          }, 1000)
          that.store_check = this.get_random()
          var form = new FormData()
          form.append('email', that.emailForm.email)
          form.append('random', that.store_check)
          await this.$axios.post('/root'+'/email/code', form).then(function (res) {
            if (res.status === 200) {
              that.first_check = true
            }
            // eslint-disable-next-line handle-callback-err
          }).catch(function (error) {
            console.log(error)
            that.$message.error('用户邮箱不正确')
          })
        }
      }
      )
    },
    get_random () {
      return Math.random().toFixed(6).slice(-6)
    },
    onSubmit (formName) {
      const that = this
      this.$refs[formName].validate(valid => {
        if (valid) {
          var form = new FormData()
          form.append('password', that.form.newPassword)
          form.append('email', that.emailForm.email)
          that.$axios.post('/root'+'/user/password', form).then(function (resp) {
            if (resp.status === 200) {
              that.$message.success('密码修改成功')
            }
          })
        } else {
          this.$message.error('新密码不合法')
          return false
        }
      })
    }
  }
}
</script>
<style scoped>
.steps-content {
  margin-top: 20px;
  border: 1px dashed #e9e9e9;
  border-radius: 6px;
  background-color: #fafafa;
  min-height: 200px;
  text-align: center;
  padding-top: 50px;
}

.steps-action {
  margin-top: 24px;
}
.retrievePass {
  margin-top:100px;
  width: 600px;
  margin-left: 500px;
}
.main{
  background-image: url("./../assets/back.jpg");
  width:100%;
  height:100%;
  position:fixed;
  background-size:100% 100%
}
.form-content{
  margin-left: 10%;
  margin-right: 10%;
}
</style>
