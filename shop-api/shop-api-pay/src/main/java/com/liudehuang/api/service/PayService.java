package com.liudehuang.api.service;

import com.liudehuang.base.ResponseBase;
import com.liudehuang.entity.PaymentInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liudehuang
 * @date 2019/1/14 16:05
 */
@RequestMapping("/pay")
public interface PayService {
    /**
     * 创建支付令牌
     * @return
     */
    @RequestMapping("/createPayToken")
    ResponseBase createToken(@RequestBody PaymentInfo paymentInfo);

    /**
     * 使用支付令牌查找支付信息
     * @param payToken
     * @return
     */
    @RequestMapping("/findPayToken")
    ResponseBase findPayToken(@RequestParam("payToken") String payToken);
}
