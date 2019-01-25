package com.liudehuang.annotation;

import java.lang.annotation.*;

/**
 * @author liudehuang
 * @date 2019/1/25 14:05
 */
//执行请求的时候，需要生成token(令牌）,转发到页面进行展示
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LdhApiToken {
}
