package com.sustechstore.mapper;

import com.sustechstore.dao.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {
    @Select(value = "select * from admins where admin_name = #{name} and password = #{password}")
    public List<Admin> check(String name, String password);
}
