package com.sustechstore.controller;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
public class ChatServerApplication {
    @Autowired
    @Qualifier("bootstrap")
    private ServerBootstrap serverBootstrap;

    private Channel channel;

    public void start() throws Exception {
        System.out.println("netty启动");
        channel = serverBootstrap.bind("10.0.8.14", 8888).sync().channel().closeFuture().sync().channel();
    }
    @PreDestroy
    public void close(){
        channel.close();
        channel.parent().close();
    }
}
