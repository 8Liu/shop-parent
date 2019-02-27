package com.liudehuang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/2/27 11:33
 */
@Component
@PropertySource(value={"classpath:weiXinConfig.properties"},encoding = "utf-8")
public class WeiXinProperties {
    /**
     * 微信的appId
     */
    @Value("${appId}")
    public String appId;
    /**
     * 微信支付商户号
     */
    @Value("${mchId}")
    public String mchId;
    /**
     * 微信支付商户密钥
     */
    @Value("${mchKey}")
    public String mchKey;
    /**
     * 服务商模式下的子商户号公众账号ID
     */
    @Value("${subAppId}")
    public String subAppId;
    /**
     * 服务商模式下的自商户号
     */
    @Value("${subMchId}")
    public String subMchId;
    /**
     * p12证书的位置，可以指定绝对路径，也可以指定类路径（以classpath:开头）
     */
    @Value("${keyPath}")
    public String keyPath;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getSubAppId() {
        return subAppId;
    }

    public void setSubAppId(String subAppId) {
        this.subAppId = subAppId;
    }

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }
}
