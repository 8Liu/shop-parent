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
 * @date 2018/12/19 17:36
 */
@EnableFeignClients
@SpringBootApplication
@EnableEurekaClient
public class PCWebApp {

    public static void main(String[] args) {

        new SpringApplicationBuilder(PCWebApp.class).banner(new ResourceBanner(new ClassPathResource("banner.txt"))).run(args);

    }
}
