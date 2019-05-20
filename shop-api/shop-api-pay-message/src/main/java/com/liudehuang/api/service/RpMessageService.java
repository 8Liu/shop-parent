package com.liudehuang.api.service;

import com.liudehuang.base.ResponseBase;
import com.liudehuang.entity.RpMessage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author liudehuang
 * @date 2019/3/19 8:56
 */
@RequestMapping("/rpMessage")
public interface RpMessageService {
    /**
     * 预存储消息
     * @param rpMessage
     * @return
     */
    @RequestMapping("/saveMessageWaitingConfirm")
    ResponseBase saveMessageWaitingConfirm(@RequestBody RpMessage rpMessage);

    /**
     * 确认并发送消息
     * @param messageId
     * @return
     */
    @RequestMapping("/confirmAndSendMessage")
    ResponseBase confirmAndSendMessage(@RequestParam("messageId") String messageId);

    /**
     * 存储并发送消息
     * @param rpMessage
     * @return
     */
    @RequestMapping("/saveAndSendMessage")
    ResponseBase saveAndSendMessage(@RequestBody RpMessage rpMessage);

    /**
     * 直接发送消息
     * @param rpMessage
     * @return
     */
    @RequestMapping("/directSendMessage")
    ResponseBase directSendMessage(@RequestBody RpMessage rpMessage);

    /**
     * 重发消息
     * @param rpMessage
     * @return
     */
    @RequestMapping("/reSendMessage")
    ResponseBase reSendMessage(@RequestBody RpMessage rpMessage);

    /**
     * 根据messageId重发消息
     * @param messageId
     * @return
     */
    @RequestMapping("/reSendMessageByMessageId")
    ResponseBase reSendMessageByMessageId(@RequestParam("messageId") String messageId);

    /**
     * 将消息标记为死亡消息
     * @param messageId
     * @return
     */
    @RequestMapping("/setMessageToAreadlyDead")
    ResponseBase setMessageToAreadlyDead(@RequestParam("messageId") String messageId);

    /**
     * 根据消息ID获取消息
     * @param messageId
     * @return
     */
    @RequestMapping("/getMessageByMessageId")
    ResponseBase getMessageByMessageId(@RequestParam("messageId") String messageId);

    /**
     * 根据消息ID删除消息
     * @param messageId
     * @return
     */
    @RequestMapping("/deleteMessageByMessageId")
    ResponseBase deleteMessageByMessageId(@RequestParam("messageId") String messageId);

    /**
     * 重发消息队列中全部已经死亡的消息
     * @param queueName
     * @param batchSize
     * @return
     */
    @RequestMapping("/reSendAllDeadMessageByQueueName")
    ResponseBase reSendAllDeadMessageByQueueName(@RequestParam("queueName") String queueName,@RequestParam("batchSize") int batchSize);

    /**
     * 获取分页数据
     * @param paramMap
     * @return
     */
    @RequestMapping("/listPage")
    ResponseBase listPage(@RequestBody Map<String,Object> paramMap);
}
