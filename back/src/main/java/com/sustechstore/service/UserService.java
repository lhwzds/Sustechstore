package com.sustechstore.service;

import com.sustechstore.dao.User;
import com.sustechstore.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserService{

    @Autowired
    private UserMapper userMapper;

    public List<User> check(String name,String password){return userMapper.check(name, password);}
    public List<User> findAll() {
        return userMapper.findAll();
    }

    public User findByID(int id){
        User ans = userMapper.findByID(id);
        ans.password = "";
//        ans.id = -1;
        return ans;
    }

    public void insert(String nick_name,
            String user_name,
            String password,
            String email,
            String telephone,
            String qq,
            String wechat,
            String alipay){
        userMapper.insert(nick_name,user_name,password,email,telephone,qq,wechat,alipay);
    }

    public void deleteId(int id){
        userMapper.delete(id);
    }

    public List<Integer> gerIdFromName(String name){
       return userMapper.getIdFromName(name);
    }
}
