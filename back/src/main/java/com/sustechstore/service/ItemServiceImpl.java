package com.sustechstore.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sustechstore.dao.Item;
import com.sustechstore.mapper.ItemMapper;
import com.sustechstore.service.Interface.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {
    @Autowired
    private ItemMapper itemMapper;

    public List<Item> findAll(){
        return itemMapper.findAll();
    }
    public List<Item> findAllerrand(){
        return itemMapper.findAllerrand();
    }
    public List<Item> findAllneed(){
        return itemMapper.findAllneed();
    }

}
