package com.sustechstore.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("chatread")
public class ChatRead {
    @TableId(type= IdType.AUTO)
    public int id;
    public int chatter0;
    public int chatter1;
    public int read;
}
