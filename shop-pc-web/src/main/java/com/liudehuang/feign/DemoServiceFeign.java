package com.liudehuang.feign;

import com.liudehuang.api.service.DemoService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/1/25 13:19
 */
@Component
@FeignClient("demo")
public interface DemoServiceFeign extends DemoService {
}
