package com.sustechstore.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sustechstore.dao.Item;
import com.sustechstore.dao.User;
import com.sustechstore.service.ItemServiceImpl;
import com.sustechstore.service.UserService;
import com.sustechstore.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class WebController {
    @Autowired
    private UserService userService;
    private AdminController adminController;

    @RequestMapping(value = "/",method = GET)
    public ModelAndView helloPage(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index.html");
        return mv;
    }

    @RequestMapping(value = "/findall",method = GET)
    public List<User> loginPage(){
        return userService.findAll();
    }

    @RequestMapping(value = "/deleteid")
    public String deleteIdPage(@RequestParam(name="id")int id){
        userService.deleteId(id);
        return id+" delete success!";
    }

    @RequestMapping(value = "/test")
    public ResponseEntity<Object> verify(@RequestHeader HttpHeaders req){
        System.out.println(req.get("token"));
        if(TokenUtils.verify(req.get("token").get(0))){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
