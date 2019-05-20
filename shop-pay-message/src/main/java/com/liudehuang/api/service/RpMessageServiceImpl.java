package com.liudehuang.api.service;

import com.liudehuang.base.BaseApiService;
import com.liudehuang.base.ResponseBase;
import com.liudehuang.config.RpMessageProperty;
import com.liudehuang.dao.RpMessageDao;
import com.liudehuang.entity.RpMessage;
import com.liudehuang.enums.MessageStatusEnum;
import com.liudehuang.enums.PublicEnum;
import com.liudehuang.mq.RpMessageMqProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author liudehuang
 * @date 2019/3/19 9:15
 */
@Slf4j
@RestController
public class RpMessageServiceImpl extends BaseApiService implements RpMessageService {
    @Autowired
    private RpMessageDao rpMessageDao;
    @Autowired
    private RpMessageMqProducer rpMessageMqProducer;
    @Autowired
    private RpMessageProperty rpMessageProperty;

    @Override
    public ResponseBase saveMessageWaitingConfirm(@RequestBody RpMessage rpMessage) {
        if(rpMessage==null){
            return setResultError("保存的消息为空");
        }
        if(StringUtils.isEmpty(rpMessage.getConsumerQueue())){
            return setResultError("消息的消费队列不能为空");
        }
        //设置消息状态为待确认
        rpMessage.setStatus(MessageStatusEnum.WAITING_CONFIRM.name());
        rpMessage.setAreadlyDead(PublicEnum.NO.name());
        rpMessage.setMessageSendTimes(0);
        int row = rpMessageDao.saveMessage(rpMessage);
        if(row!=1){
            return setResultError("保存消息失败");
        }
        return setResultSuccess();
    }

    @Override
    public ResponseBase confirmAndSendMessage(@RequestParam("messageId") String messageId) {
        RpMessage rpMessage = rpMessageDao.getMessageByMessageId(messageId);
        if(rpMessage==null){
            return setResultError("保存的消息为空");
        }
        if(StringUtils.isEmpty(rpMessage.getConsumerQueue())){
            return setResultError("消息的消费队列不能为空");
        }
        rpMessage.setStatus(MessageStatusEnum.SENDING.name());
        rpMessage.addSendTimes();
        int row = rpMessageDao.updateMessageByMessageId(rpMessage);
        if(row!=1){
            return setResultError("更新消息失败");
        }
        String queueName = rpMessage.getConsumerQueue();
        //发送mq消息
        rpMessageMqProducer.sendMsg(queueName,rpMessage.getMessageBody());
        System.out.println("发送mq消息成功");
        return setResultSuccess();
    }

    @Override
    public ResponseBase saveAndSendMessage(@RequestBody RpMessage rpMessage) {
        if(rpMessage==null){
            return setResultError("保存的消息为空");
        }
        if(StringUtils.isEmpty(rpMessage.getConsumerQueue())){
            return setResultError("消息的消费队列不能为空");
        }
        rpMessage.setStatus(MessageStatusEnum.SENDING.name());
        rpMessage.setAreadlyDead(PublicEnum.NO.name());
        rpMessage.setMessageSendTimes(0);
        int row = rpMessageDao.saveMessage(rpMessage);
        if(row!=1){
            return setResultError("保存消息失败");
        }
        String queueName = rpMessage.getConsumerQueue();
        //发送mq消息
        rpMessageMqProducer.sendMsg(queueName,rpMessage.getMessageBody());
        return setResultSuccess();
    }

    @Override
    public ResponseBase directSendMessage(@RequestBody RpMessage rpMessage) {
        if(rpMessage==null){
            return setResultError("保存的消息为空");
        }
        if(StringUtils.isEmpty(rpMessage.getConsumerQueue())){
            return setResultError("消息的消费队列不能为空");
        }
        String queueName = rpMessage.getConsumerQueue();
        //发送mq消息
        rpMessageMqProducer.sendMsg(queueName,rpMessage.getMessageBody());
        return setResultSuccess();
    }

    @Override
    public ResponseBase reSendMessage(@RequestBody RpMessage rpMessage) {
        if(rpMessage==null){
            return setResultError("保存的消息为空");
        }
        if(StringUtils.isEmpty(rpMessage.getConsumerQueue())){
            return setResultError("消息的消费队列不能为空");
        }
        rpMessage.addSendTimes();
        int row = rpMessageDao.updateMessageByMessageId(rpMessage);
        if(row!=1){
            return setResultError("更新消息失败");
        }
        String queueName = rpMessage.getConsumerQueue();
        //发送mq消息
        rpMessageMqProducer.sendMsg(queueName,rpMessage.getMessageBody());
        return setResultSuccess();
    }

    @Override
    public ResponseBase reSendMessageByMessageId(@RequestParam("messageId") String messageId) {
        RpMessage rpMessage = rpMessageDao.getMessageByMessageId(messageId);
        if(rpMessage==null){
            return setResultError("根据消息id查找的消息为空");
        }
        if(StringUtils.isEmpty(rpMessage.getConsumerQueue())){
            return setResultError("消息的消费队列不能为空");
        }
        Integer maxTimes = Integer.valueOf(rpMessageProperty.getMessageMaxSendTimes());
        if(rpMessage.getMessageSendTimes() >= maxTimes){
            rpMessage.setAreadlyDead(PublicEnum.YES.name());
        }
        rpMessage.addSendTimes();
        int row = rpMessageDao.updateMessageByMessageId(rpMessage);
        if(row!=1){
            return setResultError("更新消息失败");
        }
        String queueName = rpMessage.getConsumerQueue();
        //发送mq消息
        rpMessageMqProducer.sendMsg(queueName,rpMessage.getMessageBody());
        return setResultSuccess();
    }

    @Override
    public ResponseBase setMessageToAreadlyDead(@RequestParam("messageId") String messageId) {
        RpMessage rpMessage = rpMessageDao.getMessageByMessageId(messageId);
        if(rpMessage == null){
            return setResultError("根据消息id查找的消息为空");
        }
        rpMessage.setAreadlyDead(PublicEnum.YES.name());
        int row = rpMessageDao.updateMessageByMessageId(rpMessage);
        if(row!=1){
            return setResultError("更新消息失败");
        }
        return setResultSuccess();
    }

    @Override
    public ResponseBase getMessageByMessageId(@RequestParam("messageId") String messageId) {
        RpMessage rpMessage = rpMessageDao.getMessageByMessageId(messageId);
        return setResultSuccess(rpMessage);
    }

    @Override
    public ResponseBase deleteMessageByMessageId(@RequestParam("messageId") String messageId) {
        int row = rpMessageDao.deleteMessageByMessageId(messageId);
        if(row!=1){
            return setResultError("删除消息失败");
        }
        return setResultSuccess();
    }

    @Override
    public ResponseBase reSendAllDeadMessageByQueueName(String queueName, int batchSize) {
        return null;
    }

    @Override
    public ResponseBase listPage(Map<String, Object> paramMap) {
        return null;
    }
}
