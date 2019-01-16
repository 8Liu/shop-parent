package com.liudehuang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author liudehuang
 * @date 2018/12/19 17:36
 */
@EnableFeignClients
@SpringBootApplication
@EnableEurekaClient
public class PCWebApp {

    public static void main(String[] args) {
        SpringApplication.run(PCWebApp.class,args);
    }
}
