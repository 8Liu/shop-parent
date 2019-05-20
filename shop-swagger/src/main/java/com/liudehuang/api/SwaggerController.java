package com.liudehuang.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liudehuang
 * @date 2019/4/23 10:10
 */

@RestController
@Api("SwaggerDemo控制器")
public class SwaggerController {

    @ApiOperation("swagger演示接口")
    @GetMapping("/swaggerIndex")
    public String swaggerIndex(){
        return "swaggerIndex";
    }


    @ApiOperation("获取会员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "query", dataType = "String")
    })
    @PostMapping("/getMember")
    public String getMember(@RequestParam("userName") String userName){
        System.out.println(userName);
        return userName;
    }
}
