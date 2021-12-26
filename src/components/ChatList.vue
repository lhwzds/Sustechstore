<template>
    <div>
        <section class="chatlist" :class="showSelBox>0?'chatlist-bottom-collapse':'chatlist-bottom'">
            <ul>
                <div v-for="item in records" :key="item.id">
                    <li class="chat-mine" v-if="item.type==1" style="list-style-type:none;">
                        <div class="chat-user"><img :src="mineImg"></div>
                        <div class="time"><cite><i>{{item.time}}</i>{{mineName}}</cite></div>
                        <div class="chat-text" v-html="replaceFace(item.content)"></div>
                    </li>
                    <li class="chat-other" v-if="item.type==2" style="list-style-type:none;">
                        <div class="chat-user"><img src="../assets/default.png"></div>
                        <div class="time"><cite>{{item.name}}<i>{{item.time}}</i></cite></div>
                        <div class="chat-text" v-html="replaceFace(item.content)"></div>
                    </li>
                </div>
            </ul>
        </section>

        <section class="foot">
            <mt-field id="txtContent" placeholder="请输入消息" class="con" v-model="content"></mt-field>
            <span class="btn-face" v-on:click="showSelBox=showSelBox==1?0:1"><i class="fa fa-smile-o" aria-hidden="true"></i></span>
            <span class="btn-plus" v-on:click="showSelBox=showSelBox==2?0:2"><i class="fa" aria-hidden="true" :class="showSelBox==2?'fa-minus-circle':'fa-plus-circle'"></i></span>
            <span class="btn btn-send" v-on:click="sendMsg">发送</span>
            <section class="selbox" :class="showSelBox>0?'show':'hide'">
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
            </section>
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
            otherImg: null,
            otherName:'',
            otherID:'',
            //聊天记录
            records: [{
                id:1,
                type: 1,
                time: util.formatDate.format(new Date(),'yyyy-MM-dd hh:mm:ss'),
                name: '田荫熙',
                content: '你好，我是田荫熙'
            }, {
                id: 2,
                type: 2,
                time: util.formatDate.format(new Date(),'yyyy-MM-dd hh:mm:ss'),
                name: '李怀武',
                content: '你好，我是李怀武'
            }],
            // 表情
            EXPS: [
                { file: '100.gif', code: '/::)', title: '微笑',reg:/\/::\)/g },
                { file: '101.gif', code: '/::~', title: '伤心',reg:/\/::~/g },
                { file: '102.gif', code: '/::B', title: '美女',reg:/\/::B/g },
                { file: '103.gif', code: '/::|', title: '发呆',reg:/\/::\|/g },
                { file: '104.gif', code: '/:8-)', title: '墨镜',reg:/\/:8-\)/g },
                { file: '105.gif', code: '/::<', title: '哭',reg:/\/::</g },
                { file: '106.gif', code: '/::$', title: '羞',reg:/\/::\$/g },
                { file: '107.gif', code: '/::X', title: '哑',reg:/\/::X/g },
                { file: '108.gif', code: '/::Z', title: '睡',reg:/\/::Z/g },
                { file: '109.gif', code: '/::\'(', title: '哭',reg:/\/::'\(/g },
                { file: '110.gif', code: '/::-|', title: '囧',reg:/\/::-\|/g },
                { file: '111.gif', code: '/::@', title: '怒',reg:/\/::@/g },
                { file: '112.gif', code: '/::P', title: '调皮',reg:/\/::P/g },
                { file: '113.gif', code: '/::D', title: '笑',reg:/\/::D/g },
                { file: '114.gif', code: '/::O', title: '惊讶',reg:/\/::O/g },
                { file: '115.gif', code: '/::(', title: '难过',reg:/\/::\(/g },
                { file: '116.gif', code: '/::+', title: '酷',reg:/\/::\+/g },
                { file: '117.gif', code: '/:--b', title: '汗',reg:/\/:--b/g },
                { file: '118.gif', code: '/::Q', title: '抓狂',reg:/\/::Q/g },
                { file: '119.gif', code: '/::T', title: '吐',reg:/\/::T/g },
                { file: '120.gif', code: '/:,@P', title: '笑',reg:/\/:,@P/g },
                { file: '121.gif', code: '/:,@-D', title: '快乐',reg:/\/:,@-D/g },
                { file: '122.gif', code: '/::d', title: '奇',reg:/\/::d/g },
                { file: '123.gif', code: '/:,@o', title: '傲' ,reg:/\/:,@o/g},
                { file: '124.gif', code: '/::g', title: '饿',reg:/\/::g/g },
                { file: '125.gif', code: '/:|-)', title: '累' ,reg:/\/:\|-\)/g},
                { file: '126.gif', code: '/::!', title: '吓',reg:/\/::!/g },
            ]
        }
    },
    created() {
        const that =this;
        this.mineImg=that.$store.state.user.img;
        this.mineName=JSON.parse(that.$store.state.user.user).nick_name;
        this.mineID=JSON.parse(that.$store.state.user.user).id;
        this.mineToken=that.$store.state.user.token;
        if (this.mineID==2){
            this.otherID='1';
            console.log('mine is 2')
        }else{
            this.otherID='2';
            console.log('mine is 1')
        }
        console.log('mineId is '+this.mineID);
        console.log('otherId is '+this.ohterID);
        console.log('mineToken is '+JSON.stringify(this.mineToken));
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

            var send=this.mineID+','+this.ohterID+','+this.content
            this.websocketsend(send);

            this.records.push({
                type: 1,
                time: util.formatDate.format(new Date(),'yyyy-MM-dd hh:mm:ss'),
                name: '田荫熙',
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
      
        replaceFace:function(con){
            var exps=this.EXPS;
            for(var i=0;i<exps.length;i++){
                //con = con.replace(new RegExp(exps[i].code,'g'), '<img src="static/emotion/' + exps[i].file +'"  alt="" />');
                con = con.replace(exps[i].reg, '<img src="static/emotion/' + exps[i].file +'"  alt="" />');
            }
            return con;
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
        // const wsuri = "ws://database.sustechstore.com:8888/websocket";
        const wsuri = "ws://localhost:8888/websocket";
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
            name: '李怀武',
            content: e.data
        });
        // const redata = JSON.parse(e.data);
        // console.log('receive'+redata)
      },
      websocketsend(Data){//数据发送
        this.websock.send(Data);
        console.log('send'+Data)
      },
      websocketclose(e){  //关闭
        console.log('断开连接',e);
      },
    },
    async mounted() {
        this.scrollToBottom();
        this.focusTxtContent();

        let formdata = new FormData()
        formdata.append('mineId', this.mineID)
        // formdata.append('otherId', this.otherID)
        if (formdata.get('mineId')==2){
            formdata.append('otherId', 1)
        }else{
            formdata.append('otherId', 2)
        }
        await axios({
        url:"/root"+"/chathistory",
        method:'post',
        data:formdata
        }).then(res => (this.records = res.data))
        console.log(this.records)
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
        width: 40px;
        height: 40px;
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
        font-size: 12px;
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
        background-color: #33DF83;
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
        margin-top: 25px;
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
        border-top-color: #33DF83;
    }
    .chat-other .chat-text:after {
        right: auto;
        left: -10px;
        border-top-color: #444647;
    }
    
    .foot {
        width: 100%;
        min-height: 48px;
        position: fixed;
        bottom: 0px;
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
        background-color: #33DF83;
        color: #fff;
        border-radius: 3px;
    }
    
    .btn-send {
        position: absolute;
        right: 0px;
        top: 8px;
    }
</style>