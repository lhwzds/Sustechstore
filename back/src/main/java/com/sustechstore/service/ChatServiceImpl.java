package com.sustechstore.service;

import com.sustechstore.dao.Chat;
import com.sustechstore.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ChatServiceImpl{
    @Autowired
    private ChatMapper chatMapper;
    public List<Chat> history(int id1, int id2){
        return chatMapper.history(id1,id2);
    }
}
