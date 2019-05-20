package com.liudehuang;

import com.liudehuang.config.RpMessageProperty;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liudehuang
 * @date 2019/3/19 9:59
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.liudehuang.dao")
@EnableConfigurationProperties(RpMessageProperty.class)
public class RpMessageApp {
    public static void main(String[] args) {

        SpringApplication.run(RpMessageApp.class,args);
    }
}
