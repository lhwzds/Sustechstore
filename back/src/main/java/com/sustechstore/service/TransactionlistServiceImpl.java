package com.sustechstore.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sustechstore.dao.Item;
import com.sustechstore.dao.Transactionlist;
import com.sustechstore.mapper.ItemMapper;
import com.sustechstore.mapper.TransactionlistMapper;
import com.sustechstore.service.Interface.IItemService;
import com.sustechstore.service.Interface.ITransactionlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionlistServiceImpl {
    @Autowired
    private TransactionlistMapper transactionMapper;
}
