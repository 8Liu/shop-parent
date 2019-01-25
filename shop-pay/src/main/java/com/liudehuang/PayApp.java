package com.liudehuang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author liudehuang
 * @date 2019/1/14 17:30
 */
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class PayApp {
    public static void main(String[] args) {
        SpringApplication.run(PayApp.class,args);
    }
}
