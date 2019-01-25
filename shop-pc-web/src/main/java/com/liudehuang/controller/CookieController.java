package com.liudehuang.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liudehuang
 * @date 2019/1/23 15:34
 */
@RestController
@RequestMapping("/cookie")
public class CookieController {

    @RequestMapping("/getCookie")
    public String getCookie(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie c:cookies){
                System.out.println(c.getName()+","+c.getValue()+","+c.getDomain()+","+c.getPath()+","+c.getMaxAge()+","+c.getSecure()+","+c.isHttpOnly());

            }
        }
        //写cookie
        Cookie cookie = new Cookie("mycookie",String.valueOf(System.currentTimeMillis()));
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        response.addCookie(cookie);
        return null;
    }
}
