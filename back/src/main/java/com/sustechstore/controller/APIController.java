package com.sustechstore.controller;

import com.sustechstore.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class APIController {
    @RequestMapping(value="/star")
    public ResponseEntity<Object> starAPI(@RequestParam(value="star",required = false)String star){
        if(!star.equals("baiyang") &&!star.equals("tianxie") &&!star.equals("jinniu") &&!star.equals("shuiping") &&
                !star.equals("shuangzi") &&!star.equals("juxie") &&!star.equals("sheshou") &&!star.equals("mojie") &&
                !star.equals("shizi") &&!star.equals("tiancheng") &&!star.equals("shuangyu"))
            return null;
            //baiyang jinniu shuangzi juxie shizi chunv tiancheng tianxie sheshou mojie shuiping shuangyu
        String host = "https://ali-star-lucky.showapi.com";
        String path = "/star";
        String method = "GET";
        String appcode = "c15b4621db124e15b010ddc72b88555a";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("needMonth", "0");
        querys.put("needTomorrow", "0");
        querys.put("needWeek", "0");
        querys.put("needYear", "0");
        querys.put("star", star);


        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            return new ResponseEntity<>(EntityUtils.toString(response.getEntity()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value="/weather")
    public ResponseEntity<Object> weatherAPI(){
        String host = "https://weather01.market.alicloudapi.com";
        String path = "/area-to-weather";
        String method = "GET";
        String appcode = "c15b4621db124e15b010ddc72b88555a";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("area", "深圳");
        querys.put("need3HourForcast", "0");
        querys.put("needAlarm", "1");
        querys.put("needHourData", "0");
        querys.put("needIndex", "0");
        querys.put("needMoreDay", "0");
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);

            //System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            return new ResponseEntity<>(EntityUtils.toString(response.getEntity()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
