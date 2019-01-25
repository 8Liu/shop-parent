package com.liudehuang.annotation;

import com.liudehuang.constants.Constants;

import java.lang.annotation.*;

/**
 * @author liudehuang
 * @date 2019/1/25 13:27
 */
//解决接口幂等性问题（支持网络延迟和表单重复提交）
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LdhApiIdempotent {
    String type() default Constants.EXTAPIHEAD;
}
