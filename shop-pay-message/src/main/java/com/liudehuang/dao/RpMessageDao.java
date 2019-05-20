package com.liudehuang.dao;

import com.liudehuang.entity.RpMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liudehuang
 * @date 2019/3/19 9:15
 */
@Repository
public interface RpMessageDao {
    /**
     * 存储消息
     * @param rpMessage
     * @return
     */
    int saveMessage(RpMessage rpMessage);

    /**
     * 根据messageId更新消息
     * @param rpMessage
     * @return
     */
    int updateMessageByMessageId(RpMessage rpMessage);

    /**
     * 根据messageId获取消息
     * @param messageId
     * @return
     */
    RpMessage getMessageByMessageId(@Param("messageId") String messageId);

    /**
     * 根据messageId删除消息
     * @param messageId
     * @return
     */
    int deleteMessageByMessageId(@Param("messageId") String messageId);

    /**
     *
     * @param map
     * @return
     */
    List<RpMessage> getMessageByPage(@Param("map") Map<String,Object> map);
}
