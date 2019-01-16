package com.liudehuang.controller;

import com.liudehuang.base.ResponseBase;
import com.liudehuang.constants.Constants;
import com.liudehuang.entity.UserEntity;
import com.liudehuang.feign.MemberServiceFeign;
import com.liudehuang.util.CookieUtil;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;

/**
 * @author liudehuang
 * @date 2018/12/21 9:38
 */
@Controller
public class LoginController {
    private static final String LOGIN = "login";

    private static final String INDEX = "redirect:/";

    private static final String QQRELATION = "qqrelation";

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @RequestMapping(value="/login",method = RequestMethod.GET)
    public String login(){
        return LOGIN;
    }

    @RequestMapping(value="/login",method = RequestMethod.POST)
    public String loginPost(UserEntity userEntity, HttpServletRequest request, HttpServletResponse response){
        //1、验证参数
        //2、调用登录接口，获取token信息
        ResponseBase login = memberServiceFeign.login(userEntity);
        if(!login.getCode().equals(Constants.HTTP_RES_CODE_200)){
            request.setAttribute("error","账号密码错误");
            return LOGIN;
        }

        //3、将token信息放在cookie中
        LinkedHashMap loginData = (LinkedHashMap) login.getData();
        String token = (String)loginData.get("token");
        if(StringUtils.isEmpty(token)){
            request.setAttribute("error","会话已经失效!");
            return LOGIN;
        }

        setCookie(token,response);
        return INDEX;
    }

    //生成qq授权登录的链接
    @RequestMapping("/locaQQLogin")
    public String localQQLogin(HttpServletRequest request) throws QQConnectException {
        //生成授权链接
        String authorizeURL = new Oauth().getAuthorizeURL(request);
        return "redirect:"+authorizeURL;
    }

    @RequestMapping("/qqLoginCallback")
    public String qqLoginCallback(HttpServletRequest request, HttpSession session,HttpServletResponse response) throws QQConnectException {
        //1、获取授权码code
        //2、使用授权码code换取accessToken
        AccessToken accessTokenObj = new Oauth().getAccessTokenByRequest(request);
        if(accessTokenObj==null){
            request.setAttribute("error","QQ授权失败");
            return "error";
        }
        String accessToken = accessTokenObj.getAccessToken();
        if(accessToken==null){
            request.setAttribute("error","accessToken为null");
            return "error";
        }
        //3、使用accessToken换取openid
        OpenID openID = new OpenID(accessToken);
        String userOpenID = openID.getUserOpenID();
        //4、调用会员服务接口 使用userOpenID查找是否已经关联账号
        ResponseBase responseBase = memberServiceFeign.findByOpenIdUser(userOpenID);
        if(responseBase.getCode().equals(Constants.HTTP_RES_CODE_201)){
            //5、如果没有关联账号，跳转到关联账号页面
            session.setAttribute("qqOpenid",userOpenID);
            return QQRELATION;
        }

        //6、已经绑定账号,存入会话信息,自动登录，将用户的token存放在cookie中
        LinkedHashMap dataToken = (LinkedHashMap) responseBase.getData();
        String token = (String)dataToken.get("token");
        setCookie(token,response);
        return INDEX;

    }

    @RequestMapping("/localWXLogin")
    public String localWXLogin(HttpServletRequest request){
        return null;
    }

    public void setCookie(String token,HttpServletResponse response){
        CookieUtil.addCookie(response,Constants.COOKIE_MEMBER_TOKEN,token,Constants.COOKIE_TOKEN_MEMBER_TIME);
    }


    /**
     * qq授权关联页面，已有账号
     * @return
     */
    @RequestMapping(value="/qqReliation",method = RequestMethod.POST)
    public String qqReliation(UserEntity userEntity, HttpServletRequest request, HttpServletResponse response,HttpSession session){
        //1、获取openid
        String qqOpenid = (String)session.getAttribute("qqOpenid");
        if(StringUtils.isEmpty(qqOpenid)){
            session.setAttribute("error","没有获取到openid");
            return "error";
        }
        userEntity.setOpenid(qqOpenid);
        //2、调用登录接口，获取token信息
        ResponseBase login = memberServiceFeign.qqLogin(userEntity);
        if(!login.getCode().equals(Constants.HTTP_RES_CODE_200)){
            request.setAttribute("error","账号密码错误");
            return LOGIN;
        }

        //3、将token信息放在cookie中
        LinkedHashMap loginData = (LinkedHashMap) login.getData();
        String token = (String)loginData.get("token");
        if(StringUtils.isEmpty(token)){
            request.setAttribute("error","会话已经失效!");
            return LOGIN;
        }

        setCookie(token,response);
        return INDEX;
    }
}
