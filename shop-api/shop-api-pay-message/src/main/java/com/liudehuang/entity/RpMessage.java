package com.liudehuang.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liudehuang
 * @date 2019/3/19 8:51
 */
@Setter
@Getter
@ToString
public class RpMessage {

    private String id;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 修改人
     */
    private String editor;
    /**
     * 描述
     */
    private String remark;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String editTime;
    /**
     * 消息ID
     */
    private String messageId;
    /**
     * 消息实体
     */
    private String messageBody;
    /**
     * 消息数据类型
     */
    private String messageDataType;
    /**
     * 消费队列
     */
    private String consumerQueue;
    /**
     * 重发次数
     */
    private Integer messageSendTimes;
    /**
     * 是否已经死亡
     */
    private String areadlyDead;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 扩展字段1
     */
    private String field1;
    /**
     * 扩展字段2
     */
    private String field2;

    public void addSendTimes() {
        messageSendTimes++;
    }
}
