package com.liudehuang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author liudehuang
 * @date 2019/3/19 10:51
 */
@Configuration
@Component
@ConfigurationProperties(prefix = "message")
@PropertySource("classpath:message_config.properties")
public class RpMessageProperty {
    /**
     * 消息超时时间
     */
    @Value("${message.handle.timeout}")
    private String messageHandleTimeout;
    /**
     * 消息最多发送次数
     */
    @Value("${message.max.send.times}")
    private String messageMaxSendTimes;

    public String getMessageHandleTimeout() {
        return messageHandleTimeout;
    }

    public void setMessageHandleTimeout(String messageHandleTimeout) {
        this.messageHandleTimeout = messageHandleTimeout;
    }

    public String getMessageMaxSendTimes() {
        return messageMaxSendTimes;
    }

    public void setMessageMaxSendTimes(String messageMaxSendTimes) {
        this.messageMaxSendTimes = messageMaxSendTimes;
    }
}
