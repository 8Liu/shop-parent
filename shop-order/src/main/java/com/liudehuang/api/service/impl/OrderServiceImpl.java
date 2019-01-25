package com.liudehuang.api.service.impl;

import com.liudehuang.api.service.OrderService;
import com.liudehuang.base.BaseApiService;
import com.liudehuang.base.ResponseBase;
import com.liudehuang.dao.OrderDao;
import com.liudehuang.entity.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseBase updateOrder(@RequestParam("isPay") Long isPay, @RequestParam("payId") String aliPayId,
                                          @RequestParam("orderNumber") String orderNumber) {
        int updateOrder = orderDao.updateOrder(isPay, aliPayId, orderNumber);
        if (updateOrder <= 0) {
            return setResultError("系统错误!");
        }
        return setResultSuccess();
    }


}
