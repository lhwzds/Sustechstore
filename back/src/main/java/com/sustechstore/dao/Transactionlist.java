package com.sustechstore.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Transactionlist {
    @TableId(type= IdType.AUTO)
    public int id;
    public int consumerid;
    public int publisherid;
    public Date createtime;
    public Date endtime;
    public int publisherconfirm;
    public int consumerfirm;
    //consumer email
    public String email1;
    //publisher email
    public String email2;
    public int itemid;
    public int star;
}
