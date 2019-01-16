package com.liudehuang.feign;

import com.liudehuang.api.service.CallBackService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/1/15 16:43
 */
@Component
@FeignClient("pay")
public interface CallBackServiceFeign extends CallBackService {
}
