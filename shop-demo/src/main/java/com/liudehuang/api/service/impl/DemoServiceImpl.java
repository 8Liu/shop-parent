package com.liudehuang.api.service.impl;

import com.liudehuang.api.service.DemoService;
import com.liudehuang.base.BaseApiService;
import com.liudehuang.base.ResponseBase;
import com.liudehuang.dao.DemoDao;
import com.liudehuang.entity.OrderInfo;
import com.liudehuang.util.RedisTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liudehuang
 * @date 2019/1/25 11:10
 */
@RestController
@Slf4j
public class DemoServiceImpl extends BaseApiService implements DemoService {
    @Autowired
    private DemoDao demoDao;
    @Autowired
    private RedisTokenUtils redisTokenUtils;

    public HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }
    @Override
    public ResponseBase addOrder(@RequestBody OrderInfo orderInfo) {
        //如何使用token解决幂等性问题
        //1、调用接口时候，将该令牌放入到请求头中去（获取请求头中的令牌）
        HttpServletRequest request = getRequest();
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return setResultError("参数错误");
        }
        //2、接口获取对应的令牌，如果能够获取该令牌（将当前令牌删了），直接执行访问
        boolean isToken = redisTokenUtils.findToken(token);
        //3、接口获取对应的令牌,如果获取不到该令牌 直接返回请勿重复提交
        if(!isToken){
            return setResultError("请勿重复提交");
        }
        int row = demoDao.addOrder(orderInfo);
        if(row==1){
            return setResultSuccess("添加成功");
        }
        return setResultError("添加失败");
    }

    @Override
    public ResponseBase redisToken() {
        return setResultSuccess(redisTokenUtils.getToken());
    }

    @Override
    public ResponseBase insertOrder(@RequestBody OrderInfo orderInfo) {
        int row = demoDao.addOrder(orderInfo);
        if(row==1){
            return setResultSuccess("添加成功");
        }
        return setResultError("添加失败");
    }
}
