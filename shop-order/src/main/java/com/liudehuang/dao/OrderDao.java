package com.liudehuang.dao;

import com.liudehuang.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderDao {

	@Update("update order_info set isPay=#{isPay} ,payId=#{payId" +
			"} where orderNumber=#{orderNumber};")
	int updateOrder(OrderEntity param);

	@Insert("insert into order_info(orderNumber,orderDesc,price,isPay,payId,userId)values(#{orderNumber},#{orderDesc},#{price},#{isPay},#{payId},#{userId})")
	int insertOrder(OrderEntity param);
}
