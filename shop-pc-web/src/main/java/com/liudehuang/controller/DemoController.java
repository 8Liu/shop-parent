package com.liudehuang.controller;

import com.liudehuang.annotation.LdhApiIdempotent;
import com.liudehuang.annotation.LdhApiToken;
import com.liudehuang.base.ResponseBase;
import com.liudehuang.constants.Constants;
import com.liudehuang.entity.OrderInfo;
import com.liudehuang.feign.DemoServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liudehuang
 * @date 2019/1/25 13:17
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private DemoServiceFeign demoServiceFeign;

    @RequestMapping(value = "/indexPage",method = RequestMethod.GET)
    @LdhApiToken
    public String indexPage(){
        return "indexPage";
    }

    @ResponseBody
    @RequestMapping(value = "/addOrderPage",method = RequestMethod.POST)
    @LdhApiIdempotent(type = Constants.EXTAPIFROM)
    public String addOrderPage(OrderInfo orderInfo){
        ResponseBase responseBase = demoServiceFeign.insertOrder(orderInfo);
        return responseBase.getMsg();
    }

    @ResponseBody
    @RequestMapping("/getToken")
    public String getToken(){
        ResponseBase responseBase = demoServiceFeign.redisToken();
        return "success";

    }

}
