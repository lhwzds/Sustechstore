package com.sustechstore;

import com.sustechstore.controller.ChatServerApplication;
import com.sustechstore.utils.TokenUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.sustechstore.mapper")
public class SustechStoreApplication {
    public static void main(String[] args){
       ConfigurableApplicationContext context = SpringApplication.run(SustechStoreApplication.class, args);
        //System.out.println(TokenUtils.sign(1));
        ChatServerApplication chatServerApplication = context.getBean(ChatServerApplication.class);
        try {
            chatServerApplication.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
