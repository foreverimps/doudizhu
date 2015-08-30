package com.zj.platform.gamecenter.dao;

import com.zj.platform.gamecenter.entity.Recharge;

public interface RechargeDao {

	public void insert(Recharge recharge);

	public void pay(Recharge recharge);

	public void cacle(Recharge recharge);

}
