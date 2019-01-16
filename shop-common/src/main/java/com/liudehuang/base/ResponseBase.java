package com.liudehuang.base;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liudehuang
 * @date 2018/12/13 13:47
 */
@Setter
@Getter
@Slf4j
public class ResponseBase {
    /**
     * code码
     */
    private Integer code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 值
     */
    private Object data;

    public ResponseBase(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseBase() {
    }
}
