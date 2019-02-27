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
     * 使用支付令牌查找支付信息（网页端支付form表单）
     * @param payToken
     * @return
     */
    @RequestMapping("/aliPagePay")
    ResponseBase aliPagePay(@RequestParam("payToken") String payToken);

    /**
     * 支付宝扫码支付
     * @param payToken
     * @return
     */
    @RequestMapping("/aliQrPay")
    ResponseBase aliQrPay(@RequestParam("payToken") String payToken);

    /**
     * 支付宝安卓端Pay
     * @param payToken
     * @return
     */
    @RequestMapping("/aliAppPay")
    ResponseBase aliAppPay(@RequestParam("payToken") String payToken);

    @RequestMapping("/weiXinPagePay")
    ResponseBase weiXinPagePay(@RequestParam("payToken")String payToken);

    @RequestMapping("/weiXinQrPay")
    ResponseBase weiXinQrPay(@RequestParam("payToken") String payToken);
}
