package com.sustechstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustechstore.dao.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ItemMapper extends BaseMapper<Item> {
    @Select(value = "select * from item where isoverdate=0 and type='1'")
    public List<Item> findAll();
    @Select(value = "select * from item where isoverdate=0 and type='2'")
    public List<Item> findAllerrand();
    @Select(value = "select * from item where isoverdate=0 and type='3'")
    public List<Item> findAllneed();
}
