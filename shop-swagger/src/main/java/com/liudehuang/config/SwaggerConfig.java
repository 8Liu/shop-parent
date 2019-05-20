package com.liudehuang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author liudehuang
 * @date 2019/4/23 10:02
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                //生成扫包范围
                .apis(RequestHandlerSelectors.basePackage("com.liudehuang.api")).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo(){

        //title 标题
        //description 描述
        return new ApiInfoBuilder().title("shop-parent商城").description("刘德煌").termsOfServiceUrl("127.0.0.1")
                .version("1.0").build();
    }
}
