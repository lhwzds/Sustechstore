package com.sustechstore.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Date;
@Data
@TableName("users")
public class User{
    @TableId(value="id")
    public int id;
    public String nick_name;
    public String user_name;
    public String password;
    public String email;
    public String telephone;
    public String qq;
    public String wechat;
    public String alipay;
    public String gender;
    public Date birthday;
    public String avatar;
    public double money;
    public int credit;
}
