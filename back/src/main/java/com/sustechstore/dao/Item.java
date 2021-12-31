package com.sustechstore.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.postgresql.jdbc.TimestampUtils;
import org.springframework.stereotype.Controller;
import java.sql.Timestamp;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Item {
    @TableId(type= IdType.AUTO)
    public int id;
    public String name;
    public String price;
    public String tag;
    public String description;
    public int publisherid;
    public Date publishtime;
    public int transactionid;
    public int isoverdate;
    public String type;
}
