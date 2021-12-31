package com.sustechstore.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sustechstore.dao.AvatarPicture;
import com.sustechstore.dao.Item;
import com.sustechstore.dao.ItemPicture;
import com.sustechstore.dao.User;
import com.sustechstore.mapper.ItemMapper;
import com.sustechstore.mapper.ItemPictureMapper;
import com.sustechstore.mapper.TransactionlistMapper;
import com.sustechstore.mapper.UserMapper;
import com.sustechstore.service.ItemServiceImpl;
import com.sustechstore.utils.FileTransferUtils;
import com.sustechstore.utils.TokenUtils;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Wrapper;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
public class ItemController {
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemPictureMapper itemPictureMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TransactionlistMapper transactionMapper;

    @RequestMapping(value = "/items/{param}")
    public ResponseEntity<Object> main(@PathVariable("param") String method){
        String ans = "";
        if(method.equals("all")){
            ans = allItems();
        }
        else if(method.equals("allerrand")){
            ans = allerrand();
        }
        else if(method.equals("allneed")){
            ans = allneed();
        }
        else{
            Item item = itemService.getById(Integer.parseInt(method));
            ObjectMapper mapper = new ObjectMapper();
            try{
                String res = mapper.writeValueAsString(item);
                return new ResponseEntity<>(res,HttpStatus.OK);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(ans, HttpStatus.OK);
    }
    public String allItems(){
        List<Item> list = itemService.findAll();
        JSONArray ans = new JSONArray();
        findItemByList(list, ans);
        return ans.toJSONString();
    }
    public String allerrand(){
        List<Item> list = itemService.findAllerrand();
        JSONArray ans = new JSONArray();
        findItemByList(list, ans);
        return ans.toJSONString();
    }
    public String allneed(){
        List<Item> list = itemService.findAllneed();
        JSONArray ans = new JSONArray();
        findItemByList(list, ans);
        return ans.toJSONString();
    }
//圖片，名稱，描述，賣家，已收貨
    @PostMapping("/items/detail")
    public String detail(@RequestParam("itemID")String id1) throws IOException {
        int id = Integer.parseInt(id1);
        String ans = "";
        Item i = itemMapper.selectById(id);
        JSONObject tmp = new JSONObject();
        tmp.put("id",i.id);
        tmp.put("name",i.name);
        tmp.put("price",i.price);
        if(i.tag != null){
            String [] category=i.tag.split(",");
            tmp.put("category",category);
        }
        else {
            String [] category=" ".split(",");
            tmp.put("category",category);
        }
        tmp.put("description",i.description);
        tmp.put("publisherid",i.publisherid);
        tmp.put("publishertime",i.publishtime);
        tmp.put("transactionid",i.transactionid);
        tmp.put("type",i.type);
        User publisher = userMapper.selectById(i.publisherid);
        tmp.put("publisher",publisher.nick_name);
        tmp.put("email",publisher.email);
        tmp.put("telephone",publisher.telephone);
        tmp.put("gender",publisher.gender);
        tmp.put("qq",publisher.qq);
        tmp.put("wechat",publisher.wechat);
        tmp.put("avatar",publisher.avatar);
        tmp.put("isoverdate",i.isoverdate);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.allEq(Map.of("itemid",i.id));
        List<ItemPicture> temp = itemPictureMapper.selectList(wrapper);
        String[] filelist = new String[temp.size()];
        for(int j =0;j<filelist.length;j++){
            filelist[j] = temp.get(j).content;
        }
        tmp.put("file",filelist);
        return tmp.toJSONString();
    }

    @PostMapping(value="/items/query")
    public ResponseEntity<String> findByText(@RequestHeader HttpHeaders req){
        int userid = TokenUtils.getUserid(req);
        if(userid==-1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.allEq(Map.of("publisherid",userid));
        List<Item> list = itemMapper.selectList(wrapper);
        JSONArray ans = new JSONArray();
        findItemByList(list,ans);
        return new ResponseEntity<String>(ans.toJSONString(),HttpStatus.OK);
    }
    private void findItemByList(List<Item> itemList, JSONArray ans) {
        for(Item i:itemList){
            JSONObject tmp = new JSONObject();
            tmp.put("id",i.id);
            tmp.put("name",i.name);
            tmp.put("price",i.price);
            tmp.put("category",i.tag);
            tmp.put("description",i.description);
            tmp.put("type",i.type);
            User publisher = userMapper.selectById(i.publisherid);
            tmp.put("publisher",publisher.nick_name);
            tmp.put("publishertime",i.publishtime);
            tmp.put("transactionid",i.transactionid);
            tmp.put("isoverdate",i.isoverdate);
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.allEq(Map.of("itemid",i.id));
            List<ItemPicture> temp = itemPictureMapper.selectList(wrapper);
            String[] filelist = new String[temp.size()];
            for(int j =0;j<filelist.length;j++){
                filelist[j] = temp.get(j).content;

            }
            tmp.put("fileList",filelist);
            tmp.put("picture","");
            ans.add(tmp);
        }
    }

    @PostMapping(value = "/items/add")
    public ResponseEntity<String> upload(@RequestParam(value="picture",required = false) MultipartFile[] multipartFile,
                                         @RequestParam("merchandiseName")String itemName,
                                         @RequestParam("merchandisePrice")String itemPrice,
                                         @RequestParam("merchandiseText")String itemText,
                                         @RequestParam(value = "merchandiseType")String type,
                                         @RequestHeader HttpHeaders req) throws IOException {
        int userid = TokenUtils.getUserid(req);
        if(userid==-1){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.println("receive");
        if(multipartFile!=null)
            System.out.println(multipartFile[0].getOriginalFilename());
        boolean ans = true;
        Item item = new Item();
        item.name = itemName;
        item.publisherid = userid;
        item.price = itemPrice;
        item.description = itemText;
        item.publishtime = new Date();
        item.type = type;
        try{
            itemMapper.insert(item);
            QueryWrapper<Item> tmp = new QueryWrapper<>();
            tmp.allEq(Map.of("name",item.name,"price",item.price));
            item.id = itemMapper.selectList(tmp).get(0).id;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (multipartFile != null) {
            for (MultipartFile f:multipartFile) {
                String fileName = "";
                try {
                    fileName = FileTransferUtils.upload(f);
                    itemPictureMapper.insert(new ItemPicture(item.id, fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }
    @RequestMapping(value="/items/picture/{name}")
    public void getFeedBackPicture(HttpServletResponse response,@PathVariable("name") String name) throws Exception{
        System.out.println("====picture====");
        String realPath="src/main/resources/static/"+name;
        FileInputStream inputStream = new FileInputStream(realPath);
        int i = inputStream.available();
        byte[] buff = new byte[i];
        inputStream.read(buff);
        inputStream.close();
        response.setContentType("image/*");
        Base64Encoder encoder = new Base64Encoder();
        OutputStream out = response.getOutputStream();
        String pre = "data:image/png;base64,";
        out.write(pre.getBytes());
        encoder.encode(buff,0,buff.length,out);
        //out.write(buff);
        out.close();
    }
    @RequestMapping("/items/confirm")
    public ResponseEntity<String> confirm(@RequestParam("rateValue") int star,
                                          @RequestParam("id") int transactionid){

        UpdateWrapper updateWrapperTran = new UpdateWrapper();
        updateWrapperTran.eq("id",transactionid);
        updateWrapperTran.set("star",star);
        updateWrapperTran.set("consumerfirm",1);

        QueryWrapper queryWrapperTran = new QueryWrapper();
        queryWrapperTran.eq("id",transactionid);
        int publisherid = transactionMapper.selectOne(queryWrapperTran).publisherid;

        QueryWrapper queryWrapperUser = new QueryWrapper();
        queryWrapperUser.eq("id",publisherid);
        int currentcredit = userMapper.selectOne(queryWrapperUser).credit + star;


        UpdateWrapper updateWrapperUser = new UpdateWrapper();
        updateWrapperUser.eq("id",publisherid);
        updateWrapperUser.set("credit",currentcredit);

        transactionMapper.update(null,updateWrapperTran);
        userMapper.update(null,updateWrapperUser);
        System.out.println(currentcredit);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
