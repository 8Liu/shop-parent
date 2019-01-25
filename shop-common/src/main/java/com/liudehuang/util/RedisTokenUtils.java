package com.liudehuang.util;

import com.liudehuang.base.BaseRedisService;
import com.liudehuang.constants.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author liudehuang
 * @date 2019/1/25 11:12
 */
@Component
public class RedisTokenUtils {
    @Autowired
    private BaseRedisService baseRedisService;

    /**
     * 生成token
     * @return
     */
    public String getToken(){
        //生成token规则保证临时且唯一，不支持分布式场景，分布式全局ID生成规则
        String token = Constants.FORM_COMMIT_TOKEN+ UUID.randomUUID();
        // 如何保证token临时 （缓存）使用redis 实现缓存
        baseRedisService.setString(token, token, Constants.FORM_COMMIT_TOKEN_TIME);
        return token;
    }

    // 1.在调用接口之前生成对应的令牌(Token), 存放在Redis
    // 2.调用接口的时候，将该令牌放入的请求头中
    // 3.接口获取对应的令牌,如果能够获取该令牌(将当前令牌删除掉) 就直接执行该访问的业务逻辑
    // 4.接口获取对应的令牌,如果获取不到该令牌 直接返回请勿重复提交
    public synchronized boolean findToken(String tokenKey) {
        // 3.接口获取对应的令牌,如果能够获取该(从redis获取令牌)令牌(将当前令牌删除掉) 就直接执行该访问的业务逻辑
        String tokenValue = (String) baseRedisService.getString(tokenKey);
        if (StringUtils.isEmpty(tokenValue)) {
            return false;
        }
        // 保证每个接口对应的token 只能访问一次，保证接口幂等性问题
        baseRedisService.delKey(tokenValue);
        return true;
    }
}
