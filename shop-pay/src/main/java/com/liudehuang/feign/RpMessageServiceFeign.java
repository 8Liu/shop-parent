package com.liudehuang.feign;

import com.liudehuang.api.service.RpMessageService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/3/19 13:10
 */
@Component
@FeignClient("rpMessage")
public interface RpMessageServiceFeign extends RpMessageService {
}
