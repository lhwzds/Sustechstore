package com.sustechstore.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sustechstore.dao.Chat;
import com.sustechstore.dao.ChatRead;
import com.sustechstore.mapper.ChatMapper;
import com.sustechstore.mapper.ChatReadMapper;
import com.sustechstore.service.ChatReadServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.sustechstore.utils.UserChannelUtils.channel_user_map;
import static com.sustechstore.utils.UserChannelUtils.user_channel_map;

@Component
@ChannelHandler.Sharable
//@ServerEndpoint(value="/websocket/chatroom")
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private ChatReadMapper chatReadMapper;
    @Autowired
    private ChatReadServiceImpl chatReadService;

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    WebSocketServerHandshaker handshaker;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;

            //???????????????????????????websocket
            if(!request.decoderResult().isSuccess()||!"websocket".equals(request.headers().get("Upgrade"))){
                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);

                if(response.status().code()!=200){
                    ByteBuf byteBuf = Unpooled.copiedBuffer("????????????", CharsetUtil.UTF_8);
                    response.content().writeBytes(byteBuf);
                    byteBuf.release();
                }
                ctx.writeAndFlush(response);
                return;
            }
//            WebSocketServerHandshakerFactory webSocketServerHandshakerFactory = new WebSocketServerHandshakerFactory("ws://127.0.0.1:8888/websocket",null,false);
            WebSocketServerHandshakerFactory webSocketServerHandshakerFactory = new WebSocketServerHandshakerFactory("ws://10.0.8.14:8888/websocket",null,false);
            handshaker = webSocketServerHandshakerFactory.newHandshaker(request);

            if(handshaker == null){
                webSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            }else{
                handshaker.handshake(ctx.channel(),request);
            }
        }
        //?????????webSokect??????
        else if (msg instanceof WebSocketFrame){
            handleWebSocket(ctx,(WebSocketFrame)msg);
        }
    }
    public void handleWebSocket(ChannelHandlerContext ctx, WebSocketFrame msg) {
        // ????????????????????????????????????
        if (msg instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) msg.retain());
            return;
        }
        // ???????????????Ping??????
        if (msg instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
            return;
        }
        // ???????????????????????????????????????
        if (msg instanceof TextWebSocketFrame) {
            // ??????????????????
            String requestmsg = ((TextWebSocketFrame) msg).text();
            System.out.println("websocket??????======" + requestmsg);
            String[] array = requestmsg.split(",");
            Channel incoming = ctx.channel();
            // ??????????????????????????????
            //System.out.println(array[0]+incoming);
            //System.out.println("message");
            user_channel_map.put(Integer.parseInt(array[0]), incoming);
            channel_user_map.put(incoming, Integer.parseInt(array[0]));
            if (array.length == 3) {
                // ??????????????????h5
                String sendid = array[0];
                String friendid = array[1];
                String messageid = array[2];
                Channel to = user_channel_map.get(Integer.parseInt(friendid));
                if(to!=null) {
                    to.writeAndFlush(new TextWebSocketFrame(messageid + "\n"));
                    List<ChatRead> chatread = chatReadService.ifread(Integer.parseInt(sendid),Integer.parseInt(friendid));

                }

                List<ChatRead> chatread = chatReadService.ifread(Integer.parseInt(sendid),Integer.parseInt(friendid));
                if(chatread.size()>0){
                    UpdateWrapper updateWrapperChat = new UpdateWrapper();
                    updateWrapperChat.eq("chatter0",Integer.parseInt(sendid));
                    updateWrapperChat.eq("chatter1",Integer.parseInt(friendid));
                    updateWrapperChat.set("read",1);
                    chatReadMapper.update(null,updateWrapperChat);
                }else{
                    ChatRead chatin=new ChatRead();
                    chatin.setChatter0(Integer.parseInt(sendid));
                    chatin.setChatter1(Integer.parseInt(friendid));
                    chatin.setRead(1);
                    chatReadMapper.insert(chatin);
                }

                List<ChatRead> chatread2 = chatReadService.ifread(Integer.parseInt(friendid),Integer.parseInt(sendid));
                if(chatread2.size()>0){
                    UpdateWrapper updateWrapperChat = new UpdateWrapper();
                    updateWrapperChat.eq("chatter0",Integer.parseInt(friendid));
                    updateWrapperChat.eq("chatter1",Integer.parseInt(sendid));
                    updateWrapperChat.set("read",0);
                    chatReadMapper.update(null,updateWrapperChat);
                }else{
                    ChatRead chatin=new ChatRead();
                    chatin.setChatter0(Integer.parseInt(friendid));
                    chatin.setChatter1(Integer.parseInt(sendid));
                    chatin.setRead(0);
                    chatReadMapper.insert(chatin);
                }
                //incoming.writeAndFlush(new TextWebSocketFrame(messageid));
                Chat tmp = new Chat();
                tmp.chatter0 = Integer.parseInt(array[0]);
                tmp.chatter1 = Integer.parseInt(array[1]);
                tmp.content = array[2];
                tmp.record_time = new Date();
                chatMapper.insert(tmp);

            }

//      group chat
//            for (Channel channel : channels) {
//                channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress() + "send message"+sdf.format(new java.util.Date())));
//      }

        }
    }
    //??????
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
    }

    //??????: ????????????????????????????????????????????????????????????
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

//        for (Channel channel : channels) {
//            channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress() + "?????????????????????"+sdf.format(new java.util.Date())));
//        }
        //chatMapper.history();
        channels.add(ctx.channel());
    }

    //??????,??????
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //???????????????????????????????????????
        channels.remove(ctx.channel());
//        for (Channel channel : channels) {
//            channel.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress() + "??????????????????"));
//        }
    }


}

