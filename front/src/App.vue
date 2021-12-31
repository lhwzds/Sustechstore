<template>
  <div id="app">
      <router-view v-if="RouterState"/> 
  </div>
</template>

<script>

export default {
  name: 'App',
  provide() {
    return {
      reload: this.reload
    }
  },
  data() {
    return {
      RouterState: true
    }
  },
  methods: {
    reload() {
      this.RouterState = false
      this.$nextTick(() => {
        this.RouterState = true
      })
    }
  }
}
</script>

<style lang="less">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0px;
}
</style>
