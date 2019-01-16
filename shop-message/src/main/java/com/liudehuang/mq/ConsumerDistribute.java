package com.liudehuang.mq;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.adapter.MessageAdapter;
import com.liudehuang.constants.Constants;
import com.liudehuang.email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author liudehuang
 * @date 2018/12/17 18:58
 * mq的接口规范-----通讯协议
 * {
 *     "header": {
 *         "interfaceType": "接口类型"
 *     },
 *     "content": {}
 * }
 *
 */
@Slf4j
@Component
public class ConsumerDistribute {
    @Autowired
    private EmailService emailService;

    private MessageAdapter messageAdapter;

    //监听消息
    @JmsListener(destination = "message_queue")
    public void distribute(String json){
        log.info("#######消息服务平台接收消息内容:{}######",json);
        if(StringUtils.isEmpty(json)){
            return;
        }
        JSONObject rootJSON = JSONObject.parseObject(json);
        JSONObject header = rootJSON.getJSONObject("header");
        String interfaceType = header.getString("interfaceType");
        if(StringUtils.isEmpty(interfaceType)){
            return;
        }
        JSONObject content = rootJSON.getJSONObject("content");
        //判断接口类型是否为发邮件
        if(interfaceType.equals(Constants.MSG_EMAIL)){
            //调用
            messageAdapter = emailService;
        }
        if(messageAdapter == null){
            return;
        }
        messageAdapter.sendMsg(content);
    }
}
