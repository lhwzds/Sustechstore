package com.sustechstore.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sustechstore.dao.*;
import com.sustechstore.mapper.*;
import com.sustechstore.service.ChatReadServiceImpl;
import com.sustechstore.service.ChatServiceImpl;
import com.sustechstore.service.ItemServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

@Configuration
@RestController
public class ChatController {
    @Autowired
    private ChatReadServiceImpl chatReadService;
    @Autowired
    private ChatServiceImpl chatService;
    @Autowired
    private ChatReadMapper chatReadMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    ChatHandler chatHandler;

    //WebSocketHandler webSocketHandler;
    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup(){
        return new NioEventLoopGroup();
    }
    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup(){
        return new NioEventLoopGroup();
    }

    @Bean(name = "bootstrap")
    public ServerBootstrap bootstrap(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup(),workerGroup())
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress("localhost", 8888))
                .childHandler(new ChannelInitializer<SocketChannel>() {


                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new HttpServerCodec());
                        socketChannel.pipeline().addLast(new ChunkedWriteHandler());

                        socketChannel.pipeline().addLast(new HttpObjectAggregator(65536));
                        socketChannel.pipeline().addLast(chatHandler);
                    }
                })
        .option(ChannelOption.SO_BACKLOG,100)
        .childOption(ChannelOption.SO_KEEPALIVE,true);
        return serverBootstrap;
    }

    @RequestMapping(value ="/chathistory")
    public String getHistory(@RequestParam("mineId")String mineId,@RequestParam("otherId")String otherId) throws IOException {
        List<Chat> history = chatService.history(Integer.parseInt(mineId),Integer.parseInt(otherId));
        JSONArray chatlist = new JSONArray();
        for (int i = 0; i < history.size() ; i++) {
            Chat now= history.get(i);
            JSONObject chatitem =new JSONObject();
            chatitem.put("id",now.id);
            chatitem.put("time",now.record_time.toString());
            chatitem.put("content",now.content);
            if(now.chatter0==Integer.parseInt(mineId)){
                chatitem.put("type",1);
            }else{
                chatitem.put("type",2);
            }
            chatlist.add(i,chatitem);
        }
        return chatlist.toJSONString();
    }

    @RequestMapping("/chatnew")
    public String getNewRecord(@RequestParam("mineId")String mineId,@RequestParam("otherId")String yourId){
        QueryWrapper wrapper = new QueryWrapper();
        JSONObject chatitem =new JSONObject();
        wrapper.eq("chatter0",Integer.parseInt(yourId));
        wrapper.eq("chatter1",Integer.parseInt(mineId));
        wrapper.orderByDesc("record_time");
        List<Chat> chathis = chatMapper.selectList(wrapper);
        if(chathis.size()>0){
            chatitem.put("content",chathis.get(0).content);
        };
        return chatitem.toJSONString();
    }

    @RequestMapping(value ="/chatread")
    public String getChatReadList(@RequestParam("mineId")String mineId) throws IOException {
        List<ChatRead> readlist = chatReadService.readlist(Integer.parseInt(mineId));
        JSONArray chatlist = new JSONArray();
        if(readlist.size()==0){
            return chatlist.toJSONString();
        }else{
            for (int i = 0; i < readlist.size() ; i++) {
                ChatRead chatreaditem=readlist.get(i);
                int read=chatreaditem.getRead();
                if(read==1){
                    continue;
                }else{
                    JSONObject noreadid =new JSONObject();
                    noreadid.put("id",chatreaditem.chatter1);
                    User sender = userMapper.selectById(chatreaditem.chatter1);
                    noreadid.put("avatar",sender.avatar);
                    User user=userMapper.findByID(chatreaditem.chatter1);
                    System.out.println(user.nick_name);
                    noreadid.put("name",user.nick_name);
                    noreadid.put("content","");
                    chatlist.add(noreadid);

                }
            }
        }
        return chatlist.toJSONString();
    }

    @RequestMapping(value ="/updateunread")
    public void updateunread(@RequestParam("mineId")String mineId,@RequestParam("otherId")String otherId) throws IOException {
        List<ChatRead> chatread = chatReadService.ifread(Integer.parseInt(mineId),Integer.parseInt(otherId));
        if(chatread.size()==0){
            ChatRead chatreaditem=new ChatRead();
            chatreaditem.chatter0=Integer.parseInt(mineId);
            chatreaditem.chatter1=Integer.parseInt(otherId);
            chatreaditem.read=0;
            chatReadMapper.insert(chatreaditem);
        }else{
            ChatRead chatreaditem=new ChatRead();
            UpdateWrapper<ChatRead> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("chatter0",Integer.parseInt(mineId));
            updateWrapper.eq("chatter1",Integer.parseInt(otherId));
            chatreaditem.setChatter0(Integer.parseInt(mineId));
            chatreaditem.setChatter1(Integer.parseInt(otherId));
            chatreaditem.setRead(0);
            chatReadMapper.update(chatreaditem,updateWrapper);
        }
    }

    @RequestMapping(value ="/updateread")
    public void updateread(@RequestParam("mineId")String mineId,@RequestParam("otherId")String otherId) throws IOException {
        List<ChatRead> chatread = chatReadService.ifread(Integer.parseInt(mineId),Integer.parseInt(otherId));
        if(chatread.size()==0){
            ChatRead chatreaditem=new ChatRead();
            chatreaditem.chatter0=Integer.parseInt(mineId);
            chatreaditem.chatter1=Integer.parseInt(otherId);
            chatreaditem.read=1;
            chatReadMapper.insert(chatreaditem);
        }else{
            ChatRead chatreaditem=new ChatRead();
            UpdateWrapper<ChatRead> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("chatter0",Integer.parseInt(mineId));
            updateWrapper.eq("chatter1",Integer.parseInt(otherId));
            chatreaditem.setChatter0(Integer.parseInt(mineId));
            chatreaditem.setChatter1(Integer.parseInt(otherId));
            chatreaditem.setRead(1);
            chatReadMapper.update(chatreaditem,updateWrapper);
        }
    }

}
