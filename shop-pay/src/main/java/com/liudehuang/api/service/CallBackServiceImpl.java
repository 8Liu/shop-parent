package com.liudehuang.api.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.liudehuang.config.AlipayConfig;
import com.liudehuang.base.BaseApiService;
import com.liudehuang.base.ResponseBase;
import com.liudehuang.constants.Constants;
import com.liudehuang.dao.PaymentInfoDao;
import com.liudehuang.entity.OrderEntity;
import com.liudehuang.entity.PaymentInfo;
import com.liudehuang.entity.RpMessage;
import com.liudehuang.feign.OrderServiceFeign;
import com.liudehuang.feign.RpMessageServiceFeign;
import com.liudehuang.util.CreateIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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
    @Autowired
    private PaymentInfoDao paymentInfoDao;

    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @Autowired
    private RpMessageServiceFeign rpMessageServiceFeign;

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
    @Transactional
    @Override
    public synchronized String asynCallBack(@RequestParam Map<String, String> params) {
        //1、日志记录
        log.info("#####支付宝异步通知synCallBack开始,params:{}",params);
        //2、验签操作
        try{
            //调用SDK验证签名
            boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
            log.info("#####支付宝异步通知signVerified：{}",signVerified);
            //——请在这里编写您的程序（以下代码仅作参考）——
            if(!signVerified) {
                return Constants.PAY_FAIL;
            }
            //商户订单号
            String outTradeNo = params.get("out_trade_no");
            //支付交易id
            String tradeNo = params.get("trade_no");
            //修改支付数据库
            //使用订单号作为全局id解决幂等性问题，如果有网络延迟，需要采用分布式锁
            PaymentInfo payInfo = paymentInfoDao.getByOrderIdPayInfo(outTradeNo);
            if(payInfo==null){
                return Constants.PAY_FAIL;
            }
            //支付宝重试机制
            Integer state = payInfo.getState();
            if(state == 1){
                return Constants.PAY_SUCCESS;
            }
            log.info("==>开始处理支付成功的订单结果");
            RpMessage rpMessage = this.initRpMessage(payInfo,tradeNo);
            //插入队列消息
            ResponseBase messageResult = rpMessageServiceFeign.saveMessageWaitingConfirm(rpMessage);
            if(!messageResult.getCode().equals(Constants.HTTP_RES_CODE_200)){
                return Constants.PAY_FAIL;
            }
            //业务操作处理

            String result = completeSuccessOrder(payInfo,params,tradeNo);
            if(result.equals(Constants.PAY_FAIL)){
                return result;
            }
            //发送队列消息
            ResponseBase responseBase = rpMessageServiceFeign.confirmAndSendMessage(rpMessage.getMessageId());
            //调用订单数据库通知支付状态
            //改成mq
            /*ResponseBase responseBase = orderServiceFeign.updateOrder(1L, tradeNo, outTradeNo);
            if(!responseBase.getCode().equals(Constants.HTTP_RES_CODE_200)){
                //回滚 手动回滚事务
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Constants.PAY_FAIL;
            }*/
            //手动事务提交
            return Constants.PAY_SUCCESS;
        }catch (Exception e){
            log.info("#####支付宝异步通知出现异常,ERROR:",e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Constants.PAY_FAIL;
        }finally {
            log.info("#####支付宝异步通知synCallBack结束,params:{}",params);
        }
     }

    /**
     * 封装订单数据库所需要的订单数据
     * @param payinfo
     * @return
     */
     public RpMessage initRpMessage(PaymentInfo payinfo,String payId){
         String messageId = CreateIdUtils.getUUid();
         //封装订单数据库所需要的订单数据
         OrderEntity orderEntity = new OrderEntity();
         //设置订单为已支付状态
         orderEntity.setIsPay(1);
         //设置订单id
         orderEntity.setOrderNumber(payinfo.getOrderId());
         //设置第三方支付id
         orderEntity.setPayId(payId);

         orderEntity.setMessageId(messageId);
         String messageBody = JSONObject.toJSONString(orderEntity);

         String messageDataType = "json";
         String queueName = "ORDER_NOTIFY_QUEUE";
         RpMessage rpMessage = new RpMessage();
         rpMessage.setConsumerQueue(queueName);
         rpMessage.setMessageBody(messageBody);
         rpMessage.setMessageId(messageId);
         rpMessage.setMessageDataType(messageDataType);
         rpMessage.setOrderId(payinfo.getOrderId());
         return rpMessage;

     }

    /**
     * 处理支付成功的订单结果
     * @param payInfo
     * @param params
     * @param tradeNo
     * @return
     */
     public String completeSuccessOrder(PaymentInfo payInfo,Map<String,String> params,String tradeNo){

         //付款金额
         /*String totalAmount = params.get("total_amount");
         //判断实际付款金额与商品金额是否一致
            if(!totalAmount.equals(String.valueOf(payInfo.getPrice()))){
                return Constants.PAY_FAIL;
            }*/

         //标识为该订单已支付
         payInfo.setState(1);
         //支付宝返回的参数
         payInfo.setPayMessage(params.toString());
         //设置第三方交易id
         payInfo.setPlatformorderId(tradeNo);
         //手动事务开启
         int row = paymentInfoDao.updatePayInfo(payInfo);
         if(row <= 0){
             return Constants.PAY_FAIL;
         }
         return Constants.PAY_SUCCESS;
     }
}
