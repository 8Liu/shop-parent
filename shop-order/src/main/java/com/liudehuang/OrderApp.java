package com.liudehuang;

import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author liudehuang
 * @date 2019/1/16 17:17
 */
@EnableFeignClients
@EnableEurekaClient
@EnableTransactionManagement
@SpringBootApplication
public class OrderApp {
    public static void main(String[] args) {

        new SpringApplicationBuilder(OrderApp.class).banner(new ResourceBanner(new ClassPathResource("banner.txt"))).run(args);
    }
}
