package com.sustechstore.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sustechstore.dao.Item;
import com.sustechstore.dao.ItemPicture;
import com.sustechstore.dao.User;
import com.sustechstore.mapper.ItemMapper;
import com.sustechstore.mapper.ItemPictureMapper;
import com.sustechstore.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Map;

@RestController
public class SearchController {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemPictureMapper itemPictureMapper;
    @Autowired
    private UserMapper userMapper;
    @PostMapping("/search")
    public String search(@RequestParam("searchText") String key){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like("name",key);
        wrapper.or();
        wrapper.like("description",key);
        List<Item> list = itemMapper.selectList(wrapper);
        JSONArray ans = new JSONArray();
        for(Item i:list){
            JSONObject tmp = new JSONObject();
            tmp.put("id",i.id);
            tmp.put("name",i.name);
            tmp.put("price",i.price);
            tmp.put("category",i.tag);
            tmp.put("description",i.description);
            User publisher = userMapper.selectById(i.publisherid);
            tmp.put("publisherid",i.publisherid);
            tmp.put("publisher",publisher.nick_name);
            tmp.put("publishertime",i.publishtime);
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.allEq(Map.of("itemid",i.id));
            List<ItemPicture> temp = itemPictureMapper.selectList(wrapper1);
            String[] filelist = new String[temp.size()];
            for(int j =0;j<filelist.length;j++){
                filelist[j] = temp.get(j).content;

            }
            tmp.put("fileList",filelist);
            tmp.put("picture","");
            ans.add(tmp);
        }
        return ans.toJSONString();
    }
    @RequestMapping("/searchtag")
    public String searchtag(@RequestParam("Tag") String tag){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like("tag",tag);
        List<Item> list = itemMapper.selectList(wrapper);
        JSONArray ans = new JSONArray();
        for(Item i:list){
            JSONObject tmp = new JSONObject();
            tmp.put("id",i.id);
            tmp.put("name",i.name);
            tmp.put("price",i.price);
            tmp.put("category",i.tag);
            tmp.put("description",i.description);
            User publisher = userMapper.selectById(i.publisherid);
            tmp.put("publisherid",i.publisherid);
            tmp.put("publisher",publisher.nick_name);
            tmp.put("publishertime",i.publishtime);
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.allEq(Map.of("itemid",i.id));
            List<ItemPicture> temp = itemPictureMapper.selectList(wrapper1);
            String[] filelist = new String[temp.size()];
            for(int j =0;j<filelist.length;j++){
                filelist[j] = temp.get(j).content;

            }
            tmp.put("fileList",filelist);
            tmp.put("picture","");
            ans.add(tmp);
        }
        return ans.toJSONString();
    }
}
