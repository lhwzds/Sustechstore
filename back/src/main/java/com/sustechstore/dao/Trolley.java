package com.sustechstore.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("trolley")
public class Trolley {
    @TableId(type= IdType.AUTO)
    public int id;
    public int isoverdate;
    public int userid;
    public int itemid;
}
