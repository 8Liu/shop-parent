package com.liudehuang.controller;

import com.liudehuang.base.ResponseBase;
import com.liudehuang.constants.Constants;
import com.liudehuang.feign.PayServiceFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

/**
 * @author liudehuang
 * @date 2019/1/15 10:30
 */
@Slf4j
@Controller
public class PayController {
    @Autowired
    private PayServiceFeign payServiceFeign;
    //使用payToken进行支付
    @RequestMapping("/aliPay")
    public void aliPay(String payToken, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        //1、参数验证
        if(StringUtils.isEmpty(payToken)){
            return;
        }
        //2、调用支付服务接口，获取支付宝html元素
        ResponseBase payTokenResult = payServiceFeign.findPayToken(payToken);
        if(!payTokenResult.getCode().equals(Constants.HTTP_RES_CODE_200)){
            String message = payTokenResult.getMsg();
            writer.print(message);
            return;
        }

        //3、返回可以执行的html元素给客户端
        LinkedHashMap map = (LinkedHashMap) payTokenResult.getData();
        String payHtml = (String)map.get("payHtml");
        log.info("payHtml:{}",payHtml);
        //4、页面上渲染出form表单
        writer.write(payHtml);
        writer.close();

    }
}
