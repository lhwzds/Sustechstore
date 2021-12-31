package com.sustechstore.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sustechstore.dao.Item;
import com.sustechstore.dao.Trolley;
import com.sustechstore.dao.User;
import com.sustechstore.mapper.ItemMapper;
import com.sustechstore.mapper.UserMapper;
import com.sustechstore.service.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemUtils {

    public static String allItems(ItemServiceImpl itemService){
        List<Item> list = itemService.findAll();
        JSONArray ans = new JSONArray();
        for(Item i:list){
            JSONObject tmp = new JSONObject();
            tmp.put("id",i.id);
            tmp.put("name",i.name);
            tmp.put("price",i.price);
            tmp.put("category",i.tag);
            tmp.put("description",i.description);
            tmp.put("publisherid",i.publisherid);
            tmp.put("publishertime",i.publishtime);
            tmp.put("transactionid",i.transactionid);
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.allEq(Map.of("id",i.id));
            ans.add(tmp);
        }
        return ans.toJSONString();
    }
    public static String allItems(ItemMapper itemMapper, List<Integer> itemlist, UserMapper userMapper){
        JSONArray ans = new JSONArray();

        if (itemlist.size() == 0)
            return ans.toJSONString();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("id",itemlist);
        List<Item> list = itemMapper.selectList(wrapper);

//        System.out.println(list);
        for(Item i:list){
            JSONObject tmp = new JSONObject();
            tmp.put("id",i.id);
            tmp.put("name",i.name);
            tmp.put("price",i.price);
            tmp.put("category",i.tag);
            tmp.put("description",i.description);
            User publisher = userMapper.selectById(i.publisherid);
            tmp.put("publisher",publisher.nick_name);
            tmp.put("publishertime",i.publishtime);
            tmp.put("transactionid",i.transactionid);
            ans.add(tmp);
        }
        return ans.toJSONString();
    }
}
