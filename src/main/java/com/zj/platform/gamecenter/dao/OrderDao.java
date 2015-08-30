package com.zj.platform.gamecenter.dao;

import java.util.Date;

import com.zj.platform.gamecenter.entity.Order;

public interface OrderDao {

	public void insert(Order order);

	public void cancle(Order order);

	public void batchCancle(Date createTime);

	public void pay(Order order);
}
