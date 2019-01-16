package com.liudehuang.feign;

import com.liudehuang.api.service.PayService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/1/15 10:27
 */
@Component
@FeignClient("pay")
public interface PayServiceFeign extends PayService {
}
