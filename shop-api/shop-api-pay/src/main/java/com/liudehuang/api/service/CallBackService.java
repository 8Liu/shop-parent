package com.liudehuang.api.service;

import com.liudehuang.base.ResponseBase;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author liudehuang
 * @date 2019/1/15 14:47
 */
@RequestMapping("/callBack")
public interface CallBackService {

    /**
     *  同步通知
     */
    @RequestMapping("/synCallBackService")
    ResponseBase synCallBack(@RequestParam Map<String,String> params);
    /**
     *   异步通知
     */
    @RequestMapping("/asynCallBackService")
    String asynCallBack(@RequestParam Map<String,String> params);

}
