package com.liudehuang.feign;

import com.liudehuang.api.service.OrderService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/1/17 10:19
 */
@Component
@FeignClient("order")
public interface OrderServiceFeign extends OrderService {
}
