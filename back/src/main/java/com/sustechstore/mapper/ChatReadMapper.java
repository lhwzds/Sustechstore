package com.sustechstore.mapper;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustechstore.dao.Chat;
import com.sustechstore.dao.ChatRead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface ChatReadMapper extends BaseMapper<ChatRead> {
    @Select("select * from chatread where (chatter0 = #{id1} and chatter1 = #{id2}) ;")
    public List<ChatRead> ifread(int id1,int id2);
    @Select("select * from chatread where (chatter0 = #{id1} ) ;")
    public List<ChatRead> readlist(int id1);

}
