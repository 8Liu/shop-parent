package com.liudehuang.api.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.liudehuang.alipay.config.AlipayConfig;
import com.liudehuang.base.BaseApiService;
import com.liudehuang.base.ResponseBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author liudehuang
 * @date 2019/1/15 14:54
 */
@Slf4j
@RestController
public class CallBackServiceImpl extends BaseApiService implements CallBackService {
    /**
     * 同步通知
     * @param params
     * @return
     */
    @Override
    public ResponseBase synCallBack(@RequestParam Map<String, String> params) {
        //1、日志记录
        log.info("#####支付宝同步通知synCallBack开始,params:{}",params);
        //2、验签操作
        try{
            //调用SDK验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
            log.info("#####支付宝同步通知signVerified：{}",signVerified);
            //——请在这里编写您的程序（以下代码仅作参考）——
            if(!signVerified) {
                return setResultError("验签失败");
            }
            //商户订单号
            String outTradeNo = params.get("out_trade_no");
            //支付宝交易号
            String tradeNo = params.get("trade_no");
            //付款金额
            String totalAmount = params.get("total_amount");
            JSONObject json = new JSONObject();
            json.put("outTradeNo",outTradeNo);
            json.put("tradeNo",tradeNo);
            json.put("totalAmount",totalAmount);
            return setResultSuccess(json);
        }catch (Exception e){
            log.info("#####支付宝同步通知出现异常,ERROR:",e);
            return setResultError("系统错误");
        }finally {
            log.info("#####支付宝同步通知synCallBack结束,params:{}",params);
        }

    }

    /**
     * 异步通知
     * @param params
     * @return
     */
    @Override
    public String asynCallBack(@RequestParam Map<String, String> params) {
        return null;
    }
}
