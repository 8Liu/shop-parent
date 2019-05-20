package com.liudehuang.mq;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * @author liudehuang
 * @date 2019/3/19 10:30
 */
@Service
public class RpMessageMqProducer {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    public void sendMsg(Destination destination, String json) {
        jmsMessagingTemplate.convertAndSend(destination, json);
    }

    /**
     * 发送消息
     * @param queueName
     * @param json
     */
    public void sendMsg(String queueName,String json) {
        System.out.println("queueName:"+queueName+","+"json:"+json);
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(queueName);
        this.sendMsg(activeMQQueue, json);
    }
}
