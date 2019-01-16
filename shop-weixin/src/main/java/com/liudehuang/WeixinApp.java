package com.liudehuang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author liudehuang
 * @date 2018/12/25 19:41
 */
@SpringBootApplication
@EnableEurekaClient
public class WeixinApp {
    public static void main(String[] args) {
        SpringApplication.run(WeixinApp.class,args);
    }
}
