package com.liudehuang.api.service.impl;

import com.liudehuang.api.service.OrderService;
import com.liudehuang.base.BaseApiService;
import com.liudehuang.base.ResponseBase;
import com.liudehuang.dao.OrderDao;
import com.liudehuang.entity.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liudehuang
 * @date 2019/1/16 17:05
 */
@Slf4j
@RestController
public class OrderServiceImpl extends BaseApiService implements OrderService {
    @Autowired
    private OrderDao orderDao;



    @Override
    public ResponseBase updateOrder(@RequestBody OrderEntity orderEntity) {
        int row = orderDao.updateOrder(orderEntity);
        if(row<=0){
            return setResultError("系统错误!");
        }
        return setResultSuccess();
    }

    @Override
    public ResponseBase createOrder(@RequestBody OrderEntity orderEntity) {
        int row = orderDao.insertOrder(orderEntity);
        if(row<=0){
            return setResultError("系统错误!");
        }
        return setResultSuccess();
    }

    @Override
    public ResponseBase orderService() {
        return setResultSuccess("我是订单服务");
    }
}
