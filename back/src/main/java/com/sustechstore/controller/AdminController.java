package com.sustechstore.controller;

import com.sustechstore.dao.Admin;
import com.sustechstore.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;
    @RequestMapping(value = "/admin")
    public ModelAndView adminPage(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin.html");
        return mv;
    }
    @PostMapping(value = "/adminLogin")
    public Object check(@RequestParam(name = "name")String name,
                        @RequestParam(name = "password")String password, HttpSession session){
        if(name.isEmpty()||password.isEmpty()){
            return loginError("无效用户名或者无效密码");
        }
        List<Admin> tmp = adminService.check(name,password);
        if(tmp.isEmpty()){
            return loginError("错误的用户名或密码");
        }
        session.setAttribute("name",tmp.get(0).name);
        session.setAttribute("role","admin");
        return "redirect:/home";
    }
    @RequestMapping(value = "/home")
    public ModelAndView home(HttpSession session){
        if(session==null||session.isNew()){
            return loginError("您还没有登录！");
        }
        String tmp = "Welcome! "+(String) session.getAttribute("name");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index.html");
        mv.addObject("text",tmp);
        return mv;
    }
    private ModelAndView loginError(String text){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin.html");
        mv.addObject("error",text);
        return mv;
    }
    @RequestMapping(value = "/chatroom")
    public ModelAndView chat(HttpSession session){

        ModelAndView mv = new ModelAndView();
        mv.setViewName("chatroom.html");
        return mv;
    }
    @RequestMapping(value = "/chat")
    public ModelAndView chat2(HttpSession session){

        ModelAndView mv = new ModelAndView();
        mv.setViewName("chat.html");
        return mv;
    }

}
