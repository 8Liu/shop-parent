package com.liudehuang.mq;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.api.service.OrderService;
import com.liudehuang.base.ResponseBase;
import com.liudehuang.dao.OrderDao;
import com.liudehuang.entity.OrderEntity;
import com.liudehuang.entity.RpMessage;
import com.liudehuang.feign.RpMessageServiceFeign;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liudehuang
 * @date 2019/3/19 14:17
 */
@Component
public class RpMessageMqCusumer {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RpMessageServiceFeign rpMessageServiceFeign;

    @Transactional
    @JmsListener(destination = "ORDER_NOTIFY_QUEUE")
    public void distribute(String json){

        System.out.println("json:"+json);
        OrderEntity order = JSONObject.parseObject(json,OrderEntity.class);
        int row = orderDao.updateOrder(order);
        ResponseBase rpMessageResult = rpMessageServiceFeign.deleteMessageByMessageId(order.getMessageId());
    }
}
