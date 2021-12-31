package com.sustechstore.service;

import com.sustechstore.dao.Chat;
import com.sustechstore.dao.ChatRead;
import com.sustechstore.mapper.ChatReadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ChatReadServiceImpl{
    @Autowired
    private ChatReadMapper chatReadMapper;
    public List<ChatRead> ifread(int id1, int id2){
        return chatReadMapper.ifread(id1,id2);
    }
    public List<ChatRead> readlist(int id1){
        return chatReadMapper.readlist(id1);
    }
}
