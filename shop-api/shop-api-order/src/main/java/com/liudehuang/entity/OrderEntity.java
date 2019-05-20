package com.liudehuang.entity;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderEntity {
	/**
	 * 编号
	 */
	private int id;
	/**
	 * 用户userid
	 */
	private String userId;
	/**
	 * 订单描述
	 */
	private String orderDesc;
	/**
	 * 价格
	 */
	private BigDecimal price;
	/**
	 * 订单编号
	 */
	private String orderNumber;
	/**
	 * 0 未支付 1已支付
	 */
	private Integer isPay;
	/**
	 * 第三方支付id
	 */
	private String payId;
	/**
	 * 创建订单时间
	 */
	private String created;
	/**
	 *	更新订单时间
	 */
	private String updated;
	/**
	 * 非数据库映射字段，只用于传参
	 */
	private String messageId;
}
