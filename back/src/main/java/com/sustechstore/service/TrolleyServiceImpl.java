package com.sustechstore.service;

import com.sustechstore.mapper.TrolleyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrolleyServiceImpl {
    @Autowired
    private TrolleyMapper trolleyMapper;
}
