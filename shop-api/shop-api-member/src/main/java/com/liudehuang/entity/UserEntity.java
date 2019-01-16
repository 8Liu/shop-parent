package com.liudehuang.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author liudehuang
 * @date 2018/12/15 11:03
 */
@Getter
@Setter
public class UserEntity {
    /**
     * 唯一标识
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 更新时间
     */
    private Date updated;
    /**
     * qqopenid
     */
    private String openid;
    /**
     * 微信openid
     */
    private String wxopenid;
}
