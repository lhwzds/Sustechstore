<template>
  <div id="me">
    <el-backtop></el-backtop>
    <InfHead></InfHead>
    <div class="container-fluid">
      <div class="row">
        <nav class="sidebar">
          <div class="sidebar-sticky">
            <div class="navbar-brand">
              <img v-if="image" :src="image" style="height:150px;width:150px;">
              <img v-else src="src\assets\logo.png" style="height:150px;width:150px;">
            </div>
            <el-menu
              default-active="1"
              class="el-menu-vertical-demo"  style="margin-bottom:10px;">
              <el-menu-item index="1"  @click="goTo('/center/information')" >
                <i class="el-icon-document-copy"></i>
                <span style="font-size:18px">我的资料</span>
              </el-menu-item>
              <el-submenu index="2" style="margin-bottom: 10px">
                <template slot="title">
                  <i class="el-icon-edit"></i>
                  <span style="font-size:18px">编辑信息</span>
                </template>
                <el-menu-item-group>
                  <el-menu-item index="2-1" @click="goTo('changeData')" >修改基本信息</el-menu-item>
                  <el-menu-item index="2-2" @click="goTo('changePassword')">修改密码</el-menu-item>
                </el-menu-item-group>
              </el-submenu>
              <el-submenu index="3"  style="margin-bottom: 10px">
                <template slot="title">
                  <i class="el-icon-shopping-cart-2"></i>
                  <span style="font-size:18px">购买信息</span>
                </template>
                  <el-menu-item-group>
                    <el-menu-item index="3-1" @click="goTo('transactions')" >已发布商品</el-menu-item>
                    <el-menu-item index="3-2" @click="goTrolley">购物车</el-menu-item>
                    <el-menu-item index="3-3" @click="goBought">购买记录</el-menu-item>
                  </el-menu-item-group>
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
import InfHead from '../components/InfHead'
export default {
  name: 'Center',
  components: {InfHead},
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
    goTo (path) {
      this.$router.push(path)
    },
    goTrolley() {
      this.$store.commit('SET_RELOAD', true)
      this.$router.push('/trolley')
    },
    goBought() {
      this.$store.commit('SET_RELOAD', true)
      this.$router.push('/bought')
    }
  }
}
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 40px;
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
.navbar-brand img{
  border-radius: 50%;
}
.navbar-brand {
  margin-bottom: 15px;
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
