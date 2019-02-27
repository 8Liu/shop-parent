package com.liudehuang.api.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.liudehuang.config.AlipayConfig;
import com.liudehuang.base.BaseApiService;
import com.liudehuang.base.BaseRedisService;
import com.liudehuang.base.ResponseBase;
import com.liudehuang.constants.Constants;
import com.liudehuang.dao.PaymentInfoDao;
import com.liudehuang.entity.PaymentInfo;
import com.liudehuang.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liudehuang
 * @date 2019/1/14 16:28
 */
@Slf4j
@RestController
public class PayServiceImpl extends BaseApiService implements PayService {

    @Autowired
    private PaymentInfoDao paymentInfoDao;

    @Autowired
    private BaseRedisService baseRedisService;

    @Autowired
    private WxPayService wxPayService;
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
     * 使用支付令牌查找支付信息生成网页支付的form表单
     * @param payToken
     * @return
     */
    @Override
    public ResponseBase aliPagePay(@RequestParam("payToken") String payToken){
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

        //设置请求参数(网页端登录)
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
            AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);
            log.info("body:{}",response.getBody());
            JSONObject data = new JSONObject();
            data.put("payHtml",result);
            return setResultSuccess(data);
        }catch (Exception e){
            return setResultError("支付异常");
        }


    }

    @Override
    public ResponseBase aliQrPay(@RequestParam("payToken") String payToken) {
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

        //6、对接支付代码,返回支付的二维码
        //获得初始化的AlipayClient
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
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
                + "\"seller_id\":\"2088102177165112\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\""
                //+ "\"body\":\""+ body +"\","
                +"}");
      /*  alipayRequest.setBizContent("{" +
                "\"out_trade_no\":"+out_trade_no+"," +
                "\"seller_id\":\"2088102177165112\"," +
                "\"total_amount\":"+total_amount+"," +
                "\"discountable_amount\":8.88," +
                "\"subject\":\"Iphone6 16G\"," +
                "      \"goods_detail\":[{" +
                "        \"goods_id\":\"apple-01\"," +
                "\"goods_name\":\"ipad\"," +
                "\"quantity\":1," +
                "\"price\":2000," +
                "\"goods_category\":\"34543238\"," +
                "\"categories_tree\":\"124868003|126232002|126252004\"," +
                "\"body\":\"特价手机\"," +
                "\"show_url\":\"http://www.alipay.com/xxx.jpg\"" +
                "        }]," +
                "\"body\":\"Iphone6 16G\"," +
                "\"operator_id\":\"yx_001\"," +
                "\"store_id\":\"NJ_001\"," +
                "\"disable_pay_channels\":\"pcredit,moneyFund,debitCardExpress\"," +
                "\"enable_pay_channels\":\"pcredit,moneyFund,debitCardExpress\"," +
                "\"terminal_id\":\"NJ_T_001\"," +
                "\"extend_params\":{" +
                "\"sys_service_provider_id\":\"2088511833207846\"," +
                "\"industry_reflux_info\":\"{\\\\\\\"scene_code\\\\\\\":\\\\\\\"metro_tradeorder\\\\\\\",\\\\\\\"channel\\\\\\\":\\\\\\\"xxxx\\\\\\\",\\\\\\\"scene_data\\\\\\\":{\\\\\\\"asset_name\\\\\\\":\\\\\\\"ALIPAY\\\\\\\"}}\"," +
                "\"card_type\":\"S0JP0000\"" +
                "    }," +
                "\"timeout_express\":\"90m\"," +
                "\"settle_info\":{" +
                "        \"settle_detail_infos\":[{" +
                "          \"trans_in_type\":\"cardSerialNo\"," +
                "\"trans_in\":\"A0001\"," +
                "\"summary_dimension\":\"A0001\"," +
                "\"settle_entity_id\":\"2088xxxxx;ST_0001\"," +
                "\"settle_entity_type\":\"SecondMerchant、Store\"," +
                "\"amount\":0.1" +
                "          }]" +
                "    }," +
                "\"merchant_order_no\":\"20161008001\"," +
                "\"business_params\":\"{\\\"data\\\":\\\"123\\\"}\"," +
                "\"qr_code_timeout_express\":\"90m\"" +
                "  }");*/
        //请求
        try{
            AlipayTradePrecreateResponse response = alipayClient.execute(alipayRequest);
            String body = response.getBody();
            log.info("body:{}",body);
            String qrCode = response.getQrCode();
            return setResultSuccess(qrCode);
        }catch (Exception e){
            return setResultError("支付异常");
        }
    }

    @Override
    public ResponseBase aliAppPay(@RequestParam("payToken") String payToken) {
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
        //6、构建初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
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
                + "\"seller_id\":\"2088102177165112\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\""
                //+ "\"body\":\""+ body +"\","
                +"}");
        //请求
        try{
            AlipayTradeAppPayResponse response = alipayClient.pageExecute(alipayRequest);
            String body = response.getBody();
            log.info("body:{}",body);
            return setResultSuccess(body);
        }catch (Exception e){
            return setResultError("支付异常");
        }

    }

    @Override
    public ResponseBase weiXinPagePay(String payToken) {
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
        //创建微信统一下单请求
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        String body = "qq会员充值";
        //付款金额，必填,企业金额，单位:分
        Long total_amount = paymentInfo.getPrice();
        request.setBody(body);
        request.setNotifyUrl("http://183.246.86.117:10009/djpay/weixin/getwxasynmsg.do");
        //设置订单号
        request.setOutTradeNo(paymentInfo.getOrderId());
        //设置产品ID
        request.setProductId(paymentInfo.getOrderId());
        request.setTotalFee(total_amount.intValue());
        request.setSpbillCreateIp("183.246.86.117");
        request.setTradeType("MWEB");
        try {
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(request);
            log.info("result:"+result);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("weixin_web_url",result.getMwebUrl());
            return setResultSuccess(jsonObject);
        } catch (WxPayException e) {
            e.printStackTrace();
            return setResultError("支付异常");
        }
    }

    @Override
    public ResponseBase weiXinQrPay(@RequestParam("payToken") String payToken){
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
        //创建微信统一下单请求
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        String body = "qq会员充值";
        //付款金额，必填,企业金额，单位:分
        Long total_amount = paymentInfo.getPrice();
        request.setBody(body);
        request.setNotifyUrl("http://183.246.86.117:10009/djpay/weixin/getwxasynmsg.do");
        //设置订单号
        request.setOutTradeNo(paymentInfo.getOrderId());
        //设置产品ID
        request.setProductId(paymentInfo.getOrderId());
        request.setTotalFee(total_amount.intValue());
        request.setSpbillCreateIp("183.246.86.117");
        request.setTradeType("NATIVE");

        try {
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(request);
            log.info("result:"+result);
            return setResultSuccess(result.getCodeURL());
        } catch (WxPayException e) {
            e.printStackTrace();
            return setResultError("支付异常");
        }

    }
}
