package com.liudehuang.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liudehuang
 * @date 2018/12/25 19:40
 */
@RestController
public class TestController {
    @RequestMapping("/index")
    public String indext(){
        return "外网可以访问";
    }
}
