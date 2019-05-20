package com.liudehuang.util;

import java.util.UUID;

/**
 * @author liudehuang
 * @date 2019/3/19 13:57
 * 创建uuid
 */
public class CreateIdUtils {
    public static String getUUid(){
        return UUID.randomUUID().toString().replaceAll("-","").toLowerCase();
    }
}
