package com.liudehuang.email.service;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.adapter.MessageAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author liudehuang
 * @date 2018/12/17 19:16
 * 处理第三方发送邮件
 */
@Slf4j
@Service
public class EmailService implements MessageAdapter {
    @Value("${msg.subject}")
    private String subject;

    @Value("${msg.text}")
    private String text;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMsg(JSONObject body) {
        //处理发送邮件
        String email = body.getString("email");
        if(StringUtils.isEmpty(email)){
            return;
        }
        //调用发邮件接口
        log.info("消息服务平台发送邮件:{}开始",email);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //来自账号
        simpleMailMessage.setFrom(email);
        //发送账号
        simpleMailMessage.setTo(email);
        //标题
        simpleMailMessage.setSubject(subject);
        //邮件内容
        simpleMailMessage.setText(text.replace("{}",email));
        log.info("消息服务平台发送邮件:{}完成",email);
    }
}
