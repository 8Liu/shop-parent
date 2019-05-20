package com.liudehuang.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liudehuang
 * @date 2019/4/22 14:53
 * 拦截所有的服务接口，判断服务接口shang上是否有传递userToken参数
 */
@Component
public class TokenFilter extends ZuulFilter {

    @Value("${server.port}")
    private String serverPort;
    /**
     * 过滤类型 pre :表示在请求之前执行
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器执行顺序
     * 当一个请求在同一个阶段的时候，存在多个过滤器的时候，多个过滤器执行顺序问题
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 过滤器是否生效
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        //1、获取应用上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //2、获取请求
        HttpServletRequest request = requestContext.getRequest();
        //3、从请求头中获取上下文
        String userToken = request.getParameter("userToken");
        if(StringUtils.isEmpty(userToken)){
            //如果这个值设置为false，表示不会去执行服务接口，网关服务直接相应给客户端
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseBody("userToken is null");
            requestContext.setResponseStatusCode(401);
            return null;
        }
        System.out.println(String.format("网关服务器端口号"+serverPort));
        //正常调用其他服务
        return null;
    }
}
