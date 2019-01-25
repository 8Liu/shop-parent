package com.liudehuang.dao;

import com.liudehuang.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderDao {

	@Update("update order_info set isPay=#{isPay} ,payId=#{payId" +
			"} where orderNumber=#{orderNumber};")
	int updateOrder(@Param("isPay") Long isPay, @Param("payId") String payId, @Param("orderNumber") String orderNumber);


}
