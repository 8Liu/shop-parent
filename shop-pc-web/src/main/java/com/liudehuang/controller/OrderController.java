package com.liudehuang.controller;

import com.liudehuang.base.ResponseBase;
import com.liudehuang.entity.OrderEntity;
import com.liudehuang.feign.OrderServiceFeign;
import com.liudehuang.util.CreateIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * @author liudehuang
 * @date 2019/3/19 16:33
 */
@Slf4j
@Controller
public class OrderController {
    @Autowired
    private OrderServiceFeign orderServiceFeign;
    @ResponseBody
    @RequestMapping("/createOrder")
    public ResponseBase createOrder(){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderNumber(CreateIdUtils.getUUid());
        orderEntity.setIsPay(0);
        orderEntity.setUserId(CreateIdUtils.getUUid());
        orderEntity.setOrderDesc("东经一号");
        orderEntity.setPrice(new BigDecimal(1200));
        return orderServiceFeign.createOrder(orderEntity);
    }

}
