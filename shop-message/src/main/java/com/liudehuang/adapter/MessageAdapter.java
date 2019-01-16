package com.liudehuang.adapter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author liudehuang
 * @date 2018/12/17 18:55
 * 统一发送消息接口
 */
public interface MessageAdapter {
    /**
     * 发送消息
     * @param body
     */
    void sendMsg(JSONObject body);
}
