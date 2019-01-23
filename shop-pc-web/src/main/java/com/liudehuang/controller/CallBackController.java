package com.liudehuang.controller;

import com.liudehuang.base.ResponseBase;
import com.liudehuang.constants.Constants;
import com.liudehuang.feign.CallBackServiceFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author liudehuang
 * @date 2019/1/15 16:35
 */
@Slf4j
@Controller
@RequestMapping("/alibaba/callBack")
public class CallBackController {
    @Autowired
    private CallBackServiceFeign callBackServiceFeign;

    private static final String PAY_SUCCESS = "pay_success";

    /**
     * 同步通知
     */
    @RequestMapping("/synCallBack")
    public void synCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        log.info("#####支付宝同步回调CallBackController#####synCallBack开始params:{}",params);
        PrintWriter writer = response.getWriter();
        ResponseBase synCallBackResponseBase = callBackServiceFeign.synCallBack(params);
        if(!synCallBackResponseBase.getCode().equals(Constants.HTTP_RES_CODE_200)){
            //保错页面
            return;
        }
        LinkedHashMap data = (LinkedHashMap) synCallBackResponseBase.getData();
        //封装成html的form表单，浏览器模拟提交(为了避免支付宝回调地址上的get请求参数显示在地址栏，因此模拟表单请求，隐藏同步回调的参数)
        String htmlFrom = "<form name='punchout_form'"
                + " method='post' action='http://127.0.0.1/alibaba/callBack/synSuccessPage' >"
                + "<input type='hidden' name='outTradeNo' value='" + data.get("outTradeNo") + "'>"
                + "<input type='hidden' name='tradeNo' value='" + data.get("tradeNo") + "'>"
                + "<input type='hidden' name='totalAmount' value='" + data.get("totalAmount") + "'>"
                + "<input type='submit' value='立即支付' style='display:none'>"
                + "</form><script>document.forms[0].submit();" + "</script>";
        writer.println(htmlFrom);
        writer.close();
        log.info("#####支付宝同步回调CallBackController#####synCallBack结束params:{}",params);
    }

    //以post请求隐藏参数
    @RequestMapping(method = RequestMethod.POST,value = "/synSuccessPage")
    public String synSuccessPage(HttpServletRequest request,String outTradeNo,String tradeNo,String totalAmount){
        request.setAttribute("outTradeNo",outTradeNo);
        request.setAttribute("tradeNo",tradeNo);
        request.setAttribute("totalAmount",totalAmount);
        return PAY_SUCCESS;
    }

    /**
     * 异步通知
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/asynCallBack")
    @ResponseBody
    public String asynCallBack(HttpServletRequest request, HttpServletResponse response){
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
/*            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");*/
            params.put(name, valueStr);
        }
        log.info("#####支付宝同步回调CallBackController#####synCallBack开始params:{}",params);
        String result = callBackServiceFeign.asynCallBack(params);
        return result;
    }
}
