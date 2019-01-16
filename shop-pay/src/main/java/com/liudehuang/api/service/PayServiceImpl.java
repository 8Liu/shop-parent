package com.liudehuang.api.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.liudehuang.alipay.config.AlipayConfig;
import com.liudehuang.base.BaseApiService;
import com.liudehuang.base.BaseRedisService;
import com.liudehuang.base.ResponseBase;
import com.liudehuang.constants.Constants;
import com.liudehuang.dao.PaymentInfoDao;
import com.liudehuang.entity.PaymentInfo;
import com.liudehuang.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liudehuang
 * @date 2019/1/14 16:28
 */
@RestController
public class PayServiceImpl extends BaseApiService implements PayService {

    @Autowired
    private PaymentInfoDao paymentInfoDao;

    @Autowired
    private BaseRedisService baseRedisService;
    /**
     * 创建支付令牌
     * @return
     */
    @Override
    public ResponseBase createToken(@RequestBody PaymentInfo paymentInfo){
        //1、创建支付请求信息
        Integer row = paymentInfoDao.savePaymentType(paymentInfo);
        if(row<=0){
            return setResultError("创建支付订单失败");
        }
        //2、生成对应的token
        String payToken = TokenUtils.getPayToken();
        //3、存放在redis中，key为：token，value为：支付id,过期时间15分钟
        baseRedisService.setString(payToken,paymentInfo.getId()+"", Constants.PAY_TOKEN_TIME);
        //4、返回token
        JSONObject result = new JSONObject();
        result.put("payToken",payToken);
        return setResultSuccess(result);
    }

    /**
     * 使用支付令牌查找支付信息
     * @param payToken
     * @return
     */
    @Override
    public ResponseBase findPayToken(@RequestParam("payToken") String payToken){
        //1、校验参数
        if(StringUtils.isEmpty(payToken)){
            return setResultError("token不能为空");
        }
        //2、判断token有效期
        String payId = (String)baseRedisService.getString(payToken);
        if(StringUtils.isEmpty(payId)){
            return setResultError("支付已经超时");
        }
        //4、使用支付id,进行下单
        //5、使用支付id查询支付信息
        PaymentInfo paymentInfo = paymentInfoDao.getPaymentInfo(Long.parseLong(payId));
        if(paymentInfo==null){
            return setResultError("未找到支付信息");
        }
        //6、对接支付代码,返回提交支付的form表单元素的token
        //获得初始化的AlipayClient
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //设置同步回调地址
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        //设置异步回调地址
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = paymentInfo.getOrderId();
        //付款金额，必填,企业金额，单位:分
        String total_amount = paymentInfo.getPrice()+"";
        //订单名称，必填
        String subject = "刘德煌会员";

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                //+ "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        try{
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            JSONObject data = new JSONObject();
            data.put("payHtml",result);
            return setResultSuccess(data);
        }catch (Exception e){
            return setResultError("支付异常");
        }


    }
}
