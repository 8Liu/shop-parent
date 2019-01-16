package com.liudehuang.api.service.impl;

import com.liudehuang.api.service.TestApiService;
import com.liudehuang.base.BaseApiService;
import com.liudehuang.base.BaseRedisService;
import com.liudehuang.base.ResponseBase;
import com.liudehuang.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liudehuang
 * @date 2018/12/13 13:19
 */
@RestController
public class TestApiServiceImpl extends BaseApiService implements TestApiService {
    @Autowired
    private BaseRedisService baseRedisService;

    @Override
    public Map<String, Object> test(Integer id, String name) {
        Map<String, Object> result = new HashMap<String,Object>();
        result.put("errorCode","200");
        result.put("msg","success");
        result.put("data",new String[]{"123","123"});
        return result;
    }

    @Override
    public ResponseBase testResponseBase() {
        return setResultSuccess();
    }

    @Override
    public ResponseBase testRedis(String key,String value) {
        baseRedisService.setString(key,value, Constants.TOKEN_MEMBER_TIME);
        return setResultSuccess();
    }


}
