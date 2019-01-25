package com.liudehuang.api.service;

import com.liudehuang.base.ResponseBase;
import com.liudehuang.entity.OrderInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liudehuang
 * @date 2019/1/25 10:05
 */
@RequestMapping("/demo")
public interface DemoService {
    /**
     * 添加订单
     * @param orderInfo
     * @return
     */
    @RequestMapping(value = "/addOrder",method = RequestMethod.POST)
    ResponseBase addOrder(@RequestBody OrderInfo orderInfo);

    /**
     * 从redis中获取token
     * @return
     */
    @RequestMapping("/redisToken")
    ResponseBase redisToken();

    /**
     * 插入订单
     * @param orderInfo
     * @return
     */
    @RequestMapping(value = "/insertOrder",method = RequestMethod.POST)
    ResponseBase insertOrder(@RequestBody OrderInfo orderInfo);
}
