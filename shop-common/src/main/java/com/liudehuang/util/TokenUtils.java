package com.liudehuang.util;

import com.liudehuang.constants.Constants;

import java.util.UUID;


/**
 * @author liudehuang
 * @date 2018/12/18 16:17
 */
public class TokenUtils {
    /**
     * token
     * @return
     */
    public static String getMemberToken(){
        return Constants.TOKEN_MEMBER+"-"+UUID.randomUUID();
    }

    /**
     * 获取支付token令牌
     * @return
     */
    public static String getPayToken(){
        return Constants.PAY_TOEKN+"-"+UUID.randomUUID();
    }
}
