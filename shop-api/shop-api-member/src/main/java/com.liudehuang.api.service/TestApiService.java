package com.liudehuang.api.service;


import com.liudehuang.base.ResponseBase;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author liudehuang
 * @date 2018/12/13 11:21
 */
@RequestMapping("/member")
public interface TestApiService {
    @RequestMapping("/test")
    Map<String, Object> test(Integer id,String name);

    @RequestMapping("/testResponseBase")
    ResponseBase testResponseBase();

    @RequestMapping("/testRedis")
    ResponseBase testRedis(String key,String value);
}
