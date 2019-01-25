package com.liudehuang.dao;

import com.liudehuang.entity.OrderInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author liudehuang
 * @date 2019/1/25 11:18
 */
@Repository
@Mapper
public interface DemoDao {
    @Insert("insert order_info values (null,#{orderName},#{orderDes})")
    int addOrder(OrderInfo orderInfo);
}
