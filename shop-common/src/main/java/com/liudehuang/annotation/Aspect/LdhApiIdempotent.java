package com.liudehuang.annotation.Aspect;

import com.liudehuang.annotation.LdhApiToken;
import com.liudehuang.constants.Constants;
import com.liudehuang.util.RedisTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author liudehuang
 * @date 2019/1/25 13:31
 */
@Aspect
@Component
public class LdhApiIdempotent {
    @Autowired
    private RedisTokenUtils redisTokenUtils;

    @Pointcut("execution(public * com.liudehuang.controller.*.*(..))")
    public void pointCut(){

    }

    // 前置通知
    @Before("pointCut()")
    public void before(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        LdhApiToken extApiToken = signature.getMethod().getDeclaredAnnotation(LdhApiToken.class);
        if (extApiToken != null) {
            // 可以放入到AOP代码 前置通知
            getRequest().setAttribute("token", redisTokenUtils.getToken());
        }
    }

    //环绕通知
    @Around("pointCut()")
    public Object doBefore(ProceedingJoinPoint joinPoint)throws Throwable{
        //1、使用aop环绕通知拦截所有的访问（controller）
        //2、判断方法上是否有加LdhApiIdempotent注解
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        com.liudehuang.annotation.LdhApiIdempotent annotation = methodSignature.getMethod().getDeclaredAnnotation(com.liudehuang.annotation.LdhApiIdempotent.class);
        if(annotation!=null){
            String type = annotation.type();
            String token = null;
            HttpServletRequest request = getRequest();
            if(type.equals(Constants.EXTAPIHEAD)){
                token = request.getHeader("token");
            }else {
                token = request.getParameter("token");
            }
            if(StringUtils.isEmpty(token)){
                return "参数错误";
            }
            boolean isToken = redisTokenUtils.findToken(token);
            if(!isToken){
                response("请勿重复提交");
                return null;
            }
        }
        //放行
        Object proceed = joinPoint.proceed();
        return proceed;
    }

    public HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }

    public void response(String msg) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        try {
            writer.println(msg);
        } catch (Exception e) {

        } finally {
            writer.close();
        }

    }
}
