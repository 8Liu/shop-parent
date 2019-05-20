package com.liudehuang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author liudehuang
 * @date 2019/4/23 10:15
 */
@SpringBootApplication
@EnableEurekaClient
public class SwaggerApp {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerApp.class,args);
    }
}
