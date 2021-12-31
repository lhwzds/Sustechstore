<template>
    <div>
        <section class="chatlist" :class="showSelBox>0?'chatlist-bottom-collapse':'chatlist-bottom'">
            <ul>
                <div v-for="item in records" :key="item.id">
                    <li class="chat-mine" v-if="item.type==1" style="list-style-type:none;">
                        <div class="chat-user"><img :src="mineImg"></div>
                        <div class="time"><cite><i>{{item.time}}</i>{{mineName}}</cite></div>
                        <div class="chat-text" v-html="item.content"></div>
                    </li>
                    <li class="chat-other" v-if="item.type==2" style="list-style-type:none;">
                        <div class="chat-user"><img :src="otherImg"></div>
                        <div class="time"><cite>{{otherName}}<i>{{item.time}}</i></cite></div>
                        <div class="chat-text" v-html="item.content"></div>
                    </li>
                </div>
            </ul>
        </section>

        <section class="foot">
            <!-- <mt-field id="txtContent" placeholder="请输入消息" class="con" v-model="content"></mt-field> -->
            <el-col :span="23" >
                <el-input class="txtContent" v-model="content" placeholder="请输入消息" ></el-input>
            </el-col>
            <el-col :span="1" :offset="8">
                <span class="btn btn-send" v-on:click="sendMsg" style="cursor:pointer;">发送</span>
            </el-col>
            
            <!-- <span class="btn-face" v-on:click="showSelBox=showSelBox==1?0:1"><i class="fa fa-smile-o" aria-hidden="true"></i></span> -->        
            <!-- <section class="selbox" :class="showSelBox>0?'show':'hide'">
                <section v-show="showSelBox==1" class="face-box">
                    <mt-swipe :auto="0" :continuous="false">
                        <mt-swipe-item v-for="n in Math.ceil(EXPS.length/18)" :key="n">
                            <li v-for="(item, index) in getEXP(n,18)" :key="index">
                                <img :src="'static/emotion/'+item.file" alt="" :data="item.code" v-on:click="content+=item.code">
                            </li>
                        </mt-swipe-item>
                    </mt-swipe>
                </section>
                <div v-show="showSelBox==2">{{selOther}}</div>
            </section> -->
        </section>

    </div>
</template>

