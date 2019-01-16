package com.liudehuang.controller;

import com.liudehuang.base.ResponseBase;
import com.liudehuang.constants.Constants;
import com.liudehuang.entity.UserEntity;
import com.liudehuang.feign.MemberServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liudehuang
 * @date 2018/12/19 19:04
 */
@Controller
public class RegisterController {
    @Autowired
    private MemberServiceFeign memberServiceFeign;


    private static final String REGISTER = "register";

    private static final String LOGIN = "login";



    //跳转注册页面
    @RequestMapping(value="/register",method = RequestMethod.GET)
    public String register(){
        return REGISTER;
    }

    //注册业务具体实现
    @RequestMapping(value="/register",method = RequestMethod.POST)
    public String register(UserEntity userEntity, HttpServletRequest request){
        //1、验证参数
        //2、调用会员注册接口
        ResponseBase responseBase = memberServiceFeign.regUser(userEntity);
        //3、如果失败跳转到失败页面
        if(!responseBase.getCode().equals(Constants.HTTP_RES_CODE_200)){
            request.setAttribute("error","注册失败");
            return REGISTER;
        }
        //4、如果成功，跳转到成功页面
        return LOGIN;
    }
}
