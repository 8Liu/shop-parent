package com.liudehuang.pay.app.message.scheduled;

/**
 * @author liudehuang
 * @date 2019/3/20 10:38
 */
public interface MessageScheduled {
    /**
     * 处理状态为待确认但已经超时的消息
     */
    void handleWaitingConfirmTimeOutMessages();

    /**
     * 处理状态为发送中但超时没有被成功消费确认的消息
     */
    void handleSendingTimeOutMessage();

}
