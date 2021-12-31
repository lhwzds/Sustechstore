package com.sustechstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustechstore.dao.Admin;
import org.apache.ibatis.annotations.*;
import com.sustechstore.dao.User;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select(value = "select * from users where user_name = #{name} and password = #{password}")
    public List<User> check(String name, String password);

    @Select(value = "select * from users")
    List<User> findAll();

    @Select(value="select * from users where id = #{id}")
    User findByID(int id);

    @Insert(value = "insert into users (nick_name,user_name,password,email,telephone,qq,wechat,alipay) values (#{nick_name},#{user_name},#{password},#{email},#{telephone},#{qq},#{wechat},#{alipay})")
    void insert(String nick_name,
                String user_name,
                String password,
                String email,
                String telephone,
                String qq,
                String wechat,
                String alipay);

    @Delete (value ="delete from users where id = #{id}")
    void delete(int id);

    @Select (value ="select id from users where user_name = #{name} ")
    List<Integer> getIdFromName(String name);


//    @Update (value ="")
}
