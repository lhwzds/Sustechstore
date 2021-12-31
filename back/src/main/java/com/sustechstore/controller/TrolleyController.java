package com.sustechstore.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sustechstore.dao.*;
import com.sustechstore.mapper.*;
import com.sustechstore.service.ItemServiceImpl;
import com.sustechstore.utils.TokenUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.sustechstore.utils.ItemUtils.allItems;

@RestController
public class TrolleyController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TrolleyMapper trolleyMapper;
    @Autowired
    private ItemServiceImpl itemServiceImpl;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    private TransactionlistMapper transactionlistMapper;
    @Autowired
    private ItemPictureMapper itemPictureMapper;
    @RequestMapping(value="/trolley")
    public ResponseEntity<Object> listTrolley(@RequestHeader HttpHeaders req){
        int userid = TokenUtils.getUserid(req);
        if(userid==-1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String ans = "";
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("userid",userid);
        wrapper.select("itemid");
        List<Trolley> itemlist2 = trolleyMapper.selectList(wrapper);
        List<Integer> itemlist = new ArrayList<Integer>();
        for (Trolley o:itemlist2)
            itemlist.add(o.itemid);
        ans = allItems(itemMapper,itemlist,userMapper);
        return new ResponseEntity<>(ans, HttpStatus.OK);
    }
    @RequestMapping(value="/trolley/add")
    public ResponseEntity<String> addTrolley(@RequestParam("itemId")int itemid,
                                             @RequestHeader HttpHeaders req){
        int userid = TokenUtils.getUserid(req);
        if(userid==-1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            Trolley tro = new Trolley();
            tro.userid = userid;
            tro.itemid = itemid;
            trolleyMapper.insert(tro);
        }catch(Exception e){
            e.printStackTrace();
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);


    }
    @RequestMapping(value="trolley/remove")
    public ResponseEntity<String> removeTrolley(@RequestParam("itemId")List<String> list,
                                                @RequestHeader HttpHeaders req){
        int userid = TokenUtils.getUserid(req);
        if(userid==-1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        int remove_num = -1;
        for(String s:list){
            int itemid = Integer.parseInt(s);
            try{
                Map<String, Object> map = new HashMap<>();
                map.put("userid",userid);
                map.put("itemid",itemid);
                remove_num = trolleyMapper.deleteByMap(map);

            }catch(Exception e){
                e.printStackTrace();
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(String.valueOf(remove_num),HttpStatus.OK);
    }

    @RequestMapping(value="getmoney")
    public ResponseEntity<String>getMoney(@RequestHeader HttpHeaders req){
        int userid = TokenUtils.getUserid(req);
        User user = userMapper.findByID(userid);

        return new ResponseEntity<>(String.valueOf(user.money), HttpStatus.OK);
    }
    @RequestMapping(value="getcredit")
    public ResponseEntity<String>getCredit(@RequestHeader HttpHeaders req){
        int userid = TokenUtils.getUserid(req);
        User user = userMapper.findByID(userid);

        return new ResponseEntity<>(String.valueOf(user.credit), HttpStatus.OK);
    }
    @RequestMapping(value="buyhistory")
    public ResponseEntity<String> transactionHistory(@RequestHeader HttpHeaders req){
        int userid = TokenUtils.getUserid(req);
        QueryWrapper wrapper1 = new QueryWrapper();
        wrapper1.eq("consumerid",userid);
        List<Transactionlist> list = transactionlistMapper.selectList(wrapper1);
        JSONArray ans = new JSONArray();
        for(Transactionlist i : list){
            JSONObject tmp = new JSONObject();
            tmp.put("id",i.id);
            tmp.put("itemid",i.itemid);
            tmp.put("consumerid",i.consumerid);
            tmp.put("publisherid",i.publisherid);
            tmp.put("createtime",i.createtime);
            tmp.put("endtime",i.endtime);
            tmp.put("publisherconfirm",i.publisherconfirm);
            tmp.put("consumerfirm",i.consumerfirm);
            User publisher = userMapper.selectById(i.publisherid);
            tmp.put("publisherid",i.publisherid);
            tmp.put("publisher",publisher.nick_name);
            QueryWrapper wrapper2 = new QueryWrapper();
            QueryWrapper wrapper3 = new QueryWrapper();
            wrapper2.allEq(Map.of("itemid",i.itemid));
            wrapper3.allEq(Map.of("id",i.itemid));
            List<ItemPicture> temp = itemPictureMapper.selectList(wrapper2);
            Item item = itemMapper.selectOne(wrapper3);
            if(item==null)
                return new ResponseEntity<>("请用正常流程添加删除商品",HttpStatus.OK);;
            tmp.put("itemprice",item.price);
            tmp.put("itemname",item.name);
            String[] filelist = new String[temp.size()];
            for(int j =0;j<filelist.length;j++){
                filelist[j] = temp.get(j).content;

            }
            tmp.put("fileList",filelist);
            tmp.put("picture","");
            ans.add(tmp);
        }

        return new ResponseEntity<>(String.valueOf(ans),HttpStatus.OK);
    }
    @PostMapping(value="trolley/buy")
    public ResponseEntity<String> buyItem(@RequestParam(value = "itemID")List<String> list,
                                          @RequestParam("location")String location,
                                          @RequestHeader HttpHeaders req){
        double sum_money = -1;
        double your_money = -1;

        int userid = TokenUtils.getUserid(req);
        List<Integer> items= new ArrayList<Integer>();
        for (String s:list) {
            items.add(Integer.valueOf(s));
        }
        if (items.size() == 0)//购物列表为空
            return new ResponseEntity<>("0",HttpStatus.BAD_REQUEST);
        //wrapper1从商品表中找出要购买的商品
        QueryWrapper wrapper1 = new QueryWrapper();
        wrapper1.in("id",items);
        List<Item> Buylist = itemMapper.selectList(wrapper1);

        UpdateWrapper wrapper6 = new UpdateWrapper();
        wrapper6.set("isoverdate",1);
        wrapper6.in("id",items);

        //wrapper2找用户，得到该用户的钱和 信誉积分（未实现）
        QueryWrapper wrapper2 = new QueryWrapper();
        wrapper2.eq("id",userid);
        //wrapper3从商品表中把已购买的商品设为过期
        UpdateWrapper wrapper3 = new UpdateWrapper();
        wrapper3.in("id",items);
        wrapper3.set("isoverdate",1);
        User consumer = userMapper.selectOne(wrapper2);

        your_money = consumer.money;
        for (Item i: Buylist) {
            sum_money+=Double.parseDouble(i.price);
        }
        double currentmoney = your_money-sum_money;;

        if(currentmoney<0){
            return new ResponseEntity<>("0",HttpStatus.OK);
        }
        else {
            UpdateWrapper wrapper4 = new UpdateWrapper();
            wrapper4.in("id", userid);
            wrapper4.set("money", currentmoney);
            userMapper.update(null,wrapper4);
        }
        itemMapper.update(null,wrapper6);
        trolleyMapper.update(null,wrapper3);

        //邮件提醒
        EmailController emailController= new EmailController();
        String Subject = "交易通知";
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");

        StringBuilder items_name = new StringBuilder("");


        for (Item i : Buylist) {
            String content2 = "";
            int publisherid = i.publisherid;
            User publisher = userMapper.selectById(publisherid);
            //更新发布者余额
            UpdateWrapper wrapper5 = new UpdateWrapper();
            wrapper5.in("id", publisherid);
            double publishermoney = publisher.money + Double.parseDouble(i.price);
            wrapper5.set("money",publishermoney);
            userMapper.update(null, wrapper5);
            //购买者购买的商品
            items_name.append(" \"").append(i.name).append("\" ");
            content2 += "亲，您于" + i.publishtime + "发布的商品 \"" + i.name + "\" 已被" +
                    consumer.nick_name + "于" + dateFormat.format(calendar.getTime()) +
                    "下单, 此次交易获得" + i.price + "元, " + "余额为 " + publishermoney + "元，约定交易地点为 "+location+
            "，请与商家联系妥洽，交易引起的纠纷本平台概不负责！";
            //publisher email
            System.out.println(content2);
            emailController.sendEmail(javaMailSender,publisher.email, Subject, content2);

            //记录交易记录
            Transactionlist record = new Transactionlist();
            record.consumerid = userid;
            record.publisherid = publisherid;
            record.createtime = new Date();
            record.email2 = content2;
            record.itemid = i.id;
            transactionlistMapper.insert(record);
        }
        //consumer email
        String content1 = "您于"+dateFormat.format(calendar.getTime())+"下单的"
                +items_name+"交易成功！"+"此次消费 "+sum_money+" 元,"+"余额 "+ currentmoney+" 元，约定交易地点为"+location
                +"，请与商家联系妥洽，交易引起的纠纷本平台概不负责！";
        System.out.println(content1);

        emailController.sendEmail(javaMailSender,consumer.email,Subject,content1);



        return new ResponseEntity<>("1",HttpStatus.OK);
    }

}
