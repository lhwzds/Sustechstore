<template>
  <div id="me">
    <nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
      <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
          <a class="nav-link" @click="logout">退出登录</a>
        </li>
      </ul>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
          <div class="sidebar-sticky">
            <a class="navbar-brand">
              <img v-if="image" width="150px" height="150px" :src="image">
              <img v-else src="../assets/logo.png">
            </a>
            <el-menu
              default-active="1"
              class="el-menu-vertical-demo">
              <el-menu-item index="1"  @click="goTo('/center/information')">
                <i class="el-icon-document-copy"></i>
                <span slot="title">我的资料</span>
              </el-menu-item>
              <el-submenu index="2">
                <template slot="title">
                  <i class="el-icon-edit"></i>
                  <span>编辑信息</span>
                </template>
                <el-menu-item-group>
                  <el-menu-item index="2-1" @click="goTo('changeData')" >修改基本信息</el-menu-item>
                  <el-menu-item index="2-2" >修改密码</el-menu-item>
                </el-menu-item-group>
              </el-submenu>
              <el-submenu index="3">
                <template slot="title">
                  <i class="el-icon-collection-tag"></i>
                  <span>我的订单</span>
                </template>
              </el-submenu>
            </el-menu>
          </div>
        </nav>
      </div>
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
import {mapState} from 'vuex'
export default {
  name: 'Center',
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
  },
  methods: {
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
        this.$router.replace('/home')
        this.$store.commit('LOGOUT', this.$store.state.token)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出'
        })
      })
    },
    goTo (path) {
      this.$router.push(path)
    }
  }
}
</script>

<style scoped>
@media (min-width: 768px) {
  .bd-placeholder-img-lg {
    font-size: 3.5rem;
  }
}
#me {
  font-size: .875rem;
}

/*
 * Sidebar
 */
.sidebar {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  z-index: 100; /* Behind the navbar */
  padding: 48px 0 0; /* Height of navbar */
  box-shadow: inset -1px 0 0 rgba(0, 0, 0, .1);
}
.sidebar-sticky {
  position: relative;
  top: 0;
  width:200px;
  height: calc(100vh - 48px);
  padding-top: .5rem;
  overflow-x: hidden;
  overflow-y: auto; /* Scrollable contents if viewport is shorter than content. */
}

.sidebar .nav-link {
  font-weight: 500;
  color: #333;
}
.sidebar-sticky .navbar-brand {
  display: block;
  text-align: center;
  padding-top: 0.75rem;
  padding-bottom: 1.25rem;
  color: #12263f;
  margin-right: 0;
  font-size: 1.0625rem;
  line-height: inherit;
  white-space: nowrap;
  background: #f8f9fa;
}
.navbar-brand img{
  border-radius: 50%;
}
.sidebar .nav-link .feather {
  margin-right: 4px;
  color: #999;
}
.sidebar .nav-link.active {
  color: #007bff;
}
.sidebar .nav-link:hover .feather,
.sidebar .nav-link.active .feather {
  color: inherit;
}
</style>
