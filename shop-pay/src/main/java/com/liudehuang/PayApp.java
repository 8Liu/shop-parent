package com.liudehuang;

import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.core.io.ClassPathResource;

/**
 * @author liudehuang
 * @date 2019/1/14 17:30
 */
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class PayApp {
    public static void main(String[] args) {

        new SpringApplicationBuilder(PayApp.class).banner(new ResourceBanner(new ClassPathResource("banner.txt"))).run(args);

    }
}
