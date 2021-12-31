<template>
  <div class="chatting-main">
    <HeadsResponse>
    </HeadsResponse>
    <ChatListResponse>
    </ChatListResponse>
  </div>
</template>

<script>
import HeadsResponse from '../components/HeadsResponse'
import ChatListResponse from '../components/ChatListResponse'


export default {
  name: 'ChattingResponse',
  components: {
    HeadsResponse,
    ChatListResponse
  }
}
</script>

<style>
  .chatting-main {
    /*background-color: #324057;*/
    margin: 0px;
    padding: 0px;
    font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
    /*font-weight: 400;
    -webkit-font-smoothing: antialiased;*/
    overflow: hidden;
  }
  
  
</style>