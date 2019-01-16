package com.liudehuang.api.service;

import com.liudehuang.base.ResponseBase;
import com.liudehuang.entity.UserEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liudehuang
 * @date 2018/12/15 11:05
 */
@RequestMapping("/member")
public interface MemberService {
    /**
     * 使用userId查找用户的信息
     * @return
     */
    @RequestMapping("/findUserById")
    ResponseBase findUserById(Integer userId);

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping("/regUser")
    ResponseBase regUser(@RequestBody UserEntity user);

    /**
     * 用户登录
     * @param user
     * @return
     */
    @RequestMapping("/login")
    ResponseBase login(@RequestBody UserEntity user);

    /**
     * 根据token查找用户
     */
    @RequestMapping("/findByTokenUser")
    ResponseBase findByTokenUser(@RequestParam("token") String token);

    /**
     * 根据openid查找用户信息
     * @return
     */
    @RequestMapping("/findByOpenIdUser")
    ResponseBase findByOpenIdUser(@RequestParam("openid") String openid);

    /**
     * qq授权登录
     * @param user
     * @return
     */
    @RequestMapping("/qqLogin")
    ResponseBase qqLogin(@RequestBody UserEntity user);

    /**
     * 根据微信openid查找用户信息
     * @param wxOpenid
     * @return
     */
    @RequestMapping("/findByWxOpenIdUser")
    ResponseBase findByWxOpenIdUser(@RequestParam("wxOpenid") String wxOpenid);

    /**
     * 微信授权登录
     * @param user
     * @return
     */
    @RequestMapping("/wxLogin")
    ResponseBase wxLogin(@RequestBody UserEntity user);
}
