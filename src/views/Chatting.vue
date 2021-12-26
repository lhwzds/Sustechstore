<template>
  <div class="chatting-main">
    <Heads>
    </Heads>
    <ChatList>
    </ChatList>
  </div>
</template>

<script>
import Heads from '../components/Heads'
import ChatList from '../components/ChatList'


export default {
  name: 'Chatting',
  components: {
    Heads,
    ChatList
  }
}
</script>

<style>
  .chatting-main {
    /* background-color: #324057; */
    margin: 0px;
    padding: 0px;
    font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
    /*font-weight: 400;
    -webkit-font-smoothing: antialiased;*/
    overflow: hidden;
  }
  
</style>