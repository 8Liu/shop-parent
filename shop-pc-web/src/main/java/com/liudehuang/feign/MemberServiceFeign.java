package com.liudehuang.feign;

import com.liudehuang.api.service.MemberService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2018/12/20 13:18
 */
@Component
@FeignClient("member")
public interface MemberServiceFeign extends MemberService {

}
