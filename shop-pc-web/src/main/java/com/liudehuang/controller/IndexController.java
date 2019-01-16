package com.liudehuang.controller;

import com.liudehuang.base.ResponseBase;
import com.liudehuang.constants.Constants;
import com.liudehuang.feign.MemberServiceFeign;
import com.liudehuang.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

/**
 * @author liudehuang
 * @date 2018/12/19 17:28
 */
@Controller
public class IndexController {

    private static final String INDEX = "index";

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    //主页
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(HttpServletRequest request){
        //1、从cookie中获取token信息
        String token = CookieUtil.getUid(request, Constants.COOKIE_MEMBER_TOKEN);
        //2、如果cookie中存在token,调用会员服务接口，使用token查询用户信息
        if(!StringUtils.isEmpty(token)){
            ResponseBase responseBase = memberServiceFeign.findByTokenUser(token);
            if(responseBase.getCode().equals(Constants.HTTP_RES_CODE_200)){
                LinkedHashMap userData = (LinkedHashMap) responseBase.getData();
                String username = (String) userData.get("username");
                request.setAttribute("username",username);
            }
        }
        return INDEX;
    }

}
