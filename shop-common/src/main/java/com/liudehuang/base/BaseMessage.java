package com.liudehuang.base;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liudehuang
 * @date 2018/12/26 9:24
 */
@Getter
@Setter
public class BaseMessage {

    /**
     * 开发者微信
     */
    private String ToUserName;
    /**
     * 发送方openid
     */
    private String FromUserName;
    /**
     * 创建时间
     */
    private long CreateTime;
    /**
     * 内容类型
     */
    private String MsgType;
    // /**
    // * 消息id
    // */
    // private long MsgId ;
}
