package com.liudehuang;

import com.alibaba.fastjson.JSONObject;
import com.liudehuang.entity.RpMessage;
import com.liudehuang.enums.MessageStatusEnum;
import com.liudehuang.enums.PublicEnum;
import com.liudehuang.util.CreateIdUtils;

/**
 * @author liudehuang
 * @date 2019/3/19 15:31
 */
public class Test {
    public static void main(String[] args) {
        RpMessage message = new RpMessage();
        message.setMessageId(CreateIdUtils.getUUid());
        message.setMessageDataType("json");
        message.setConsumerQueue("ORDER_NOTIFY_QUEUE");
        message.setMessageBody("ddddddddd");
        message.setMessageSendTimes(0);
        message.setAreadlyDead(PublicEnum.NO.name());
        message.setVersion(0);
        message.setStatus(MessageStatusEnum.WAITING_CONFIRM.name());
        message.setOrderId(CreateIdUtils.getUUid());
        message.setCreater("liudehuang");
        message.setEditor("liudehuang");
        String json = JSONObject.toJSONString(message);
        System.out.println(json);
    }
}
