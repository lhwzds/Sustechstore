package com.sustechstore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustechstore.dao.Chat;
import com.sustechstore.dao.ItemPicture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface ChatMapper extends BaseMapper<Chat> {


    @Select("select * from chathistory where (chatter0 = #{id1} and chatter1 = #{id2}) or (chatter1 = #{id1} and chatter0 = #{id2}) order by record_time asc ")
    public List<Chat> history(int id1,int id2);
}
