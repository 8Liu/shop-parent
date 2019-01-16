package com.liudehuang.controller;

import com.liudehuang.weixin.config.WxMpConfiguration;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liudehuang
 * @date 2019/1/8 13:28
 */
@RestController
@RequestMapping("/weixin")
public class WeiXinCatController {

    @RequestMapping("/sendTemplate")
    public String createWeiXinCat(@PathVariable String appid, @RequestBody WxMpTemplateMessage wxMpTemplateMessage) throws WxErrorException {
        return WxMpConfiguration.getMpServices().get(appid).getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
    }
}
