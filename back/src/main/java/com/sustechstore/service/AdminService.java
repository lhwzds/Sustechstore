package com.sustechstore.service;

import com.sustechstore.dao.Admin;
import com.sustechstore.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminService")
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;


    public List<Admin> check(String name, String password){
        return adminMapper.check(name, password);
    }
}