<script>
import util from '../utils/util.js'
import { Toast } from 'mint-ui';
import axios from 'axios';
export default {
    name: 'chatlist',
    data() {
        return {
            websock: null,
            showSelBox: 0, // 0隐藏 1显示表情 2显示其他
            selFace: '表情',
            selOther: '其他功能',
            content:'',
            topStatus: '',
            //个人信息
            mineImg: null,
            mineName:'',
            mineID:'',
            mineToken:'',
            //对方信息
            otherImgName:'',
            otherImg: null,
            otherName:'',
            otherID:'',
            //聊天记录
            records: [{
                id:1,
                type: 1,
                time: util.formatDate.format(new Date(),'yyyy-MM-dd hh:mm:ss'),
                content: '你好！'
            }, {
                id: 2,
                type: 2,
                time: util.formatDate.format(new Date(),'yyyy-MM-dd hh:mm:ss'),
                content: '你好！'
            }],
        }
    },
    created() {
        const that =this;
        this.mineImg=that.$store.state.user.img;
        this.mineName=JSON.parse(that.$store.state.user.user).nick_name;
        this.mineID=JSON.parse(that.$store.state.user.user).id;
        this.mineToken=that.$store.state.user.token;
        console.log(that.$store.state.image_store.seller_info)
        this.otherImgName = that.$store.state.image_store.seller_info.avatar
        this.otherName = that.$store.state.image_store.seller_info.publisher
        this.otherID = that.$store.state.image_store.seller_info.publisherid
      
        console.log('mineId is '+this.mineID);
        console.log('mineName is '+this.mineName);
        console.log('otherId is '+this.otherID);
        console.log('otherImgName is '+this.otherImgName)
        this.initWebSocket();
    },
    destroyed() {
      this.websock.close() 
    },
    methods: {
        getEXP: function (pageNow,pageSize) {
            return this.EXPS.slice((pageNow - 1) * pageSize, pageSize * pageNow)
        },
        //发送消息
        sendMsg: function(){

            if(this.content==''){
                Toast('请输入消息');
                return;
            }

            var send=this.mineID+','+this.otherID+','+this.content
            this.websocketsend(send);

            this.records.push({
                type: 1,
                time: util.formatDate.format(new Date(),'yyyy-MM-dd hh:mm:ss'),
                name: this.mineName,
                content: this.content
            });

            this.content='';
            this.scrollToBottom();
   
        },
      
        focusTxtContent:function(){
            document.querySelector("#txtContent input").focus();
        },
    
        scrollToBottom:function(){
            setTimeout(function(){
                var chatlist = document.getElementsByClassName('chatlist')[0];
                chatlist.scrollTop=chatlist.scrollHeight;
            },100);
        },
      
        handleTopChange(status) {
            this.topStatus = status;
        },
        loadTop(id) {
            var _this=this;
            setTimeout(() => {
                var chatlist = document.getElementsByClassName('chatlist')[0];
                var oldHeight=chatlist.scrollHeight;

                _this.records.unshift({
                    type: 1,
                    time: util.formatDate.format(new Date(),'yyyy-MM-dd hh:mm:ss'),
                    name: '你',
                    content: '你好！'
                }, {
                    type: 2,
                    time: util.formatDate.format(new Date(),'yyyy-MM-dd hh:mm:ss'),
                    name: '对方',
                    content: '你好！'
                });

                setTimeout(function(){
                    var newHeight=chatlist.scrollHeight;
                    chatlist.scrollTop=newHeight-oldHeight;
                },100);

                this.$refs.loadmore.onTopLoaded(id);
            }, 1500);
        },
      initWebSocket(){ //初始化weosocket
        const wsuri = "ws://106.52.122.142:8888/websocket";
        // const wsuri = "ws://10.26.115.152:8888/websocket";
        // const wsuri = "ws://localhost:8888/websocket";
        this.websock = new WebSocket(wsuri);
        this.websock.onmessage = this.websocketonmessage;
        this.websock.onopen = this.websocketonopen;
        this.websock.onerror = this.websocketonerror;
        this.websock.onclose = this.websocketclose;
      },
      websocketonopen(){ //连接建立之后执行send方法发送数据
        let actions = this.mineID;
        // this.websocketsend(JSON.stringify(actions));
        this.websocketsend(actions);
      },
      websocketonerror(){//连接建立失败重连
        this.initWebSocket();
      },
      websocketonmessage(e){ //数据接收
        console.log(e.data)
        this.records.push({
            type: 2,
            time: util.formatDate.format(new Date(),'yyyy-MM-dd hh:mm:ss'),
            name: this.otherName,
            content: e.data
        });
        // const redata = JSON.parse(e.data);
        // console.log('receive'+redata)
      },
      websocketsend(Data){//数据发送
        if (this.websock.readyState === this.websock.OPEN) {
                this.websock.send(Data);
        }
        else if (this.websock.readyState === this.websock.CONNECTING) {
            let that = this;//保存当前对象this
            setTimeout(function () {
                that.websock.send(Data);
            }, 300);
        }
        else {
            let that = this;
            setTimeout(function () {
                that.websock.send(Data);
            }, 500);
        }
        console.log('send'+Data)
      },
      websocketclose(e){  //关闭
        console.log('断开连接',e);
      },
    },
    async mounted() {
        const that=this;
        this.scrollToBottom();

        let formdata = new FormData()
        formdata.append('mineId', this.mineID)
        formdata.append('otherId', this.otherID)

        await axios({
        url:"/root"+"/chathistory",
        method:'post',
        data:formdata
        }).then(res => (this.records = res.data))

        console.log('Chat history is'+this.records)
        
        await this.$axios.get('/root' + '/items/picture/' + that.otherImgName).then(function (res) {
        if (res.status === 200) {
            that.otherImg = res.data}
        }).catch(function (error) {
        console.log(error)
        })

    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
    .chatlist {
        position: absolute;
        top: 60px;
        bottom: 48px;
        left: 0px;
        right: 0px;
        overflow-y: scroll;
        overflow-x: hidden;
        padding: 10px;
    }
    
    .chatlist-bottom {
        bottom: 48px;
    }
    
    .chatlist-bottom-collapse {
        bottom: 198px;
    }
    
    .chatlist ul {
        min-height: 300px;
    }
    
    .chatlist ul .chat-mine {
        text-align: right;
        padding-left: 0;
        padding-right: 60px;
    }

    .chatlist ul .chat-other {
        text-align: left;
        padding-left: 30px;
        padding-right: 0;
    }
    
    .chatlist ul li {
        position: relative;
        /* font-size: 0; */
        margin-bottom: 10px;
        padding-left: 60px;
        min-height: 68px;
    }
    
    .chat-mine .chat-user {
        left: auto;
        right: 3px;
    }

    .chat-other .chat-user {
        right: auto;
        left: -25px;
    }
    
    .chat-user {
        position: absolute;
        /* left: 3px; */
    }
    
    /* .chat-text, */
    .chat-user {
        display: inline-block;
        vertical-align: top;
        /*font-size: 14px;*/
    }
    
    .chat-user img {
        width: 45px;
        height: 45px;
        border-radius: 100%;
    }
    
    .time {
        width: 100%;
    }
    
    cite {
        left: auto;
        right: 60px;
        text-align: right;
    }
    
    cite {
        font-style: normal;
        line-height: 24px;
        font-size: 20px;
        white-space: nowrap;
        color: #999;
        text-align: left;
    }
    
    cite i {
        font-style: normal;
        padding-left: 5px;
        padding-right: 5px;
        font-size: 12px;
    }
    
    .chat-mine .chat-text {
        margin-left: 0;
        text-align: left;
        background-color: #FF5809;
        color: #fff;
    }

    .chat-other .chat-text {
        margin-right: 0;
        text-align: right;
        background-color: #444647;
        color: #fff;
    }

    .chat-text {
        position: relative;
        line-height: 22px;
        margin-top: 10px;
        padding: 10px 15px;
        background-color: #eee;
        border-radius: 3px;
        color: #333;
        word-break: break-all;
        max-width: 462px\9;
    }
    
    .chat-text,
    .chat-user {
        display: inline-block;
        vertical-align: top;
        font-size: 14px;
    }
    
    .chat-text img {
        max-width: 100%;
        vertical-align: middle;
    }
    
    .chat-user {
        position: absolute;
        left: 3px;
    }
    
    .chat-text:after {
        content: '';
        position: absolute;
        left: -10px;
        top: 15px;
        width: 0;
        height: 0;
        border-style: solid dashed dashed;
        border-color: #eee transparent transparent;
        overflow: hidden;
        border-width: 10px;
    }
    
    .chat-mine .chat-text:after {
        left: auto;
        right: -10px;
        border-top-color: #FF5809;
    }
    .chat-other .chat-text:after {
        right: auto;
        left: -10px;
        border-top-color: #444647;
    }
    
    .foot {
        width: 100%;
        min-height: 4px;
        position: fixed;
        bottom: 4px;
        left: 0px;
        background-color: #F8F8F8;
    }
    
    .foot .con {
        position: absolute;
        left: 66px;
        right: 40px;
    }
    
    .foot .btn-plus {
        width: 28px;
        padding: 9px 3px;
        position: absolute;
        left: 0px;
        border-left: 1px solid #d9d9d9;
    }
    
    .foot .btn-plus i {
        font-size: 2em;
        color: #ccc;
    }
    
    .foot .btn-face {
        width: 28px;
        padding: 9px 3px 9px 0px;
        position: absolute;
        left: 35px;
        /*border-right: 1px solid #d9d9d9;*/
    }
    
    .foot .btn-face i {
        font-size: 2em;
        color: #d9d9d9;
    }
    
    .foot .selbox {
        height: 150px;
        margin-top: 48px;
        border-top: 1px solid #d9d9d9;
    }
    
    .show {
        display: block;
    }
    
    .hide {
        display: none;
    }
    
    .face-box {
        position: absolute;
        top: 48px;
        left: 0px;
        right: 0px;
        bottom: 0px;
        padding: 15px 15px 0px 15px;
        overflow: hidden;
        width: 290px;
        margin: 0px auto;
        height: 135px;
    }
    
    .face-box li {
        width: 30px;
        float: left;
        padding: 6px 10px 0px 8px;
    }
    
    .btn {
        display: inline-block;
        vertical-align: top;
        font-size: 14px;
        line-height: 32px;
        margin-left: 5px;
        padding: 0 6px;
        background-color: #FF5809;
        color: #fff;
        border-radius: 3px;
    }
    
    .btn-send {
        position: absolute;
        right: 3px;
        top: 6px;
    }

    /* .txtContent {
       margin-right: 40px; 
    } */
</style>