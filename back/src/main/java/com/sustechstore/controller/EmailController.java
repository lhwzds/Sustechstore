package com.sustechstore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

//@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailController {
    @Autowired
    JavaMailSender javaMailSender;

//    @Value("${spring.mail.username}")
    private String account;
    {
        try {
            account = new String(("SustechStoreTeam" + " <" + "1771330667@qq.com" + ">")
                    .getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @PostMapping(value = "/sendmail")
    public String sendSimpleMail(
            @RequestParam("address") String address,
            @RequestParam("subject") String subject,
            @RequestParam("body") String body) {
        this.sendEmail(javaMailSender,address,subject,body);
        return "发送成功";
    }
    public void sendEmail(JavaMailSender javaMailSender,String address, String subject, String body){
        try {
            SimpleMailMessage smm = new SimpleMailMessage();
            smm.setFrom(account);
            smm.setTo(address);
            smm.setSubject(subject);
            smm.setText(body);
            javaMailSender.send(smm);
        }catch(Exception e) {
            e.printStackTrace();
        }


    }
    @PostMapping(value = "/email/code")
    public String sendCode(
            @RequestParam(value = "email",required = false) String address,
            @RequestParam(value = "random",required = false) String code) {
        //System.out.println(b);
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom(account);
        smm.setTo(address);
        String subject = "【SustechStore】验证码";
        smm.setSubject(subject);
        smm.setText("您的验证码是："+code);
        javaMailSender.send(smm);
        return "Success";
    }

    @PostMapping("sendmail2")
    public String sendAttachmentMail(
            @RequestParam("address") String address,
            @RequestParam("subject") String subject,
            @RequestParam("body") String body,
            @RequestPart("attachment") MultipartFile attach) throws MessagingException, IOException {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
        mimeMessageHelper.setFrom(account);
        mimeMessageHelper.setTo(new String[]{address});
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body);
        //文件路径
        byte[] bytes = attach.getBytes();
        String name = attach.getOriginalFilename();
        mimeMessageHelper.addAttachment(name, new ByteArrayResource(bytes));
//        log.info("fileName:{}", name);
        javaMailSender.send(mimeMailMessage);
        return "发送成功";
    }
}
