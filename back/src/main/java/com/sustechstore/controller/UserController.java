package com.sustechstore.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sustechstore.dao.AvatarPicture;
import com.sustechstore.dao.Item;
import com.sustechstore.dao.ItemPicture;
import com.sustechstore.dao.User;
import com.sustechstore.mapper.UserMapper;
import com.sustechstore.service.UserService;
import com.sustechstore.utils.FileTransferUtils;
import com.sustechstore.utils.TokenUtils;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.sustechstore.mapper.AvartarPictureMapper;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper usermapper;
    @Autowired
    private AvartarPictureMapper avartarPictureMapper;
    static class LoginData {
        public String username;
        public String pass;
    }
    @RequestMapping("/charge")
    public ResponseEntity<String> charge(@RequestParam("ratio") String ratioo,
                                         @RequestHeader HttpHeaders req){
        int ratio = Integer.parseInt(ratioo);
        int id = TokenUtils.getUserid(req);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",id);
        double m = usermapper.selectOne(queryWrapper).money;
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("id",id);
        wrapper.set("money",m+ratio);
        usermapper.update(null,wrapper);
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }
    @PostMapping(value = "/login")
    public ResponseEntity<String> check(@RequestBody String body) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        LoginData req = mapper.readValue(body, LoginData.class);
        LOGGER.info(String.format("Receive login request: %s %s",req.username,req.pass));
        List<User> list = userService.check(req.username,req.pass);
        if(list.size()>0){
            JSONObject ans = new JSONObject();
            ans.put("token",TokenUtils.sign(list.get(0).id));
            return new ResponseEntity<String>(ans.toJSONString(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Wrong", HttpStatus.NOT_FOUND);
    }

    static class RegisterData{
        public String nick_name;
        public String user_name;
        public String password;
        public String email;
        public String telephone;
        public String qq;
        public String wechat;
        public String alipay;
    }
    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody String body) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        RegisterData req = mapper.readValue(body, RegisterData.class);
        List<Integer> tmp = userService.gerIdFromName(req.user_name);
        if(tmp.size()>0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.insert(req.nick_name,req.user_name, req.password,req.email,req.telephone,req.qq,req.wechat,req.alipay);
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }

    @RequestMapping("/user/id")
    public ResponseEntity<String> userdetail(@RequestParam("id")String id){
        int userid = Integer.parseInt(id);
        JSONObject ans = new JSONObject();
        User user = userService.findByID(userid);
        JSONObject obj = new JSONObject();
        Field[] tmp = user.getClass().getFields();

        try {
            for (Field f : tmp) {
                if (f.getName().equals("birthday")) {
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                    String str = s.format(user.birthday);
                    obj.put(f.getName(), str);
                    continue;
                }
                obj.put(f.getName(), f.get(user));
            }
            System.out.println(obj.toJSONString());

            return new ResponseEntity<String>(obj.toJSONString(), HttpStatus.OK);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value="/user/{param}")
    public ResponseEntity<String> detail(@PathVariable("param") String method,@RequestHeader HttpHeaders req) {
        List<String> tokenList = req.get("token");
        if(tokenList == null||tokenList.size()==0){
            //Error: request with no token
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String token = tokenList.get(0);
        System.out.println(token);
        if(!TokenUtils.verify(token)){
            //Error: invalid token
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        int userid = TokenUtils.getID(token);
        JSONObject ans = new JSONObject();
        if(method.equals("basic")){
            User user = userService.findByID(userid);
            JSONObject obj = new JSONObject();
            Field[] tmp = user.getClass().getFields();

            try{
                for(Field f:tmp){
                    if(f.getName().equals("birthday")){
                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                        String str = s.format(user.birthday);
                        obj.put(f.getName(),str);
                        continue;
                    }
                    obj.put(f.getName(),f.get(user));
                }
                System.out.println(obj.toJSONString());

                return new ResponseEntity<String>(obj.toJSONString(),HttpStatus.OK);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<String>(ans.toJSONString(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping(value="/user/update")
    public ResponseEntity<String>update(@RequestParam(value = "nick_name",required = false)String nick_name,
                                        @RequestParam(value = "sex",required = false)String gender,
                                        @RequestParam(value = "birthday",required = false) String birthday,
                                        @RequestParam(value = "qq",required = false) String qq,
                                        @RequestParam(value = "avatar",required = false) MultipartFile multipartFile,
                                        @RequestHeader HttpHeaders req
                                        ) throws ParseException {

        List<String> tokenList = req.get("token");
        if(tokenList == null||tokenList.size()==0){
            //Error: request with no token
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String token = tokenList.get(0);
        System.out.println(token);
        User u = new User();
        u.nick_name = nick_name;
        u.gender = gender;
        u.qq=qq;
        SimpleDateFormat s = new SimpleDateFormat("yy-MM-dd");
        Date tmp = s.parse(birthday);
        u.birthday = tmp;
        u.id = TokenUtils.getID(token);
        System.out.println(u.id);
        String fileName = "";
        if(multipartFile!=null&&!multipartFile.isEmpty()){
            try {
                fileName = FileTransferUtils.upload(multipartFile);
                u.avatar = fileName;
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        int num = usermapper.updateById(u);
        System.out.println(num);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/user/password")
    public void updatePassword(@RequestParam("email")String email,
                               @RequestParam("password")String password){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.allEq(Map.of("email",email));
        List<User> temp = usermapper.selectList(wrapper);
        User o = temp.get(0);
        User u = new User();
        u.password = password;
        u.id = o.id;
        usermapper.updateById(u);
    }
    @RequestMapping(value="/user/icon")
    public void getFeedBackPicture(HttpServletResponse response,@RequestHeader HttpHeaders req) throws Exception{
        int userid = TokenUtils.getUserid(req);
        if(userid==-1){ return;}
        User avatar = usermapper.selectById(userid);
        System.out.println("====icon====\n"+avatar.avatar);
        String realPath="src/main/resources/static/"+avatar.avatar;
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
}
