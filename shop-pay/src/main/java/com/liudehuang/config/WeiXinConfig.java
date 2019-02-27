package com.liudehuang.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liudehuang
 * @date 2019/2/27 13:15
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
public class WeiXinConfig {

    private WeiXinProperties weiXinProperties;

    @Autowired
    public WeiXinConfig(WeiXinProperties properties) {
        this.weiXinProperties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(this.weiXinProperties.getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(this.weiXinProperties.getMchId()));
        payConfig.setMchKey(StringUtils.trimToNull(this.weiXinProperties.getMchKey()));
        payConfig.setSubAppId(StringUtils.trimToNull(this.weiXinProperties.getSubAppId()));
        payConfig.setSubMchId(StringUtils.trimToNull(this.weiXinProperties.getSubMchId()));
        payConfig.setKeyPath(StringUtils.trimToNull(this.weiXinProperties.getKeyPath()));

        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}
