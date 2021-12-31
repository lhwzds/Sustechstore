<template>
  <div class="password">
    <el-form class="user-account-key" ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="原密码" prop="password">
        <el-input type="password" placeholder="请输入原密码" v-model="form.password"></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input type="password" placeholder="请设置新密码" v-model="form.newPassword"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirm">
        <el-input type="password" placeholder="请确认新密码" v-model="form.confirm"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit('form')">保存</el-button>
        <el-button @click="$refs['form'].resetFields()">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'changePassword',
  data () {
    let validateNewPassword = (rule, value, callback) => {
      if (value === this.form.password) {
        return callback(new Error('新密码不能与原密码相同!'))
      } else if (value.length < 8) {
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
      form: {},
      rules: {
        password: [
          { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请设置新密码', trigger: 'blur' },
          { validator: validateNewPassword, trigger: 'blur' }
        ],
        confirm: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: validateconfirm, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    onSubmit (formName) {
      const that = this
      this.$refs[formName].validate(valid => {
        if (valid) {
          var form = new FormData()
          form.append('email', JSON.parse(this.$store.state.user.user).email)
          form.append('password', that.form.newPassword)
          that.$axios.post('/root'+'/user/password', form).then(function (resp) {
            if (resp.status === 200) {
              that.$message.success('密码修改成功')
            }
          })
        } else {
          this.$message.error('原密码错误')
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
.password {
  width: 70%;
  height: 100%;
  margin: 100px auto 20px;
}
</style>
